package com.example.imageselectframetest

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.imageselectframetest.CustomView.CropActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Btn_Select.setOnClickListener {
            var intent_crop=Intent(this,CropActivity::class.java)
            startActivityForResult(intent_crop, 1);
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    var path = (Environment.getExternalStorageDirectory()).path + "/DCIM/image.jpg"
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
}
