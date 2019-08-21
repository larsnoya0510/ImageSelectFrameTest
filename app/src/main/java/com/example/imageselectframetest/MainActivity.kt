package com.example.imageselectframetest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.imageselectframetest.CustomView.CropActivity
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    val tmpFile = File(Environment.getExternalStorageDirectory(), "/DCIM/image.jpg")
    var targetWidth =0
    var targetHeight =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Btn_Cut.setOnClickListener {
            var intent_crop=Intent(this,CropActivity::class.java)
            startActivityForResult(intent_crop, 1);
//            var intent_Crop = Intent("com.android.camera.action.CROP")
//            intent_Crop.setType("image/*")
//            intent_Crop.putExtra("circleCrop", "true");
//            intent_Crop.putExtra("outputX", 150);
//            intent_Crop.putExtra("outputY", 150);
//            intent_Crop.putExtra("scale", true)
//            intent_Crop.putExtra("return-data", false)
//            val outputFileUri = Uri.fromFile(tmpFile)
//            intent_Crop.putExtra("output", outputFileUri)
//            intent_Crop.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
//            startActivityForResult(intent_Crop, 1);
        }
        Btn_Cut2.setOnClickListener {
//            saveBitmap(cropBitmap1())
//            var path = (Environment.getExternalStorageDirectory()).path + "/DCIM/image.jpg"
//            val updateTime = System.currentTimeMillis().toString()
//            var potions = RequestOptions()
//            potions.skipMemoryCache(true)
//            potions.diskCacheStrategy(DiskCacheStrategy.NONE)
//            Glide.with(this)
//                .load(path)
//                .apply(RequestOptions.bitmapTransform(CircleCrop()))
//                .apply(potions)
//                .into(imageViewCut)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    var path = (Environment.getExternalStorageDirectory()).path + "/DCIM/image.jpg"
//                    Glide.with(this).load(path).apply(RequestOptions.bitmapTransform(CircleCrop())).into(imageViewCut)
                    var potions = RequestOptions()
                    potions.skipMemoryCache(true)
                    potions.diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(this)
                        .load(path)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .apply(potions)
                        .into(imageViewCut)
                }
            }
        }
    }

    private fun cropBitmap1(): Bitmap {
        val bmp2 = (imageViewOrigin.getDrawable() as BitmapDrawable).bitmap
        val bmOverlay = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        var rectArray = imageView2.getCutArr()
        var srcRect = Rect(rectArray[0].toInt(), rectArray[1].toInt(), rectArray[2].toInt(), rectArray[3].toInt())
        var targetRect = RectF(0F, 0F, 150F, 150F)
        val canvas = Canvas(bmOverlay)
//        canvas.drawBitmap(bmp2, 0F, 0F, null)
        canvas.drawBitmap(bmp2, srcRect, targetRect, null)
//        canvas.drawRect(rectArray[0], rectArray[1], rectArray[2], rectArray[3], paint)
        return bmOverlay
    }
    fun saveBitmap(bitmap: Bitmap) {
        val fOut: FileOutputStream
        var path = (Environment.getExternalStorageDirectory()).path + "/DCIM/image.jpg"
        try {
//            val dir = File("/sdcard/demo/")
//            if (!dir.exists()) {
//                dir.mkdir()
//            }

//            val tmp = "/sdcard/demo/takepicture.jpg"
            if (File(path).exists()) {
                File(path).delete()
            }
            fOut = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)

            try {
                fOut.flush()
                fOut.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }
}
