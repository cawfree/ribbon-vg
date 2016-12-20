package uk.ac.manchester.sisp.ribbon.ui.easing;

import java.util.Arrays;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.ui.easing.global.EEasingAlgorithm;

public abstract class EasingPacket implements IDisposable {
	
	/* Member Variables. */
	private float[] mEasingData;
	private float[] mResultsBuffer;
	
	public EasingPacket(final float[] pEasingData) {
		/* Initialize Member Variables. */
		this.mEasingData    = pEasingData;
		this.mResultsBuffer = Arrays.copyOf(pEasingData, pEasingData.length >> 1);
	}
	
	public final void onUpdateBounds(final IEasingConfiguration pEasingConfiguration, final float pObjectTimeSeconds, final float pCurrentTimeSeconds, final float ... pTerminals) {
		/* Calculate the SplitIndex. */
		final int lSplitIndex = this.onCalculateSplitIndex();
		/* Iterate across the data. */
		for(int i = 0; i < pTerminals.length; i++) {
			/* Update the progression for this index. */
			this.getEasingData()[i]               = EEasingAlgorithm.onCalculateBoundedEasing(pEasingConfiguration, pObjectTimeSeconds, pCurrentTimeSeconds, this.getEasingData()[i], this.getEasingData()[i + lSplitIndex]);
			/* Update the corresponding Terminal. */
			this.getEasingData()[i + lSplitIndex] = pTerminals[i];
		}
	}
	
	public final float[] getEasingData() {
		return this.mEasingData;
	}
	
	protected final float[] getResultsBuffer() {
		return this.mResultsBuffer;
	}

	@Override
	public void dispose() {
		/* Nullify our EasingData. */
		this.mEasingData = null;
	}
	
	public final int onCalculateSplitIndex() {
		return this.getEasingData().length >> 1;
	}
	
}