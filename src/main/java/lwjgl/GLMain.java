package lwjgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class GLMain {

	public GLInf ginf;

	private Image img1;
	private Image img2;

	public void run(GLInf ginf) {
		this.ginf = ginf;
		ginf.initProgram();
		
		img1 = new Image(ginf.tex1, ginf.shader, 0, 0, -1000, 640, 480);
		img2 = new Image(ginf.tex2, ginf.shader, -300, 300, -1000, 300, 300);
		img1.gauss = 1;
		img2.gauss = 1;
		img2.setColor(0.5, 0.5, 1.0, 0.8);
		
		while (!glfwWindowShouldClose(ginf.hWnd)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if (update())
				break;
			glfwSwapBuffers(ginf.hWnd);
			glfwPollEvents();
		}
		
		img1.finish();
		img2.finish();
		ginf.finish();
	}

	public boolean update() {
		img1.rot[1]++;
		img2.rot[0]++;
		img2.rot[1]++;
		img2.rot[2]++;
		img1.draw();
		img2.draw();
		return false;
	}

	public static void main(String[] args) {
		new GLMain().run(new GLInf(new WindowInf(1280, 960, "test", false)));
	}

}
