package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

public interface IStroke {
	
	public static interface W extends IStroke {
		public abstract void setStrokeWidth(final float pStrokeWidth);
		public abstract void setLineJoin(final ELineJoin pLineJoin);
		public abstract void setLineCap(final ELineCap pLineCap);
	}
	
	public abstract float     getStrokeWidth();
	public abstract ELineJoin getLineJoin();
	public abstract ELineCap  getLineCap();

}