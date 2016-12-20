package uk.ac.manchester.sisp.ribbon.ui.time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UIElapsedFilter {
	
	/* Member Variables. */
	private       float        mStartTimeSeconds;
	private       float        mDurationSeconds;
	private final List<Object> mDeferList;
	
	public UIElapsedFilter(final float pStartTimeSeconds, final float pDurationSeconds) {
		/* Initialize Member Variables. */
		this.mStartTimeSeconds = pStartTimeSeconds;
		this.mDurationSeconds  = pDurationSeconds;
		this.mDeferList        = Collections.synchronizedList(new ArrayList<Object>());
	}
	
//	public synchronized final void onRegisterCheckpoint(final float pCurrentTimeSeconds, final float pDurationSeconds) {
//		/* Calculate the ElapsedTime using the parameters. */
//		final float   lElapsedTime = pCurrentTimeSeconds + pDurationSeconds;
//		/* Determine whether the parameters should override the member variables. */
//		final boolean lIsLonger    = lElapsedTime > (this.getStartTimeSeconds() + this.getDurationSeconds());
//		/* Determine whether to perform the update. */
//		if(lIsLonger) {
//			/* Update the variables. */
//			this.mStartTimeSeconds = pCurrentTimeSeconds;
//			this.mDurationSeconds  = pDurationSeconds;
//		}
//	}
	
	/* Determines whether input is in a state that does not have to be deferred. This is when the checkpoint has been exceeded, and the DeferList is empty. */
	public final boolean isElapsed(final float pCurrentTimeSeconds) { 
		/* Synchronize along the DeferList. */
		synchronized(this.getDeferList()) { 
			/* Determine whether we've elapsed. */
			return (pCurrentTimeSeconds > (this.getStartTimeSeconds() + this.getDurationSeconds())) && (this.getDeferList().isEmpty());
		}
	}
	
	public final float getStartTimeSeconds() {
		return this.mStartTimeSeconds;
	}
	
	public final float getDurationSeconds() {
		return this.mDurationSeconds;
	}
	
	public final List<Object> getDeferList() {
		return this.mDeferList;
	}
	
}