package uk.ac.manchester.sisp.ribbon.font;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.IGLScreenParameters;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;

public interface IFont extends IDisposable {
	
	public abstract boolean   isBold();
	public abstract boolean   isItalic();
	public abstract FontGlyph onFetchFontGlyph(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final Character pCharacter); /** TODO: MUST be FontGlyph! **/
	public abstract FontGlyph getFontGlyph(final Character pCharacter);
	public abstract int       getAscent();
	public abstract int       getDescent();
	public abstract int       getGlyphGap();
	public abstract float     getFontScale(final IGLScreenParameters pGLScreenParameters, final float pPointSize);
	
}
