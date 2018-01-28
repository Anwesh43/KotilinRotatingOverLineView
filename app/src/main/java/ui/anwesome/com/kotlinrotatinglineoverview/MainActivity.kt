package ui.anwesome.com.kotlinrotatinglineoverview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ui.anwesome.com.rotatingoverlineview.RotatingOverLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = RotatingOverLineView.create(this)
        view.addRotatingOverListener({j ->
            Toast.makeText(this,"$j is opened",Toast.LENGTH_SHORT).show()
        },{j ->
            Toast.makeText(this,"$j is closed",Toast.LENGTH_SHORT).show()
        })
        fullScreen()
    }
}
fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
