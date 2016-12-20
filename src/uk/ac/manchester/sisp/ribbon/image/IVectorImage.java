package uk.ac.manchester.sisp.ribbon.image;

import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.IVectorPathGroup;

public interface IVectorImage extends IImage {
	/* A VectorImage is composed of an array of VectorPathGroups. We supply a FloatStore and VectorPathContext to allow execution at invocation-time. */
	public abstract IVectorPathGroup[] getVectorPathGroups(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext);
}