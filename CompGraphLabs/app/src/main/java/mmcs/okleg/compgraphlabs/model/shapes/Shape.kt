package mmcs.okleg.compgraphlabs.model.shapes

interface  Shape {
    // Use to access and set the view transformation
    fun draw(mvpMatrix: FloatArray)
}