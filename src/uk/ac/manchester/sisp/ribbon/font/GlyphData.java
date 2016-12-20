package uk.ac.manchester.sisp.ribbon.font;

public class GlyphData {
	
	/* Member Variables. */
	private final int mGlyphLocation;
	private final int mNumberOfContours;
	private final int mMinimumX;
	private final int mMinimumY;
	private final int mMaximumX;
	private final int mMaximumY;
	
	public GlyphData(final int pGlyphLocation, final int pNumberOfContours, final int pMinimumX, final int pMinimumY, final int pMaximumX, final int pMaximumY) {
		/* Initialize Member Variables. */
		this.mGlyphLocation    = pGlyphLocation;
		this.mNumberOfContours = pNumberOfContours;
		this.mMinimumX         = pMinimumX;
		this.mMinimumY         = pMinimumY;
		this.mMaximumX         = pMaximumX;
		this.mMaximumY         = pMaximumY;
	}

	/* Calculates the glyph width in pixels for a particular FontScale. */
	public final float onCalculateWidth(final float pFontScale) {
		return (float)(pFontScale * ((float)((this.getMaximumX() + this.getMinimumX())))); 
		//return (float)Math.ceil(pFontScale * ((float)(Math.ceil(this.getMaximumX() + this.getMinimumX())))); 
	}

	/* Calculates the glyph height in pixels for a particular FontScale. */
	public final float onCalculateHeight(final float pFontScale) {
		return (float)(pFontScale * ((float)((this.getMaximumY() + this.getMinimumY())))); /** TODO: AdvanceY **/
		//return (float)Math.ceil(pFontScale * ((float)(Math.ceil(this.getMaximumY() + this.getMinimumY()))));
	}
	
	public final int getGlyphLocation() {
		return this.mGlyphLocation;
	}
	
	public final int getNumberOfContours() {
		return this.mNumberOfContours;
	}
	
	public final int getMinimumX() {
		return this.mMinimumX;
	}
	
	public final int getMinimumY() {
		return this.mMinimumY;
	}
	
	public final int getMaximumX() {
		return this.mMaximumX;
	}
	
	public final int getMaximumY() {
		return this.mMaximumY;
	}
	
}