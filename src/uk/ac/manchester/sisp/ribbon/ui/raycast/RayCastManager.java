package uk.ac.manchester.sisp.ribbon.ui.raycast;

import java.util.Collections;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.ui.pointer.UIPointerEvent;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

/** TODO: Abstract dependence upon the orthographic perspective. **/
public class RayCastManager <U extends IVec2 & IDim2> {
	
	public static final <U extends IVec2> U onFetchParent(final List<U> pHierarchy, final U pChild) {
		/* Fetch the index of pU. */
		final int lIndex = pHierarchy.indexOf(pChild) - 1;
		/* Return the ParentCollision of pU, whilst ensuring a valid index has been specified. */
		return DataUtils.isNotNull(lIndex) ? pHierarchy.get(lIndex) : null;
	}
	
	public static final <U extends IVec2> U onFetchChild(final List<U> pHierarchy, final U pParent) {
		/* Fetch the index of pU. */
		final int lIndex = pHierarchy.indexOf(pParent) + 1;
		/* Return the ChildCollision of pU, ensuring a valid index is specified. */
		return (lIndex < pHierarchy.size()) ? pHierarchy.get(lIndex) : null;
	}
	
	/* Fetches the element corresponding to the most recent collision within the CollidingElements List. */
	public static final <U extends IVec2 & IDim2> U onFetchRecentCollision(final List<U> pCollidingElements) {
		return DataUtils.getLastElementOf(pCollidingElements);
	}
	
	/* Buffer Lengths. */
	private static final int BUFFER_COLLISION_LENGTH   = 2;
	
	/* Buffer Indices. */
	private static final int BUFFER_INDEX_COLLISION_X  = 0;
	private static final int BUFFER_INDEX_COLLISION_Y  = 1;
	
	/* Member Variables. */
	private final float[] mCollisionBuffer;
	private final List<U> mCollisionResults;
	
	public RayCastManager(final List<U> pCollisionResults) {
		/* Initialize Member Variables. */
		this.mCollisionBuffer  = new float[RayCastManager.BUFFER_COLLISION_LENGTH];
		this.mCollisionResults = pCollisionResults;
	}
	
	/* A bounding box collision detection algorithm for collision elements. Compensates for an applied global scale and additional padding. */
	public final boolean isIntersectingBounds(final U pU) {
		/* Calculate the lower bounds of the UIElement. */
		final float lX0 = (pU.getX());
		final float lY0 = (pU.getY());
		/* Calculate the upper bounds. */
		final float lX1 = lX0 + ((pU.getWidth()  + (2.0f)));
		final float lY1 = lY0 + ((pU.getHeight() + (2.0f)));
		/* Use these bounds within the conventional intersection test. */
		return MathUtils.isIntersecting(this.getCollisionX(), this.getCollisionY(), lX0, lY0, lX1, lY1);
	}
	
	/* Precise scaling metrics at arbitrary positions along the collision hierarchy. */
	public final void onRayHierarchyTransform(final UIPointerEvent pUIPointerEvent) {
		/* Declare the Initial Coefficients. */
		this.setCollisionX(pUIPointerEvent.getX());
		this.setCollisionY(pUIPointerEvent.getY());
		/* Reverse iterate along the CollisionResults. (Element indices impose nested impact.) */
		for(int i = 0; i < this.getCollisionResults().size(); i++) {
			/* Fetch the CollisionResult. */
			final U lU = this.getCollisionResults().get(i);
			/* Offset the X and Y co-ordinates given the global parent scale. */
			this.setCollisionX(this.getCollisionX() - lU.getX());
			this.setCollisionY(this.getCollisionY() - lU.getY());
		}
	}
	
	public final boolean isRayIntersection() {
		return !this.getCollisionResults().isEmpty();
	}
	
	public final boolean isRecentCollision(final U pU) {
		return pU.equals(this.getRecentCollision());
	}
	
	public final U getRecentCollision() {
		return RayCastManager.onFetchRecentCollision(this.getCollisionResults());
	}
	
	public final U getParentCollision(final U pU) {
		/* Return the ParentCollision. */
		return RayCastManager.onFetchParent(this.getCollisionResults(), pU);
	}
	
	public final U getChildCollision(final U pU) {
		/* Return the ChildCollision. */
		return RayCastManager.onFetchChild(this.getCollisionResults(), pU);
	}
	
	private final float[] getCollisionBuffer() {
		return this.mCollisionBuffer;
	}
	
	protected final List<U> getCollisionResults() {
		return this.mCollisionResults;
	}
	
	public final List<U> onFetchCollisionResults() {
		return Collections.unmodifiableList(this.getCollisionResults());
	}
	
	private final void setCollisionX(final float pX) {
		this.getCollisionBuffer()[RayCastManager.BUFFER_INDEX_COLLISION_X] = pX;
	}
	
	public final float getCollisionX() {
		return this.getCollisionBuffer()[RayCastManager.BUFFER_INDEX_COLLISION_X];
	}
	
	private final void setCollisionY(final float pY) {
		this.getCollisionBuffer()[RayCastManager.BUFFER_INDEX_COLLISION_Y] = pY;
	}
	
	public final float getCollisionY() {
		return this.getCollisionBuffer()[RayCastManager.BUFFER_INDEX_COLLISION_Y];
	}
	
}