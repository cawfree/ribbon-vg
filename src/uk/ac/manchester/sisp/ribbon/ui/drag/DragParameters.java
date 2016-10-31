package uk.ac.manchester.sisp.ribbon.ui.drag;

import uk.ac.manchester.sisp.ribbon.common.IBounds2;

public class DragParameters implements IBounds2 {
	
	/* Static Declarations. */
	public static final DragParameters DEFAULT_DRAG_UNBOUNDED  = new DragParameters();
	public static final DragParameters DEFAULT_DRAG_HORIZONTAL = new DragParameters(Float.NEGATIVE_INFINITY, 0.0f, Float.POSITIVE_INFINITY, 0.0f, 0);
	public static final DragParameters DEFAULT_DRAG_VERITCAL   = new DragParameters(0.0f, Float.NEGATIVE_INFINITY, 0.0f, Float.POSITIVE_INFINITY, 0);
	
	/* Parameter Indices. */
	private static final int DRAG_PARAMETER_INDEX_MIN_DX = 0;
	private static final int DRAG_PARAMETER_INDEX_MIN_DY = 1;
	private static final int DRAG_PARAMETER_INDEX_MAX_DX = 2;
	private static final int DRAG_PARAMETER_INDEX_MAX_DY = 3;
	
	/* Member Variables. */
	private final float[] mDragParameters;
	private final int     mSteps;
	
	public DragParameters() {
		this(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0);
	}
	
	/** TODO: Rename... **/
	public DragParameters(final float pMinimumDX, final float pMinimumDY, final float pMaximumDX, final float pMaximumDY, final int pSteps) {
		/* Initialize Member Variables. */
		this.mDragParameters = new float[]{ pMinimumDX, pMinimumDY, pMaximumDX, pMaximumDY };
		this.mSteps          = pSteps;
	}
	
	@Override public final float getMinimumX() {
		return this.getDragParameters()[DragParameters.DRAG_PARAMETER_INDEX_MIN_DX];
	}

	@Override public final float getMinimumY() {
		return this.getDragParameters()[DragParameters.DRAG_PARAMETER_INDEX_MIN_DY];
	}

	@Override public final float getMaximumX() {
		return this.getDragParameters()[DragParameters.DRAG_PARAMETER_INDEX_MAX_DX];
	}

	@Override public final float getMaximumY() {
		return this.getDragParameters()[DragParameters.DRAG_PARAMETER_INDEX_MAX_DY];
	}
	
	public final int getSteps() {
		return this.mSteps;
	}
	
	private final float[] getDragParameters() {
		return this.mDragParameters;
	}

}
