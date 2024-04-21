package mmcs.okleg.compgraphlabs.model.shapes 

import android.opengl.GLES20
import android.opengl.Matrix
import mmcs.okleg.compgraphlabs.model.shaders.FIRST_FRAGMENT_SHADER
import mmcs.okleg.compgraphlabs.model.shaders.FIRST_VERTEX_SHADER
import mmcs.okleg.compgraphlabs.model.shaders.MULTICOLOR_FRAGMENT_SHADER
import mmcs.okleg.compgraphlabs.model.shaders.MULTICOLOR_VERTEX_SHADER
import mmcs.okleg.compgraphlabs.model.shaders.loadShader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
// number of coordinates per vertex in this array
const val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)
val triangleColors = floatArrayOf(
    1.0f, 0.0f, 0.0f, 1.0f,
    0.0f, 1.0f, 0.0f, 1.0f,
    0.0f, 0.0f, 1.0f, 1.0f
)
class Triangle(private var coordinates: FloatArray = triangleCoords,
               private var colors: FloatArray = triangleColors) : Shape {
    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0
    private var mProgram: Int
    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, MULTICOLOR_VERTEX_SHADER)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, MULTICOLOR_FRAGMENT_SHADER)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }

    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(triangleCoords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }
    private val colorBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(colors.size * Float.SIZE_BYTES).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(colors)
                position(0)
            }
        }
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    private val channelsPerColor = 4
    private val colorStride: Int = channelsPerColor * Float.SIZE_BYTES

    override fun draw(mvpMatrix: FloatArray) {
        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(positionHandle)
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                positionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }
            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "aColor").also { colorHandle ->
                GLES20.glVertexAttribPointer(
                    mColorHandle,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    colorStride,
                    colorBuffer
                )
            }
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(mColorHandle)
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(mColorHandle)

    }
}