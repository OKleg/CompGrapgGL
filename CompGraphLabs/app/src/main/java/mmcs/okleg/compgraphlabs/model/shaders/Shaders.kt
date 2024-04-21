package mmcs.okleg.compgraphlabs.model.shaders

import android.opengl.GLES20
// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
fun loadShader(type: Int, shaderCode: String): Int =
    GLES20.glCreateShader(type).also { shader ->
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }