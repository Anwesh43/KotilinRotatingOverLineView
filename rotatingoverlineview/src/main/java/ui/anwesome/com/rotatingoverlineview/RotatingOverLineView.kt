package ui.anwesome.com.rotatingoverlineview

/**
 * Created by anweshmishra on 28/01/18.
 */
import android.app.Activity
import android.view.*
import android.graphics.*
import android.content.*
import java.util.concurrent.ConcurrentLinkedQueue

class RotatingOverLineView(ctx:Context,var n:Int = 10):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    var rotatingOverListener:RotatingOverLineListener?=null
    fun addRotatingOverListener( onOpen:(Int)->Unit, onClose:(Int)->Unit) {
        rotatingOverListener = RotatingOverLineListener(onOpen,onClose)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
        val state = RotatingOverLineState()
        fun draw(canvas:Canvas,paint:Paint,x:Float,size:Float) {
            var deg = 180f
            if(i == 0) {
                deg = 90f
            }
            canvas.save()
            canvas.translate(x,size*i)
            canvas.rotate(deg*(1-state.scale))
            if(i!=0 || state.scale != 0f) {
                canvas.drawLine(0f, 0f, 0f, size, paint)
            }
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class RotatingOverLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1-2*scale
                startcb()
            }
        }
    }
    data class RotatingOverLineContainerState(var n:Int, var j:Int = 0,var dir:Int = 1) {
        fun incrementCounter() {
            j += dir
            if(j == n || j == -1) {
                dir *= -1
                j += dir
            }
        }
        fun executeCb(cb:(Int)->Unit) {
            cb(j)
        }
    }
    data class RotatingOverLineContainer(var n:Int,var w:Float,var h:Float,var y_gap:Float = 0f) {
        val lines:ConcurrentLinkedQueue<RotatingOverLine> = ConcurrentLinkedQueue()
        val state = RotatingOverLineContainerState(n)
        init {
            if(n > 0) {
                for (i in 0..n - 1) {
                    lines.add(RotatingOverLine(i))
                }
                y_gap = h/n
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#3F51B5")
            state.executeCb { j -> Int
                for(i in 0..j) {
                    lines.at(i)?.draw(canvas,paint,w/2,y_gap)
                }
            }
            state.executeCb { j ->
                if(n > 0) {
                    val deg = 360/n
                    val size = Math.min(w, h) / 8
                    val scale = lines.at(j)?.state?.scale?:0f
                    paint.color = Color.parseColor("#00838F")
                    for (i in 0..1) {
                        canvas.save()
                        canvas.translate(w / 4 + i * w / 2, h / 2)
                        canvas.drawArc(RectF(-size, -size, size, size), 0f, deg*j+deg*scale,true,paint)
                        canvas.restore()
                    }
                }
            }
        }
        fun update(stopcb:(Float,Int)->Unit) {
            state.executeCb { j ->
                lines.at(j)?.update {
                    stopcb(it,j)
                    state.incrementCounter()
                }

            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.executeCb { j ->
                lines.at(j)?.startUpdating(startcb)
            }
        }
    }
    data class Renderer(var view:RotatingOverLineView,var time:Int = 0) {
        var container:RotatingOverLineContainer?=null
        val animator = Animator(view)
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = RotatingOverLineContainer(view.n,w,h)
                paint.strokeWidth = Math.min(w,h)/45
                paint.strokeCap = Paint.Cap.ROUND
            }
            canvas.drawColor(Color.parseColor("#212121"))
            container?.draw(canvas,paint)
            time++
            animator.animate {
                container?.update {scale,j ->
                    animator.stop()
                    when(scale) {
                        0f -> view.rotatingOverListener?.onCloseListener?.invoke(j)
                        1f -> view.rotatingOverListener?.onOpenListener?.invoke(j)
                    }
                }
            }
        }
        fun handleTap() {
            container?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity):RotatingOverLineView {
            val view = RotatingOverLineView(activity)
            activity.setContentView(view)
            return view
        }
    }
    data class RotatingOverLineListener(var onOpenListener:(Int)->Unit,var onCloseListener:(Int)->Unit)
}
fun ConcurrentLinkedQueue<RotatingOverLineView.RotatingOverLine>.at(j:Int):RotatingOverLineView.RotatingOverLine? {
    var i = 0
    forEach {
        if(i == j) {
            return it
        }
        i++
    }
    return null
}