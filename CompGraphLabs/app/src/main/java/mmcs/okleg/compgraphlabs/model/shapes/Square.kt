package mmcs.okleg.compgraphlabs.model.shapes

import android.opengl.GLES20
import android.opengl.Matrix
import mmcs.okleg.compgraphlabs.model.shaders.FIRST_VERTEX_SHADER
import mmcs.okleg.compgraphlabs.model.shaders.FIRST_FRAGMENT_SHADER
import mmcs.okleg.compgraphlabs.model.shaders.loadShader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
var squareCoords = floatArrayOf(
    -0.5f,  0.5f, 0.0f,      // top left
    -0.5f, -0.5f, 0.0f,      // bottom left
    0.5f, -0.5f, 0.0f,      // bottom right
    0.5f,  0.5f, 0.0f       // top right
)
val squareColor = floatArrayOf(0.0f, 1.0f, 1.0f, 1.0f)

class Square(private var coordinates: FloatArray = squareCoords,
             private var color: FloatArray = squareColor) : Shape {
    // initialize vertex byte buffer for shape coordinates
    private val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * Float.SIZE_BYTES).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(coordinates)
                position(0)
            }
        }
    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0
    private val coordinatesPerVertex = 3
    private val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, FIRST_VERTEX_SHADER)
    private val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, FIRST_FRAGMENT_SHADER)
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices
    private val vertexCount: Int = coordinates.size / coordinatesPerVertex
    private val vertexStride: Int = coordinatesPerVertex * Float.SIZE_BYTES
    // Set color with red, green, blue and alpha (opacity) values
    private var mProgram: Int
    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, FIRST_VERTEX_SHADER)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, FIRST_FRAGMENT_SHADER)

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

    // initialize byte buffer for the draw list
    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    override fun draw(mvpMatrix: FloatArray) {
        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(positionHandle)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                positionHandle,
                coordinatesPerVertex,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(positionHandle)
        }
    }
}

