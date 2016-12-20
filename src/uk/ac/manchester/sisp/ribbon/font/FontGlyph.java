package uk.ac.manchester.sisp.ribbon.font;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;

public class FontGlyph implements IDisposable {
	
	/* Member Variables. */
	private       VectorPath mVectorPath; /** TODO: IBounds! **/
	private final int        mMinimumX;
	private final int        mMinimumY;
	private final int        mMaximumX;
	private final int        mMaximumY;
	
	public FontGlyph(final VectorPath pVectorPath, final int pMinimumX, final int pMinimumY, final int pMaximumX, final int pMaximumY) {
		/* Assign Member Variables. */
		this.mVectorPath = pVectorPath;
		this.mMinimumX   = pMinimumX;
		this.mMinimumY   = pMinimumY;
		this.mMaximumX   = pMaximumX;
		this.mMaximumY   = pMaximumY;
	}
	
	@Override
	public final void dispose() {
		/* Dispose of the VectorPath. */
		this.getVectorPath().dispose();
		/* Nullify the VectorPath. */
		this.mVectorPath = null;
	}
	
	public final VectorPath getVectorPath() {
		return this.mVectorPath;
	}
	
	public final int getMinimumX() {
		return this.mMinimumX;
	}
	
	public final int getMinimumY() {
		return this.mMinimumY;
	}
	
	public final int getMaximumX() {
		return this.mMaximumX;
	}
	
	public final int getMaximumY() {
		return this.mMaximumY;
	}

}
