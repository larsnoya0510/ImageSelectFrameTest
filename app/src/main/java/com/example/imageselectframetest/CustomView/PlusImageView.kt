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
    private val savedMatrix = Matrix()
    var  dm : DisplayMetrics =context.getResources().getDisplayMetrics()
    enum class modeType(value : Int) {
        NONE (0),
        DRAG (1),
        ZOOM  ( 2)
    }
    var mode = PlusImageView.modeType.NONE //模式
    var start = PointF()
    var mid = PointF()
    var oldDist = 1f

    //圖片縮放合適大小
    fun initialImage(mWidth : Int,mHeight : Int) :Float{
        imageViewWidth=mWidth
        imageViewHeight=mHeight
//        Log.d("Infor", "檢查 縮放倍率")
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
//        Log.d("Infor", "height"+mHeight.toString())
//        Log.d("Infor", "screenHeight"+screenHeight.toString())
//        Log.d("Infor", "width"+mWidth.toString())
//        Log.d("Infor", "screenWidth"+screenWidth.toString())
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
    fun spacing( event: MotionEvent) : Float{
        var x : Float= event.getX(0) - event.getX(1)
        var y : Float= event.getY(0) - event.getY(1)
        return Math.sqrt(x.toDouble() * x.toDouble() + y.toDouble() * y.toDouble()).toFloat()

    }
    fun midPoint( point :PointF,  event : MotionEvent) {
        var x : Float = event.getX(0) +  event.getX(1)
        var y : Float = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }
    //圖片置中
    fun center(vertical : Boolean, horizontal : Boolean){
        val m = Matrix()
        m.set(targetMatrix)
//        val rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        val rect = RectF(0f, 0f, imageViewWidth.toFloat(), imageViewHeight.toFloat())
        m.mapRect(rect)
        val height = rect.height()
        val width = rect.width()

        var deltaX = 0f
        var deltaY = 0f

        if (vertical) {
            // 圖片小於螢幕大小，則置中顯示。
            //大於螢幕，上方則留空白則往上移，下方留空白則往下移
            val screenHeight = dm.heightPixels
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top
            } else if (rect.top > 0) {
                deltaY = -rect.top
            } else if (rect.bottom < screenHeight) {
                deltaY = this.getHeight() - rect.bottom
            }
        }
        if (horizontal) {
            val screenWidth = dm.widthPixels
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left
            } else if (rect.left > 0) {
                deltaX = -rect.left
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right
            }
        }
        targetMatrix.postTranslate(deltaX, deltaY)
    }
}