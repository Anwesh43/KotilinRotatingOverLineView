package ui.anwesome.com.kotlinrotatinglineoverview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.rotatingoverlineview.RotatingOverLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RotatingOverLineView.create(this)
    }
}
