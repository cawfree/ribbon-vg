package uk.ac.manchester.sisp.ribbon.image.svg;

import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.image.IVectorImage;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.IVectorPathGroup;

public final class SVGImage implements IVectorImage, IDim2.F {
	
	/* Member Variables. */
	private       IVectorPathGroup[] mVectorPathGroups;
	private final float                  mWidth;
	private final float                  mHeight;
	
	protected SVGImage(final IVectorPathGroup[] pVectorPathGroups, final float pWidth, final float pHeight) {
		/* Initialize Member Variables. */
		this.mVectorPathGroups = pVectorPathGroups;
		this.mWidth            = pWidth;
		this.mHeight           = pHeight;
	}

	@Override public final float getWidth()  { return this.mWidth;  }
	@Override public final float getHeight() { return this.mHeight; }

	@Override public final void dispose() {
		/* Nullify the VectorPathGroups in an attempt to invoke the Garbage Collector. */
		this.mVectorPathGroups = null;
	}
	
	public final IVectorPathGroup[] getVectorPathGroups(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext) {
		/* We buffer the VectorPathGroups, so runtime generation is not required. */
		return this.mVectorPathGroups;
	}

}
