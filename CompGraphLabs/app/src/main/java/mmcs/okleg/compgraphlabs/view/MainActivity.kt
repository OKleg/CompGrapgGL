package mmcs.okleg.compgraphlabs.view

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import mmcs.okleg.compgraphlabs.viewmodel.MyGLRenderer


class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = SurfaceView(this)
        setContentView(gLView)
    }

}