package uk.ac.manchester.sisp.ribbon.opengl.triangulation.earcut;

import uk.ac.manchester.sisp.ribbon.common.IDoublyLinked;
import uk.ac.manchester.sisp.ribbon.common.IVec2;

final class EarcutNode implements IVec2.F.W, IDoublyLinked<EarcutNode> { 
	
	/* Define the default Serialization UID. */
	private static final long serialVersionUID = 1L;
	
	/* Member Variables. */
	private float mX;
	private float mY;
	private EarcutNode mPreviousNode;
	private EarcutNode mNextNode;
	
	protected EarcutNode(final float pX, final float pY) {
		/* Initialize Member Variables. */
		this.mX             = pX;
		this.mY             = pY;
		this.mPreviousNode  = null;
		this.mNextNode      = null;
	}
	
	@Override public final void setX(final float pX) {
		this.mX = pX;
	}
	
	@Override public final float getX() {
		return this.mX;
	}
	
	@Override public final void setY(final float pY) {
		this.mY = pY;
	}
	
	@Override public final float getY() {
		return this.mY;
	}

	@Override public final void setNext(final EarcutNode pT) {
		this.mNextNode = pT;
	}

	@Override public final EarcutNode getNext() {
		return this.mNextNode;
	}

	@Override public final void setPrevious(final EarcutNode pT) {
		this.mPreviousNode = pT;
	}

	@Override public final EarcutNode getPrevious() {
		return this.mPreviousNode;
	}

}
