package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.common.IInverse;

public enum EWindingOrder implements IInverse<EWindingOrder> {
	CW       { @Override public final EWindingOrder getInverse() { return EWindingOrder.CCW;      } @Override public final float getSign() { return +1.0f; } @Override public final EHull getCorrespondingHull() { return EHull.CONVEX;  } },
	CCW      { @Override public final EWindingOrder getInverse() { return EWindingOrder.CW;       } @Override public final float getSign() { return -1.0f; } @Override public final EHull getCorrespondingHull() { return EHull.CONCAVE; } },
	COLINEAR { @Override public final EWindingOrder getInverse() { return EWindingOrder.COLINEAR; } @Override public final float getSign() { return +0.0f; } @Override public final EHull getCorrespondingHull() { return EHull.CONCAVE;          } };
	
	public abstract float         getSign();
	public abstract EHull         getCorrespondingHull();
}
