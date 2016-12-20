package uk.ac.manchester.sisp.ribbon.common;

public interface IOpacity {
	
	public static class Impl implements IOpacity.W {
		/* Member Variables. */
		private float mOpacity;
		/* Constructor. */
		public Impl(final float pOpacity) {
			/* Initialize Member Variables. */
			this.mOpacity = pOpacity;
		}
		/* Getters and Setters. */
		public void  setOpacity(final float pOpacity) { this.mOpacity = pOpacity; }
		public float getOpacity()                     { return this.mOpacity;     }
	}
	
	public static interface W extends IOpacity {
		/* Setter for Opacity. */
		public abstract void setOpacity(final float pOpacity);
	}
	
	/* Returns a value of Opacity in the range 0.0f -> 1.0f. */
	public abstract float getOpacity();
	
}