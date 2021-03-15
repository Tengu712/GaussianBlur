package lwjgl;

import static org.lwjgl.opengl.GL33.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

	public int idT;
	public int idS;

	public Texture(String path) {
		ByteBuffer img;
		IntBuffer width, height, comp;
		width = BufferUtils.createIntBuffer(1);
		height = BufferUtils.createIntBuffer(1);
		comp = BufferUtils.createIntBuffer(1);

		img = STBImage.stbi_load(path, width, height, comp, STBImage.STBI_default);
		if (img == null)
			throw new RuntimeException("Failed to load texture.");

		idT = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, idT);
		if (comp.get(0) == 3)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, img);
		else if (comp.get(0) == 4)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, img);
		STBImage.stbi_image_free(img);

		idS = glGenSamplers();
		glSamplerParameteri(idS, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glSamplerParameteri(idS, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glSamplerParameteri(idS, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
		glSamplerParameteri(idS, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void finish() {
		glDeleteTextures(idT);
		glDeleteSamplers(idS);
	}
}
