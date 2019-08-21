package com.example.imageselectframetest.CustomView

import android.app.Activity
import android.app.Instrumentation
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.imageselectframetest.R
import kotlinx.android.synthetic.main.activity_crop.Btn_Cut
import kotlinx.android.synthetic.main.activity_crop.imageView2
import kotlinx.android.synthetic.main.activity_crop.imageViewOrigin
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CropActivity : AppCompatActivity() {
    var scaleRate:Float=0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        imageViewOrigin.setImageResource(R.drawable.ca)
        var targetWidth = imageViewOrigin.drawable.intrinsicWidth
        var targetHeight = imageViewOrigin.drawable.intrinsicHeight

        scaleRate= imageViewOrigin.initialImage(targetWidth,targetHeight)
        Btn_Cut.setOnClickListener {
            saveBitmap(cropBitmap1(scaleRate))
            this.setResult(Activity.RESULT_OK)
            this.finish()
        }
    }
    fun saveBitmap(bitmap: Bitmap) {
        val fOut: FileOutputStream
        var path = (Environment.getExternalStorageDirectory()).path + "/DCIM/image.jpg"
        try {
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
    private fun cropBitmap1(mSaleRate:Float): Bitmap {
        val bmp2 = (imageViewOrigin.getDrawable() as BitmapDrawable).bitmap
        val bmOverlay = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        var rectArray = imageView2.getCutArr()
        var srcRect = Rect((rectArray[0]/mSaleRate).toInt(), (rectArray[1]/mSaleRate).toInt(), (rectArray[2]/mSaleRate).toInt(), (rectArray[3]/mSaleRate).toInt())
        var targetRect = RectF(0F, 0F, 150F, 150F)
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(bmp2, srcRect, targetRect, null)
        return bmOverlay
    }
}
