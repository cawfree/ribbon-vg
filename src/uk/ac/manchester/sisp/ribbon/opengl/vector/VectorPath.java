package uk.ac.manchester.sisp.ribbon.opengl.vector;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;

/** TODO: A single vector path must be one outer polygon and the rest holes. Cannot contain multiple paths. **/
public class VectorPath implements IDisposable { /** TODO: Implement IBounds! **/
	
	/* Member Variables. */
	private float[] mPathData;
	
	protected VectorPath(final float[] pPathData) {
		/* Initialize Member Variables. */
		this.mPathData = pPathData;
	}

	@Override
	public void dispose() {
		/* Nullify path data. */
		this.mPathData = null;
	}
	
	public final float[] getPathData() {
		return this.mPathData;
	}
	
}