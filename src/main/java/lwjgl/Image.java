package lwjgl;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Image {

	// API
	private int vao;
	private int bufVtx;
	private int bufCol;
	private int bufUV;

	// Object
	public int gauss;
	public double[] wh;
	public double[] uv;
	public double[] pos;
	public double[] rot;
	public double[] scl;
	public double[] col;
	private Shader shader;
	private Texture tex;

	public Image(Texture tex, Shader shader, double x, double y, double z, double w, double h) {

		this.shader = shader;
		this.tex = tex;
		gauss = 0;
		wh = new double[] { w, h };
		uv = new double[] { 1, 0, 0, 0, 0, 1, 1, 1 };
		pos = new double[] { x, y, z };
		rot = new double[] { 0.0, 0.0, 0.0 };
		scl = new double[] { 1.0, 1.0, 1.0 };
		col = new double[16];
		Arrays.fill(col, 1.0);

		double[] vtx = new double[12];
		vtx = new double[] { w / 2., h / 2., 0., -w / 2., h / 2., 0., -w / 2., -h / 2., 0., w / 2., -h / 2., 0. };

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		bufVtx = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bufVtx);
		glBufferData(GL_ARRAY_BUFFER, vtx, GL_STREAM_DRAW);
		glVertexAttribPointer(0, 3, GL_DOUBLE, false, 0, 0);

		bufCol = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bufCol);
		glBufferData(GL_ARRAY_BUFFER, col, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(1, 4, GL_DOUBLE, false, 0, 0);

		bufUV = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bufUV);
		glBufferData(GL_ARRAY_BUFFER, uv, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_DOUBLE, false, 0, 0);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);

	}

	public void draw() {

		glUseProgram(shader.program);

		final FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		Matrix4f model = new Matrix4f().identity();
		model.translate((float) pos[0], (float) pos[1], (float) pos[2]);
		model.scale((float) scl[0], (float) scl[1], (float) scl[2]);
		model.rotate((float) (Math.toRadians(rot[0])), 1, 0, 0);
		model.rotate((float) (Math.toRadians(rot[1])), 0, 1, 0);
		model.rotate((float) (Math.toRadians(rot[2])), 0, 0, 1);
		glUniformMatrix4fv(glGetUniformLocation(shader.program, "model"), false, model.get(fb));
		glUniform1i(glGetUniformLocation(shader.program, "texSampler"), 0);
		glUniform1i(glGetUniformLocation(shader.program, "gaussian"), gauss);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.idT);
		glBindSampler(0, tex.idS);

		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
		glBindVertexArray(0);

		glBindSampler(0, 0);
		glBindTexture(GL_TEXTURE_2D, 0);

		glUseProgram(0);
	}

	public void setWH(double w, double h) {
		wh = new double[] { w, h };
		double[] vtx = new double[12];
		vtx = new double[] { -w / 2., h / 2., 0., -w / 2., -h / 2., 0., w / 2., h / 2., 0., w / 2., -h / 2., 0. };
		glBindBuffer(GL_ARRAY_BUFFER, bufVtx);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vtx);
	}

	public void setColor(double r, double g, double b, double a) {
		col = new double[16];
		for (int i = 0; i < 16; ++i) {
			if (i % 4 == 0)
				col[i] = r;
			else if (i % 4 == 1)
				col[i] = g;
			else if (i % 4 == 2)
				col[i] = b;
			else if (i % 4 == 3)
				col[i] = a;
		}
		glBindBuffer(GL_ARRAY_BUFFER, bufCol);
		glBufferSubData(GL_ARRAY_BUFFER, 0, col);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void finish() {
		glDeleteBuffers(bufVtx);
		glDeleteBuffers(bufCol);
		glDeleteBuffers(bufUV);
		glDeleteVertexArrays(vao);
	}
}
