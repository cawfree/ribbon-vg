package uk.ac.manchester.sisp.ribbon.common;

public interface IVisible {
	
	public static class Impl implements IVisible.W {
		/* Member Variables. */
		private boolean mVisible;
		/* Constructor. */
		public Impl(final boolean pIsVisible) {
			this.mVisible = pIsVisible;
		}
		/* Getters and Setters. */
		@Override public void setVisible(final boolean pIsVisible) {
			this.mVisible = pIsVisible;
		}
		@Override public boolean isVisible() {
			return this.mVisible;
		}
	}
	
	public static interface W extends IVisible {
		public abstract void    setVisible(final boolean pIsVisible);
	}
	
	public abstract boolean isVisible();

}
