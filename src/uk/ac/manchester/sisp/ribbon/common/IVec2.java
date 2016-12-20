package uk.ac.manchester.sisp.ribbon.common;

import java.io.Serializable;
import java.util.Comparator;

public interface IVec2 extends Serializable {
	
	/* Sorts Vectors Horizontally. */
	public static final Comparator<IVec2.I> COMPARATOR_HORIZONTAL = new Comparator<IVec2.I>() { 
		/* Define the Comparison methodology. */
		@Override public final int compare(final IVec2.I arg0, final IVec2.I arg1) { 
			/* Use the standard Integer comparison function. */
			return Integer.compare(arg0.getX(), arg1.getX());
		} 
	};
	
	/* Sorts Vectors Vertically. */
	public static final Comparator<IVec2.I> COMPARATOR_VERTICAL = new Comparator<IVec2.I>() { 
		/* Define the Comparison methodology. */
		@Override public final int compare(final IVec2.I arg0, final IVec2.I arg1) { 
			/* Use the standard Integer comparison function. */
			return Integer.compare(arg0.getY(), arg1.getY());
		} 
	};
	
	/* Static Constants. */
	public static final int NUM_COMPONENTS = 2;
	
	public static interface I extends IVec2 {
		
		public static class Impl implements I.W {
			
			/* Define the default Serialization UID. */
			private static final long serialVersionUID = 1L;
			
			/* Member Variables. */
			private int mX;
			private int mY;
			
			public Impl() {
				this(0, 0);
			}
			
			public Impl(final IVec2.I pVec2) {
				this(pVec2.getX(), pVec2.getY());
			}
			
			public Impl(final int pX, final int pY) {
				/* Initialize Member Variables. */
				this.mX = pX;
				this.mY = pY;
			}
			
			public void setX(final int pX) { this.mX = pX;   }
			public int  getX()             { return this.mX; }
			public void setY(final int pY) { this.mY = pY;   }
			public int  getY()             { return this.mY; }
			
		}
		
		public interface W extends I {
			public abstract void setX(final int pX);
			public abstract void setY(final int pY);
		}
		
		public abstract int getX();
		public abstract int getY();
		
	}
	
	public static interface F extends IVec2 {
		
		public static class Impl implements F.W {
			
			/* Define the default Serialization UID. */
			private static final long serialVersionUID = 1L;
			
			/* Member Variables. */
			private float mX;
			private float mY;
			
			public Impl() {
				this(0.0f, 0.0f);
			}
			
			public Impl(final float pX, final float pY) {
				/* Initialize Member Variables. */
				this.mX = pX;
				this.mY = pY;
			}
			
			public void   setX(final float pX) { this.mX = pX;   }
			public float  getX()               { return this.mX; }
			public void   setY(final float pY) { this.mY = pY;   }
			public float  getY()               { return this.mY; }
			
		}
		
		public interface W extends F {
			public abstract void setX(final float pX);
			public abstract void setY(final float pY);
		}
		
		public abstract float getX();
		public abstract float getY();
		
	}
	

//	
//	public static class Impl implements IVec2.W {
//		
//		/* Member Variables. */
//		private float mX;
//		private float mY;
//		
//		public Impl() {
//			/* Initialize Member Variables. */
//			this(0.0f, 0.0f);
//		}
//		
//		public Impl(final IVec2 pVec2) {
//			/* Initialize Member Variables. */
//			this(pVec2.getX(), pVec2.getY());
//		}
//		
//		public Impl(final float pX, final float pY) {
//			/* Initialize Member Variables. */
//			this.mX = pX;
//			this.mY = pY;
//		}
//
//		@Override public void setX(final float pX) {
//			this.mX = pX;
//		}
//
//		@Override public float getX() {
//			return this.mX;
//		}
//
//		@Override public void setY(final float pY) {
//			this.mY = pY;
//		}
//
//		@Override public float getY() {
//			return this.mY;
//		}
//		
//	}
//	
//	public static interface W extends IVec2 {
//		public abstract void  setX(final float pX);
//		public abstract void  setY(final float pY);
//	}
//	
//	public abstract float getX();
//	public abstract float getY();
	
}
