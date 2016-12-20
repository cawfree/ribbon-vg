package uk.ac.manchester.sisp.ribbon.ui.pointer;

import java.util.Arrays;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.event.filter.EventDispatcher;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

/** TODO: We now generate safe pointers for each filter. Important to guarantee this for every dispatcher. Can this be modularised? **/
/** TODO: Is this inefficient? **/
public final class UIPointerDispatcher extends EventDispatcher<UIPointerEvent, UIPointerDispatcher> {
	
	/* Static Declarations. */
	private static final int BUFFER_POINTER_SIZE = 2;
	
	/* Member Variables. */
	private final UIPointerEvent[] mPointerBuffer;
	private       int              mRecentIndex;
	private final IVec2.I.W        mDelta;
	
	public UIPointerDispatcher(final float pCurrentTimeSeconds, final boolean pIsEnabled) {
		super(pIsEnabled);
		/* Initialize Member Variables. */
		this.mPointerBuffer = new UIPointerEvent[UIPointerDispatcher.BUFFER_POINTER_SIZE];
		this.mRecentIndex   = 0;
		this.mDelta         = new IVec2.I.Impl();
		/* Allocate a default placeholder element for the PointerBuffer. */
		Arrays.fill(this.getPointerBuffer(), new UIPointerEvent(pCurrentTimeSeconds, 0, 0, 0, EPointerAction.POINTER_RELEASE, EPointerIndex.LEFT, false, false));
	}

	@Override protected final UIPointerEvent onGenerateImmutableEvent(final UIPointerEvent pUIPointerEvent) {
		return new UIPointerEvent(pUIPointerEvent);
	}
	
	/* Allocates, controls and distributes immutable UIPointerEvents. */
	public final void onUpdatePointer(final float pCurrentTimeSeconds, final int pX, final int pY, final int pClickCount, EPointerAction pPointerAction, final EPointerIndex pPointerIndex, final boolean pIsShiftDown, final boolean pIsControlDown) {
		/* Allocate the DragCount. */
		int lDragCount = 0;
		/* Iterate across the PointerBuffer. */
		for(final UIPointerEvent lUIPointerEvent : this.getPointerBuffer()) {
			/* If the UIPointerEvent is of type POINTER_DRAGGED, then increase the DragCount. */
			lDragCount += DataUtils.booleanToInt(lUIPointerEvent.getPointerAction() == EPointerAction.POINTER_DRAGGED);
		}
		/* Define that the user is dragging if the entire buffer is filled with POINTER_DRAGGED UIPointerEvents. */
		final boolean lIsDragging = (lDragCount == this.getPointerBuffer().length);
//		/* Perform some initial sanity checks for the input data. */
//		switch(pPointerAction) {
//			case POINTER_RELEASE : 
//				/* Determine whether the pointer has not been dragged during this period. */
//				if(!lIsDragging) {
//					/* Use the ClickCount to refine the type of PointerAction. */
//					pPointerAction = EPointerAction.POINTER_CLICK;
//				}
//			break;
//			default              : /* Refuse to handle other types of EPointerAction. */ break;
//		}
		/* Encapsulate the parameter data as a sanitised UIPointerEvent. */
		final UIPointerEvent lUIPointerEvent = new UIPointerEvent(pCurrentTimeSeconds, pX, pY, pClickCount, pPointerAction, pPointerIndex, pIsShiftDown, pIsControlDown);
		/* Next, push the UIPointerEvent this into the PointerBuffer. */
		DataUtils.onPushArrayElement(this.getPointerBuffer(), lUIPointerEvent);
		/* Update the Delta if the PointerEvent indicates the mouse has moved. */
		if(lUIPointerEvent.getPointerAction().equals(EPointerAction.POINTER_DRAGGED) || lUIPointerEvent.getPointerAction().equals(EPointerAction.POINTER_MOVED)) {
			/* Update the Delta as the difference between the pointer locations. */
			this.getDelta().setX(this.getPointerBuffer()[0].getX() - this.getPointerBuffer()[1].getX());
			this.getDelta().setY(this.getPointerBuffer()[0].getY() - this.getPointerBuffer()[1].getY());
		}
		/* Determine whether to dispatch any starved POINTER_DRAGGED events. */
		if(!lIsDragging && pPointerAction == EPointerAction.POINTER_DRAGGED && (lDragCount == this.getPointerBuffer().length - 1)) {
			/* Reverse iterate through the old DragEvents. */
			for(int i = this.getPointerBuffer().length - 1; i > 0; i--) {
				/* Assign the RecentIndex. (Enables access-correct external lookup for the PointerEvent reference.) */
				this.mRecentIndex = i;
				/* Dispatch the withheld drag events. */
				this.onRibbonEvent(this.getRecentPointer());
			}
		}
		else {
			/* Reset the RecentIndex. */
			this.mRecentIndex = 0;
		}
		/* Determine whether to dispatch the event current UIPointerEvent. */
		if(!(pPointerAction == EPointerAction.POINTER_DRAGGED && !lIsDragging)) {
			/* Force the UIPointerDispatcher to handle the event. (Starve of POINTER_DRAGGED events whilst we haven't satisfied dragging.) */
			this.onRibbonEvent(this.getRecentPointer());
		}
	}
	
	/** TODO: use trigonomertry **/
	/* Accumulates the trend in pointer co-ordinates in the supplied callback. */
	public final IVec2.F.W onCalculateTrend(final IVec2.F.W pCallback) {
		/* Iterate through the buffer until we find the beginning of the current operation, or hit the end of the buffer. */
		for(int i = 1; i < this.getPointerBuffer().length && this.getPointerBuffer()[i].getPointerAction() != EPointerAction.POINTER_PRESSED; i++) { // between i and i - 1
			/* Calculate the difference in X and difference in Y. */
			final float lDX = this.getPointerBuffer()[i].getX() - this.getPointerBuffer()[i - 1].getX();
			final float lDY = this.getPointerBuffer()[i].getY() - this.getPointerBuffer()[i - 1].getY();
			/* Accumulate the delta variables. */
			pCallback.setX(pCallback.getX() + lDX);
			pCallback.setY(pCallback.getY() + lDY);
		}
		/* Return the callback. */
		return pCallback;
	}
	
	/* Returns a safe copy of the runtime's most 'recent' UIPointerEvent. */
	public final UIPointerEvent getPointerEvent() {
		return this.onGenerateImmutableEvent(this.getRecentPointer());
	}
	
	private final UIPointerEvent getRecentPointer() {
		return this.getPointerBuffer()[this.getRecentIndex()];
	}

	private final UIPointerEvent[] getPointerBuffer() {
		return this.mPointerBuffer;
	}
	
	private final int getRecentIndex() {
		return this.mRecentIndex;
	}

	private final IVec2.I.W getDelta() {
		return this.mDelta;
	}
	
	/* Returns a safe public reference to the Delta. */
	public final IVec2.I onFetchDelta() {
		return this.getDelta();
	}
	
}