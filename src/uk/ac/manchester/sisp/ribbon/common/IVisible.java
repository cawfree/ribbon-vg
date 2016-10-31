package uk.ac.manchester.sisp.ribbon.common;

public interface IVisible {
	
	public static interface W extends IVisible {
		public abstract void    setVisible(final boolean pIsVisible);
	}
	
	public abstract boolean isVisible();

}
