package lwjgl;

public class Gaussian {
	
	private FrameBuffer fb1;
	private FrameBuffer fb2;
	
	public Gaussian(GLInf ginf) {
		fb1 = new FrameBuffer(ginf);
		fb2 = new FrameBuffer(ginf);
	}
	
	public void startGauss() {
		fb1.bindFrameBuffer();
	}
	
	public void drawGauss() {
		fb2.bindFrameBuffer();
		fb1.drawCurrentBuffer(1);
		fb2.drawBuffer(2);
	}
	
	public void finish() {
		fb1.finish();
		fb2.finish();
	}

}
