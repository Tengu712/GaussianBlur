package lwjgl;

public class WindowInf {
	
	public final int width;
	public final int height;
	public final String title;
	public final boolean fullScreen;
	
	public WindowInf(int width, int height, String title, boolean fullScreen) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.fullScreen = fullScreen;
	}

}
