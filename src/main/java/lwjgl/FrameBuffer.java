package lwjgl;

import static org.lwjgl.opengl.GL33.*;

public class FrameBuffer {

	private int width;
	private int height;

	private int frameBuffer;
	private int renderedTexture;
	private int renderedSampler;
	private int depthRenderBuffer;

	private Image screen;

	public FrameBuffer(GLInf ginf) {
		this.width = ginf.winf.width;
		this.height = ginf.winf.height;
		screen = new Image(null, ginf.shader, 0, 0, -1000, width, height);
		screen.setUV(new double[] { 1, 1, 0, 1, 0, 0, 1, 0 });

		frameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		renderedTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, renderedTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

		renderedSampler = glGenSamplers();
		glSamplerParameteri(renderedSampler, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glSamplerParameteri(renderedSampler, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glSamplerParameteri(renderedSampler, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
		glSamplerParameteri(renderedSampler, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);

		depthRenderBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBuffer);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, renderedTexture, 0);
		glDrawBuffers(GL_COLOR_ATTACHMENT0);
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			throw new RuntimeException("Failed to use frame buffer.");

		clear();
	}

	public void bindFrameBuffer() {
		clear();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, width, height);
	}

	public void drawBuffer(int gauss) {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, width, height);
		drawCurrentBuffer(gauss);
	}

	public void drawCurrentBuffer(int gauss) {
		if (gauss > 0) {
			screen.gauss = gauss;
			if (gauss == 1)
				screen.horizon = 1;
			else
				screen.horizon = 0;
		}
		screen.draw(renderedTexture, renderedSampler);
		clear();
	}

	public int getFrameBufferID() {
		return frameBuffer;
	}

	private void clear() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
	}

	public void finish() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);

		glDeleteTextures(renderedTexture);
		glDeleteSamplers(renderedSampler);
		glDeleteBuffers(depthRenderBuffer);
		glDeleteBuffers(frameBuffer);
	}
}
