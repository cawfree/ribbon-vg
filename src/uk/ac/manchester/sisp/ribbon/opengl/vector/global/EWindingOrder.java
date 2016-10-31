package uk.ac.manchester.sisp.ribbon.opengl.vector.global;




public enum EWindingOrder {
	CW       { @Override public final EWindingOrder getOppositeWinding() { return EWindingOrder.CCW;      } @Override public final float getSign() { return +1.0f; } @Override public final EHull getCorrespondingHull() { return EHull.CONVEX;  } },
	CCW      { @Override public final EWindingOrder getOppositeWinding() { return EWindingOrder.CW;       } @Override public final float getSign() { return -1.0f; } @Override public final EHull getCorrespondingHull() { return EHull.CONCAVE; } },
	COLINEAR { @Override public final EWindingOrder getOppositeWinding() { return EWindingOrder.COLINEAR; } @Override public final float getSign() { return +0.0f; } @Override public final EHull getCorrespondingHull() { return EHull.CONCAVE;          } };
	
	public abstract EWindingOrder getOppositeWinding();
	public abstract float         getSign();
	public abstract EHull         getCorrespondingHull();
}
