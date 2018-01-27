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
}