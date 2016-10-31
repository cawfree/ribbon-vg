package uk.ac.manchester.sisp.ribbon.ui.drag;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.filter.EventDispatcher;
import uk.ac.manchester.sisp.ribbon.ui.easing.EEasingAlgorithm;
import uk.ac.manchester.sisp.ribbon.ui.easing.UIReactiveEasing;
import uk.ac.manchester.sisp.ribbon.ui.pointer.EPointerAction;
import uk.ac.manchester.sisp.ribbon.ui.pointer.UIPointerEvent;
import uk.ac.manchester.sisp.ribbon.ui.time.UITimeDispatcher;

public class UIDragDispatcher <T extends IVec2.W> extends EventDispatcher<UIPointerEvent, UIDragDispatcher<T>> {
	
	/* Creates a UIDragFilter which can be used for updating the 2D co-ordinates of a UIReactiveEasing. */
	private final UIDragFilter<T> onEasingCartesian(final UIPointerEvent pUIPointerEvent, final DragParameters pDragParameters, final UIReactiveEasing pUIReactiveEasing) {
		/* Allocate a new UIDragFilter. */
		final UIDragFilter<T> lUIDragFilter = new UIDragFilter<T>(pUIPointerEvent, pDragParameters) { 
		/* Handle the drag update. */
		@Override public final void onDragEvent(final UIDragDispatcher<T> pUIDragDispatcher, final UIPointerEvent pUIPointerEvent, final float pDX, final float pDY) {
			/* Overwrite the Terminals to account for relative positioning. */ 
			pUIReactiveEasing.getTerminals()[UIReactiveEasing.INDEX_PARAMETER_X] += pDX;
			pUIReactiveEasing.getTerminals()[UIReactiveEasing.INDEX_PARAMETER_Y] += pDY;
			/* Update the bounds of the ongoing UIReactiveEasing. We update the final point, not whatever is currently interpolated because we won't have reached it yet! */
			pUIReactiveEasing.onUpdateBounds(pUIPointerEvent.getEventTimeSeconds(), pUIReactiveEasing.getTerminals());
		}
		/* Ignore disposals. */
		@Override public void dispose() { } 
		};
		/* Return the UIDragFilter. */
		return lUIDragFilter;
	}
	
	/* Member Variables. */
	private T mDragComponent;
	
	public UIDragDispatcher(final boolean pIsEnabled) {
		super(pIsEnabled);
		/* Initialize Member Variables. */
		this.mDragComponent = null;
	}
	
	@Override
	public final void onRibbonEvent(final UIPointerEvent pUIPointerEvent) {
		/* Filter the UIPointerEvent based on the PointerAction. */
		switch(pUIPointerEvent.getPointerAction()) {
			case POINTER_DRAGGED : 
			case POINTER_RELEASE : 
				/* Deploy the event to the registered listeners. */
				super.onRibbonEvent(pUIPointerEvent);
			break;
			default              : /* Refuse to handle other cases. */ break;
		}
		/* Check whether the pointer has been released. */
		if(pUIPointerEvent.getPointerAction() == EPointerAction.POINTER_RELEASE) {
			/* This signifies the end of a drag operation. All event filters must be cleared. */
			this.getEventFilters().clear();
		}
	}
	
	/* Co-ordinates a smooth drag task. */
	public final UIDragFilter<T> onLaunchEasingDrag(final DragParameters pDragParameters, final UIPointerEvent pUIPointerEvent, final T pT, final EEasingAlgorithm pEasingAlgorithm, final float pEasingDuration, final UITimeDispatcher pUITimeDispatcher) {
		/* Initialize a UIReactiveEasing for modulating the Decoupler width. */
		final UIReactiveEasing lUIReactiveEasing = new UIReactiveEasing(EEasingAlgorithm.QUARTIC_EASE_OUT, pUIPointerEvent.getEventTimeSeconds(), pEasingDuration, pT) {
			/* Handle the EasingResults. */
			@Override public final boolean onHandleEvent(final IEvent pEvent, final UITimeDispatcher pEventsDispatcher) {
				/* Update the EasingAlgorithm. */
				if(super.onHandleEvent(pEvent, pEventsDispatcher) && isDragging()) {
					/* Reposition the IVec2. Use pixel-constrained co-ordinates. */
					pT.setX((int) this.getResultsBuffer()[UIReactiveEasing.INDEX_PARAMETER_X]);
					pT.setY((int) this.getResultsBuffer()[UIReactiveEasing.INDEX_PARAMETER_Y]);
				}
				/* Return the lifetime parameter. */
				return isDragging();
			}
		};
		/* Assign the DragComponent. */
		this.mDragComponent = pT;
		/* Export the UIReactiveEasing. */
		pUITimeDispatcher.getEventFilters().add(lUIReactiveEasing);
		/* Create the UIDragFilter. */
		final UIDragFilter<T> lUIDragFilter = this.onEasingCartesian(pUIPointerEvent, pDragParameters, lUIReactiveEasing);
		/* Create and export a drag-driven UIDragFilter. */
		this.getEventFilters().add(lUIDragFilter);
		/* Return the UIReactiveEasing. */
		return lUIDragFilter;
	}

	/* Define that whenever any DragFilters are registered with this dispatcher, we must be listening for drag operations. */
	public final boolean isDragging() {
		return !this.getEventFilters().isEmpty();
	}
	
	public final T getDragComponent() {
		return this.mDragComponent;
	}

	@Override
	protected final UIPointerEvent onGenerateImmutableEvent(final UIPointerEvent pUIPointerEvent) {
		return new UIPointerEvent(pUIPointerEvent);
	}

}
