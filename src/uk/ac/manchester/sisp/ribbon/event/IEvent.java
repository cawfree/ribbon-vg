package uk.ac.manchester.sisp.ribbon.event;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.common.ITimedObject;

public interface IEvent extends ITimedObject, IDisposable { 
	
	public static class Impl implements IEvent {
		
		/* Member Variables. */
		private float mEventTimeSeconds;
		
		public Impl(final float pEventTimeSeconds) {
			/* Initialize Member Variables. */
			this.mEventTimeSeconds = pEventTimeSeconds;
		}
		
		@Override
		public float getObjectTimeSeconds() {
			return this.mEventTimeSeconds;
		}
		
		@Override
		public void dispose() {
			
		}
		
	};
	
}
