package mmcs.okleg.compgraphlabs.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import mmcs.okleg.compgraphlabs.viewmodel.MyGLRenderer

class SurfaceView(context: Context): GLSurfaceView(context) {
    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}