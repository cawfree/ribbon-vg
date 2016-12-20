package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.common.IInverse;

public enum EHull implements IInverse<EHull> {
	
	CONVEX {
		@Override public final float    getBezierStart()   { return  0.0f; }
		@Override public final float    getBezierControl() { return +0.5f; }
		@Override public final float    getBezierEnd()     { return +1.0f; }
		@Override public final EHull    getInverse()       { return EHull.CONCAVE; }
	}, 
	CONCAVE {
		@Override public final float    getBezierStart()   { return  0.0f; }
		@Override public final float    getBezierControl() { return -0.5f; }
		@Override public final float    getBezierEnd()     { return -1.0f; }
		@Override public final EHull    getInverse()       { return EHull.CONVEX; }
	};

	public abstract float getBezierStart();
	public abstract float getBezierControl();
	public abstract float getBezierEnd();
	
}
