package uk.ac.manchester.sisp.ribbon.ui.pointer;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.event.IEvent;

public final class UIPointerEvent implements IEvent, IVec2.W {
	
	public static final void onTransformPointer(final UIPointerEvent pUIPointerEvent, final float pXOffset, final float pYOffset, final float pScale) {
		/* Scale the internal PointerData to compensate for the transform variables. */
		pUIPointerEvent.getPointerData()[INDEX_BUFFER_POINTER_X] = (pUIPointerEvent.getX() + pXOffset) / pScale;
		pUIPointerEvent.getPointerData()[INDEX_BUFFER_POINTER_Y] = (pUIPointerEvent.getY() + pYOffset) / pScale;
	}
	
	public static final boolean isDoubleClick(final UIPointerEvent pUIPointerEvent) {
		return (pUIPointerEvent.getClickCount() == 2);
	}
	
	/** TODO: Flagged implementation. **/
	
	/* Static Declarations. */
	private static final int INDEX_BUFFER_POINTER_SECONDS = 0;
	private static final int INDEX_BUFFER_POINTER_X       = 1;
	private static final int INDEX_BUFFER_POINTER_Y       = 2;
	
	/* Member Variables. */
	private final float[]        mPointerData;
	private final int            mClickCount;
	private final EPointerAction mPointerAction;
	private final EPointerIndex  mPointerIndex;
	private final boolean        mShiftDown;
	
	/* Creates a copy of the parameterised UIPointerEvent. */
	public UIPointerEvent(final UIPointerEvent pUIPointerEvent) {
		this(pUIPointerEvent.getEventTimeSeconds(), pUIPointerEvent.getX(), pUIPointerEvent.getY(), pUIPointerEvent.getClickCount(), pUIPointerEvent.getPointerAction(), pUIPointerEvent.getPointerIndex(), pUIPointerEvent.isShiftDown());
	}
	
	protected UIPointerEvent(final float pEventTimeSeconds, final float pX, final float pY, final int pClickCount, final EPointerAction pPointerAction, final EPointerIndex pPointerIndex, final boolean pIsShiftDown) {
		/* Initialize Member Variables. */
		this.mPointerData   = new float[]{ pEventTimeSeconds, pX, pY };
		this.mClickCount    = pClickCount;
		this.mPointerAction = pPointerAction;
		this.mPointerIndex  = pPointerIndex;
		this.mShiftDown     = pIsShiftDown;
	}

	@Override
	public final float getEventTimeSeconds() {
		return this.getPointerData()[UIPointerEvent.INDEX_BUFFER_POINTER_SECONDS];
	}
	
	@Override
	public final void setX(final float pX) {
		this.getPointerData()[UIPointerEvent.INDEX_BUFFER_POINTER_X] = pX;
	}
	
	@Override
	public final float getX() {
		return this.getPointerData()[UIPointerEvent.INDEX_BUFFER_POINTER_X];
	}
	
	@Override
	public final void setY(final float pY) {
		this.getPointerData()[UIPointerEvent.INDEX_BUFFER_POINTER_Y] = pY;
	}
	
	@Override
	public final float getY() {
		return this.getPointerData()[UIPointerEvent.INDEX_BUFFER_POINTER_Y];
	}
	
	private final float[] getPointerData() {
		return this.mPointerData;
	}
	
	public final int getClickCount() {
		return this.mClickCount;
	}
	
	public final EPointerAction getPointerAction() {
		return this.mPointerAction;
	}
	
	public final EPointerIndex getPointerIndex() {
		return this.mPointerIndex;
	}
	
	public final boolean isShiftDown() {
		return this.mShiftDown;
	}

}
