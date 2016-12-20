package uk.ac.manchester.sisp.ribbon.font.global;

import uk.ac.manchester.sisp.ribbon.font.GlyphData;
import uk.ac.manchester.sisp.ribbon.font.IFont;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;


public final class FontGlobal {
	
	/** TODO: Support multiline configurations. return in vec2 **/
	public static final float onCalculateLineWidth(final IFont pFont, final float pFontScale, final String pString) {
		/* Declare the width variable. */
		float lTextWidth = 0.0f;
		/* Iterate through each character in the String. */
		for(int i = 0; i < pString.length(); i++) {
			/* Fetch the current GlyphData. */
			final GlyphData lGlyphData = pFont.onFetchDetails(DataUtils.getCachedChar(pString.charAt(i)));
			/* Increment the TextWidth. */
			lTextWidth                += lGlyphData.onCalculateWidth(pFontScale);
		}
		/* Return the TextWidth. */
		return lTextWidth;
	}
	
	public static final float onCalculateLineHeight(final IFont pFont, final float pFontScale, final String pString) {
		/* Declare the height variable. */
		float lMaxHeight = 0.0f;
		/* Iterate through each character in the String. */
		for(int i = 0; i < pString.length(); i++) {
			/* Fetch the current GlyphData. */
			final GlyphData lGlyphData = pFont.onFetchDetails(DataUtils.getCachedChar(pString.charAt(i)));
			/* Determine if the Curent Glyph exceeds the MaximumHeight. */
			lMaxHeight = Math.max(lMaxHeight, lGlyphData.onCalculateHeight(pFontScale));
		}
		/* Return the MaxHeight. */
		return lMaxHeight;
	}
	
	/* Prevent instantiation of this class. */
	private FontGlobal() {}
	
}
