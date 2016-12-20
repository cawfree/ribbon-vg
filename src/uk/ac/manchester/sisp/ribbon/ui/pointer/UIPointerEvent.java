package uk.ac.manchester.sisp.ribbon.ui.pointer;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.event.IEvent;

public final class UIPointerEvent implements IEvent, IVec2.I.W { 
	
	/* Define the default Serialization UID. */
	private static final long serialVersionUID = 1L;
	
	public static final void onTransformPointer(final UIPointerEvent pUIPointerEvent, final float pXOffset, final float pYOffset, final float pScale) {
		/* Scale the internal PointerData to compensate for the transform variables. */
		pUIPointerEvent.setX(Math.round((pUIPointerEvent.getX() + pXOffset) / pScale));
		pUIPointerEvent.setY(Math.round((pUIPointerEvent.getY() + pYOffset) / pScale));
	}
	
	public static final boolean isDoubleClick(final UIPointerEvent pUIPointerEvent) {
		return (pUIPointerEvent.getClickCount() == 2);
	}
	
	/* Member Variables. */
	private final float          mEventTimeSeconds;
	private       int            mX;
	private       int            mY;
	private final int            mClickCount;
	private final EPointerAction mPointerAction;
	private final EPointerIndex  mPointerIndex;
	private final boolean        mShiftDown;
	private final boolean        mControlDown;
	
	/* Creates a copy of the parameterised UIPointerEvent, but updates the time. */
	public UIPointerEvent(final UIPointerEvent pUIPointerEvent, final float pCurrentTimeSeconds) {
		this(pCurrentTimeSeconds, pUIPointerEvent.getX(), pUIPointerEvent.getY(), pUIPointerEvent.getClickCount(), pUIPointerEvent.getPointerAction(), pUIPointerEvent.getPointerIndex(), pUIPointerEvent.isShiftDown(), pUIPointerEvent.isControlDown());
	}
	
	/* Creates a copy of the parameterised UIPointerEvent. */
	public UIPointerEvent(final UIPointerEvent pUIPointerEvent) {
		this(pUIPointerEvent.getObjectTimeSeconds(), pUIPointerEvent.getX(), pUIPointerEvent.getY(), pUIPointerEvent.getClickCount(), pUIPointerEvent.getPointerAction(), pUIPointerEvent.getPointerIndex(), pUIPointerEvent.isShiftDown(), pUIPointerEvent.isControlDown());
	}
	
	/* Creates a copy of the parameterised UIPointerEvent but enumerates a different PointerAction. */
	public UIPointerEvent(final UIPointerEvent pUIPointerEvent, final EPointerAction pPointerAction) {
		this(pUIPointerEvent.getObjectTimeSeconds(), pUIPointerEvent.getX(), pUIPointerEvent.getY(), pUIPointerEvent.getClickCount(), pPointerAction, pUIPointerEvent.getPointerIndex(), pUIPointerEvent.isShiftDown(), pUIPointerEvent.isControlDown());
	}
	
	protected UIPointerEvent(final float pEventTimeSeconds, final int pX, final int pY, final int pClickCount, final EPointerAction pPointerAction, final EPointerIndex pPointerIndex, final boolean pIsShiftDown, final boolean pIsControlDown) {
		/* Initialize Member Variables. */
		this.mEventTimeSeconds = pEventTimeSeconds;
		this.mX                = pX;
		this.mY                = pY;
		this.mClickCount       = pClickCount;
		this.mPointerAction    = pPointerAction;
		this.mPointerIndex     = pPointerIndex;
		this.mShiftDown        = pIsShiftDown;
		this.mControlDown      = pIsControlDown;
	}

	@Override public final float getObjectTimeSeconds() {
		return this.mEventTimeSeconds;
	}

	@Override public void dispose() { }
	
	@Override
	public final void setX(final int pX) {
		this.mX = pX;
	}
	
	@Override public final int getX() {
		return this.mX;
	}
	
	@Override
	public final void setY(final int pY) {
		this.mY = pY;
	}
	
	@Override public final int getY() {
		return this.mY;
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
	
	public final boolean isControlDown() {
		return this.mControlDown;
	}

}
