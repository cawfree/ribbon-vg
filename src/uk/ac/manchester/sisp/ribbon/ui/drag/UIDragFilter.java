package uk.ac.manchester.sisp.ribbon.ui.drag;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.event.filter.IEventsFilter;
import uk.ac.manchester.sisp.ribbon.ui.pointer.EPointerAction;
import uk.ac.manchester.sisp.ribbon.ui.pointer.UIPointerEvent;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

public abstract class UIDragFilter<T extends IVec2.W> implements IEventsFilter<UIPointerEvent, UIDragDispatcher<T>> {
	
	private static final boolean isValidDispatch(final float pDX, final float pDY) {
		return pDX != 0.0f || pDY != 0.0f;
	}
	
	private static final boolean isFiniteSteps(final UIDragFilter<?> pUIDragFilter) {
		return pUIDragFilter.getDragParameters().getSteps() > 0;
	}
	
	/* Member Variables. */
	private final DragParameters mDragParameters;
	private       float          mLastX;
	private       float          mLastY;
	private       float          mAccumulatedX;
	private       float          mAccumulatedY;
	
	public UIDragFilter(final UIPointerEvent pUIPointerEvent) {
		/* Initialize Member Variables. */
		this.mDragParameters = DragParameters.DEFAULT_DRAG_UNBOUNDED;
		this.mLastX          = pUIPointerEvent.getX();
		this.mLastY          = pUIPointerEvent.getY();
		this.mAccumulatedX   = 0.0f;
		this.mAccumulatedY   = 0.0f;
	}
	
	public UIDragFilter(final UIPointerEvent pUIPointerEvent, final DragParameters pDragParameters) {
		/* Initialize Member Variables. */
		this.mDragParameters = pDragParameters;
		this.mLastX          = pUIPointerEvent.getX();
		this.mLastY          = pUIPointerEvent.getY();
		this.mAccumulatedX   = 0.0f;
		this.mAccumulatedY   = 0.0f;
	}

	@Override
	public final boolean onHandleEvent(final UIPointerEvent pUIPointerEvent, final UIDragDispatcher<T> pUIDragDispatcher) {
		/* Allocate floats for calculating the DeltaX and DeltaY. */
		float lDeltaX;
		float lDeltaY;
		/* If the mouse is being released, ensure the component will lie within the specified bounds if we're using finite steps. */
		if(pUIPointerEvent.getPointerAction() == EPointerAction.POINTER_RELEASE && UIDragFilter.isFiniteSteps(this)) {
			/* Calculate the UIDragFilter's XRange and YRange. */
			final float lXRange   =     this.getDragParameters().getMaximumX() - this.getDragParameters().getMinimumX();
			final float lYRange   =     this.getDragParameters().getMaximumY() - this.getDragParameters().getMinimumY();
			/* Determine the UIDragFilter's current XOffset and YOffset along this range. */
			final float lXOffset  = this.getAccumulatedX() - this.getDragParameters().getMinimumX();
			final float lYOffset  = this.getAccumulatedY() - this.getDragParameters().getMinimumY();
			/* Calculate the XStep and YStep. */
			final float lXStep    = (lXRange / (float)this.getDragParameters().getSteps()) + MathUtils.TOLERANCE_DIV_ZERO;
			final float lYStep    = (lYRange / (float)this.getDragParameters().getSteps()) + MathUtils.TOLERANCE_DIV_ZERO;
			/* Calculate the NearestX and NearestY along the same plane as the UIDragFilter. */
			final float lNearestX = (((float)Math.floor(((lXOffset + 0.5f * lXStep) / lXStep))) * lXStep) + this.getDragParameters().getMinimumX();
			final float lNearestY = (((float)Math.floor(((lYOffset + 0.5f * lYStep) / lYStep))) * lYStep) + this.getDragParameters().getMinimumY();
			/* Assign the DragDispatcher's DeltaX and DeltaY to lie at the precise step; this is the difference between the Accumulated and the Nearest values. */
			lDeltaX = lNearestX - this.getAccumulatedX();
			lDeltaY = lNearestY - this.getAccumulatedY();
		}
		else {
			/* Assign the DeltaX and DeltaY value as the difference between the current UIPointerEvent position and the last UIPointerEvent position. */
			lDeltaX = pUIPointerEvent.getX() - this.getLastX();
			lDeltaY = pUIPointerEvent.getY() - this.getLastY();
		}
		/* Calculate the XDifference and YDifference. */
		final float lXDifference = this.getAccumulatedX() + lDeltaX;
		final float lYDifference = this.getAccumulatedY() + lDeltaY;
		/* Modulate the DeltaX and DeltaY to ensure we lie precisely within the bounds, and enforce maxima. */
		lDeltaX = (lXDifference >= this.getDragParameters().getMaximumX() ? this.getDragParameters().getMaximumX() - this.getAccumulatedX() : lXDifference <= this.getDragParameters().getMinimumX() ? this.getDragParameters().getMinimumX() - this.getAccumulatedX() : lDeltaX);
		lDeltaY = (lYDifference >= this.getDragParameters().getMaximumY() ? this.getDragParameters().getMaximumY() - this.getAccumulatedY() : lYDifference <= this.getDragParameters().getMinimumY() ? this.getDragParameters().getMinimumY() - this.getAccumulatedY() : lDeltaY);
		/* Determine whether to accumulate the events for future iterations. */
		if(lDeltaX != 0.0f) { this.setAccumulatedX(this.getAccumulatedX() + lDeltaX); this.setLastX(pUIPointerEvent.getX()); }
		if(lDeltaY != 0.0f) { this.setAccumulatedY(this.getAccumulatedY() + lDeltaY); this.setLastY(pUIPointerEvent.getY()); }
		/* Finally, determine whether to distribute the event. */
		if(UIDragFilter.isValidDispatch(lDeltaX, lDeltaY)) {
			/* Delegate the event. */
			this.onDragEvent(pUIDragDispatcher, pUIPointerEvent, lDeltaX, lDeltaY);
		}
		/* Inform the UIDragDispatcher that we wish to listen to events whilst still dragging. */
		return pUIDragDispatcher.isDragging();
	}
	
	protected final DragParameters getDragParameters() {
		return this.mDragParameters;
	}
	
	protected final void setLastX(final float pLastX) {
		this.mLastX = pLastX;
	}
	
	public final float getLastX() {
		return this.mLastX;
	}
	
	protected final void setLastY(final float pLastY) {
		this.mLastY = pLastY;
	}
	
	public final float getLastY() {
		return this.mLastY;
	}
	
	protected final void setAccumulatedX(final float pAccumulatedX) {
		this.mAccumulatedX = pAccumulatedX;
	}
	
	public final float getAccumulatedX() {
		return this.mAccumulatedX;
	}
	
	protected final void setAccumulatedY(final float pAccumulatedY) {
		this.mAccumulatedY = pAccumulatedY;
	}
	
	public final float getAccumulatedY() {
		return this.mAccumulatedY;
	}
	
	public abstract void onDragEvent(final UIDragDispatcher<T> pUIDragDispatcher, final UIPointerEvent pUIPointerEvent, final float pDX, final float pDY);

}
