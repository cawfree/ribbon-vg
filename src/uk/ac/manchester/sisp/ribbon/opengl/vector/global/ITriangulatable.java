package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;

public interface ITriangulatable {
	/* Defines the mode of triangulation for a specific VectorPath. */
	public abstract void onTriangulate(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final VectorPath pVectorPath);
}