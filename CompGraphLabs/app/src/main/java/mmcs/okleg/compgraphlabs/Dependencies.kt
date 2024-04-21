package mmcs.okleg.compgraphlabs

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
//import mmcs.okleg.compgraphlabs.model.light.PointLight
//import mmcs.okleg.compgraphlabs.model.utility.PlatformMode
//import mmcs.okleg.compgraphlabs.utils.TextureLoader

@SuppressLint("StaticFieldLeak")
object Dependencies {
    lateinit var activity: ComponentActivity
    lateinit var context: Context
    //val textureLoader = TextureLoader()
}