package uk.ac.manchester.sisp.ribbon.common;

import uk.ac.manchester.sisp.ribbon.ui.global.UIGlobal;

public interface IScale {
	
	public static class Impl implements IScale.W {
		
		/* Member Variables. */
		private float mScale;

		public Impl() {
			this(UIGlobal.UI_UNITY);
		}
		
		public Impl(final IScale pScale) {
			/* Initialize Member Variables. */
			this.mScale = pScale.getScale();
		}
		
		public Impl(final float pScale) {
			/* Initialize Member Variables. */
			this.mScale = pScale;
		}

		@Override public void  setScale(final float pScale) { this.mScale = pScale; }
		@Override public float getScale()                   { return this.mScale;   }
		
	}
	
	public static interface W extends IScale {
		public abstract void setScale(final float pScale);
	}
	
	public abstract float getScale();

}
