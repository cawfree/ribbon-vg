package uk.ac.manchester.sisp.ribbon.common;

public interface ITimedObject {
	
	/* An externally modifiable TimedObject interface. */
	public static interface W extends ITimedObject {
		/* Set the ObjectTimeSettings. */
		public abstract void setObjectTimeSeconds(final float pObjectTimeSeconds);
	}
	
	/* Returns the time associated with a TimedObject in seconds. */
	public abstract float getObjectTimeSeconds();
}