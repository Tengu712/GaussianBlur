package lwjgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWVidMode;

public class GLInf {

	public long hWnd;
	public Shader shader;
	public WindowInf winf;

	public Texture tex1;
	public Texture tex2;

	public GLInf(WindowInf winf) {
		this.winf = winf;
	}
	
	public void initProgram() {
		initGLFW();
		initRender();
	}

	public void finish() {
		tex1.finish();
		tex2.finish();
		shader.finish();
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		Callbacks.glfwFreeCallbacks(hWnd);
	}

	private void initGLFW() {
		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize glfw.");
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		hWnd = glfwCreateWindow(winf.width, winf.height, winf.title, winf.fullScreen ? glfwGetPrimaryMonitor() : 0, 0);
		if (hWnd == 0)
			throw new RuntimeException("Failed to create the window.");
		final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(hWnd, (vidmode.width() - winf.width) / 2, (vidmode.height() - winf.height) / 2);
		glfwMakeContextCurrent(hWnd);
		glfwSwapInterval(1);
		glfwShowWindow(hWnd);
	}

	private void initRender() {
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, winf.width, winf.height);
		shader = new Shader("./src/main/shader");
		
		tex1 = new Texture("./src/main/tex1.png");
		tex2 = new Texture("./src/main/tex2.png");
	}

}
