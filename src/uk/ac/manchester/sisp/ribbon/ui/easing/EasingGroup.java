package uk.ac.manchester.sisp.ribbon.ui.easing;

import java.util.ArrayList;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.ui.easing.global.EEasingAlgorithm;
import uk.ac.manchester.sisp.ribbon.ui.easing.global.EasingGlobal;
import uk.ac.manchester.sisp.ribbon.ui.time.UITimeDispatcher;

/** TODO: Get around to modifying the constructors to work using just EasingConfigurations. **/
public abstract class EasingGroup <T extends EasingPacket> implements IReactiveEasing {
	
	/* Static Declarations. */
	public static final int INDEX_PARAMETER_X      = 0;
	public static final int INDEX_PARAMETER_Y      = 1;
	public static final int INDEX_PARAMETER_WIDTH  = 2;
	public static final int INDEX_PARAMETER_HEIGHT = 3;
	
	/* Member Variables. */
	private IEasingConfiguration  mEasingConfiguration;
	private float                 mObjectTimeSeconds;
	
	/* Interpolation Buffers. */
	private List<T>               mEasingPackets;
	
	/* Constructor. */
	public EasingGroup(final IEasingConfiguration pEasingConfiguration, final float pObjectTimeSeconds) {
		/* Initialize Member Variables. */
		this.mEasingConfiguration = pEasingConfiguration;
		this.mObjectTimeSeconds   = pObjectTimeSeconds;
		this.mEasingPackets       = new ArrayList<T>();
	}

	@Override
	public boolean onHandleEvent(final IEvent pEvent, final UITimeDispatcher pEventsDispatcher) {
		/* Iterate through each EasingPacket. */
		for(int i = 0; i < this.getEasingPackets().size(); i++) {
			/* Fetch the EasingPacket. */
			final T lEasingPacket = this.getEasingPackets().get(i);
			/* Update the EasingPacket. */
			this.onProgressEasing(lEasingPacket, pEvent.getObjectTimeSeconds());
		}
		/* Define whether the event was successful if we're still mid-easing. */
		return this.isAlive(pEvent.getObjectTimeSeconds());
	}
	
	/* Interpolates the EasingPacket. */
	public final void onProgressEasing(final T pEasingPacket, final float pCurrentTimeSeconds) {
		/* Fetch the ResultsBuffer and calculate the interpolation. */
		this.onProgressEasing(pEasingPacket, pCurrentTimeSeconds, pEasingPacket.getResultsBuffer());
	}
	
	/* Calculates the interpolation between the Initial and Terminal values of the Easingpacket. */
	protected void onProgressEasing(final T pEasingPacket, final float pCurrentTimeSeconds, final float[] pResultsBuffer) {
		/* Calculate the split index. */
		final int lSplitIndex = pEasingPacket.onCalculateSplitIndex();
		/* Iterate across the data. */
		for(int i = 0; i < lSplitIndex; i++) {
			/* Calculate the EasingResult for this location, between the matching Initials and Terminals. */
			final float lEasingResult  = EEasingAlgorithm.onCalculateBoundedEasing(this.getEasingConfiguration(), this.getObjectTimeSeconds(), pCurrentTimeSeconds, pEasingPacket.getEasingData()[i], pEasingPacket.getEasingData()[(i + lSplitIndex)]);
			/* Update the ResultsBuffer with the EasingResult. */
			pResultsBuffer[i] = lEasingResult;
		}
		/* Deploy the EasingResult to the concrete implementor. */
		this.onEasingResult(pEasingPacket, pCurrentTimeSeconds, pResultsBuffer);
	}
	
	public abstract void onEasingResult(final T pEasingPacket, final float pCurrentTimeSeconds, final float[] pResultsBuffer);

	@Override
	public boolean isAlive(final float pCurrentTimeSeconds) {
		/* Define whether we're still mid easing. */
		return EasingGlobal.isMidEasing(pCurrentTimeSeconds, this);
	}
	
	@Override public void dispose() {
		/* Iterate our array. */
		for(int i = 0; i < this.getEasingPackets().size(); i++) {
			/* Fetch the EasingPacket. */
			final T lEasingPacket = this.getEasingPackets().get(i);
			/* Dispose of the EasingPacket. */
			lEasingPacket.dispose();
		}
		/* Nullify the EasingPackets. */
		this.mEasingPackets = null;
	}
	
	public final void setEasingConfiguration(final IEasingConfiguration pEasingConfiguration) {
		this.mEasingConfiguration = pEasingConfiguration;
	}
	
	@Override
	public final IEasingConfiguration getEasingConfiguration() {
		return this.mEasingConfiguration;
	}
	
	@Override
	public final float getObjectTimeSeconds() {
		return this.mObjectTimeSeconds;
	}
	
	public final List<T> getEasingPackets() {
		return this.mEasingPackets;
	}

	@Override
	public final void setObjectTimeSeconds(final float pObjectTimeSeconds) {
		this.mObjectTimeSeconds = pObjectTimeSeconds;
	}
	
}