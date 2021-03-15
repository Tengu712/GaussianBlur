package lwjgl;

import static org.lwjgl.opengl.GL33.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {

	public int program;

	public Shader(String name) {
		final int shaderV = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(shaderV, readFile(name + ".vert"));
		glCompileShader(shaderV);
		if (glGetShaderi(shaderV, GL_COMPILE_STATUS) != GL_TRUE)
			throw new RuntimeException(glGetShaderInfoLog(shaderV));
		final int shaderF = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(shaderF, readFile(name + ".frag"));
		glCompileShader(shaderF);
		if (glGetShaderi(shaderF, GL_COMPILE_STATUS) != GL_TRUE)
			throw new RuntimeException(glGetShaderInfoLog(shaderF));
		program = glCreateProgram();
		glAttachShader(program, shaderV);
		glAttachShader(program, shaderF);
		glDeleteShader(shaderV);
		glDeleteShader(shaderF);
		glLinkProgram(program);
		glValidateProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE)
			throw new RuntimeException(glGetShaderInfoLog(program));

		glUseProgram(program);
		final FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		final Matrix4f projection = new Matrix4f().frustum(-320, 320, -240, 240, 500, 1500);
		final Matrix4f model = new Matrix4f().identity();
		glUniformMatrix4fv(glGetUniformLocation(program, "projection"), false, projection.get(fb));
		glUniformMatrix4fv(glGetUniformLocation(program, "model"), false, model.get(fb));
		glUniform1i(glGetUniformLocation(program, "gaussian"), 0);
		glUseProgram(0);
	}

	public void finish() {
		glDeleteProgram(program);
	}

	private String readFile(String fileName) {
		StringBuilder str = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while ((line = br.readLine()) != null) {
				str.append(line);
				str.append("\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}

}
