package com.example.imageselectframetest.CustomView

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import android.R.attr.bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.RectF
import android.R.attr.right
import android.R.attr.left
import android.R.attr.bottom
import android.opengl.ETC1.getHeight
import android.R.attr.top
class PlusImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    var imageViewWidth : Int = 0
    var imageViewHeight : Int = 0
    private val targetMatrix = Matrix()
    var  dm : DisplayMetrics =context.getResources().getDisplayMetrics()
    enum class modeType(value : Int) {
        NONE (0),
        DRAG (1),
        ZOOM  ( 2)
    }

    //圖片縮放合適大小
    fun initialImage(mWidth : Int,mHeight : Int) :Float{
        imageViewWidth=mWidth
        imageViewHeight=mHeight
        val screenHeight = dm.heightPixels
        val screenWidth = dm.widthPixels
        targetMatrix.set(getImageMatrix())
        val m = Matrix()
        m.set(targetMatrix)
        val rect = RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
        m.mapRect(rect)
        val height = rect.height()
        val width = rect.width()
        var max : Float=0F
        if(height >= screenHeight && width >= screenWidth){
            if(width/screenWidth>height/screenHeight) {
                max=(screenWidth/width)
            } else{
                max=(screenHeight/height)
            }
//            Log.d("Infor", "W H 縮放倍率"+max.toString())
            targetMatrix.setScale(max,max)
            setImageMatrix(targetMatrix)
        }
        else if(height >= screenHeight  && width < screenWidth){
            max=(screenHeight/height)
            Log.d("Infor", " H 縮放倍率"+max.toString())
            targetMatrix.setScale(max,max)
            setImageMatrix(targetMatrix)
        }
        else if(width >= screenWidth  && height < screenHeight){
            max=(screenHeight/height)
            Log.d("Infor", "W  縮放倍率"+max.toString())
            targetMatrix.setScale(max,max)
            setImageMatrix(targetMatrix)
        }
        return max
    }
}