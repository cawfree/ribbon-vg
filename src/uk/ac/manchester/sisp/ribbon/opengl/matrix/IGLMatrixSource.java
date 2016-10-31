package uk.ac.manchester.sisp.ribbon.opengl.matrix;

import java.util.LinkedList;

public interface IGLMatrixSource {

	public abstract float[]              getModelMatrix();
	public abstract float[]              getViewMatrix();
	public abstract float[]              getProjectionMatrix();
	public abstract LinkedList<float[]>  getMatrixStack();

}
