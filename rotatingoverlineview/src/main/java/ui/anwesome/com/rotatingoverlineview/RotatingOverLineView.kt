package ui.anwesome.com.rotatingoverlineview

/**
 * Created by anweshmishra on 28/01/18.
 */
import android.view.*
import android.graphics.*
import android.content.*
class RotatingOverLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class Animator(var view:View,var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class RotatingOverLine(var i:Int) {
        fun draw(canvas:Canvas,paint:Paint,x:Float,size:Float) {
            canvas.save()
            canvas.translate(x,size*i)
            canvas.rotate(180f*(1))
            canvas.drawLine(0f,0f,0f,size,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}