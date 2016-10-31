package uk.ac.manchester.sisp.ribbon.common;

public interface IEnabled {
	
	public static interface W extends IEnabled {
		public abstract void setEnabled(final boolean pIsEnabled);
	}
	
	public abstract boolean isEnabled();

}
