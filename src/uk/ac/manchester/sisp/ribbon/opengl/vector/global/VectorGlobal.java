package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;

public final class VectorGlobal {
	
	public static final float COLINEARITY_TOLERANCE = 0.35f;
	
	public static final ArrayStore.Float onStoreTriangle(final ArrayStore.Float pFloatStore, final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2) {
		/* Deploy triangle vertices. */
		pFloatStore.store(new float[]{ pX0, pY0, 0.0f, 0.0f, pX1, pY1, 0.0f, 0.0f, pX2, pY2, 0.0f, 0.0f });
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	/** TODO: Check that this works, then roll out into a single FloatStore store() call. **/
	public static final ArrayStore.Float onStoreRectangle(final ArrayStore.Float pFloatStore, final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
		/* Deploy a rectangle composed of two triangles. */
		VectorGlobal.onStoreTriangle(pFloatStore, pX0, pY0, pX1, pY1, pX2, pY2);
		VectorGlobal.onStoreTriangle(pFloatStore, pX1, pY1, pX2, pY2, pX3, pY3);
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	public static final ArrayStore.Float onStoreBoundingBox(final ArrayStore.Float pFloatStore, final IDim2 pDim2) {
		/* Store the Dim2's dimensions. */
		VectorGlobal.onStoreRectangle(pFloatStore, 0.0f, 0.0f, 0.0f, pDim2.getHeight(), pDim2.getWidth(), 0, pDim2.getWidth(), pDim2.getHeight());
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	/* Prevent instantiation of this class. */
	private VectorGlobal() {}
	
}