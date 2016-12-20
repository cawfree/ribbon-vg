package uk.ac.manchester.sisp.ribbon.opengl.text;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.font.GlyphData;
import uk.ac.manchester.sisp.ribbon.font.IFont;
import uk.ac.manchester.sisp.ribbon.font.global.FontGlobal;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.io.EEntryMode;
import uk.ac.manchester.sisp.ribbon.opengl.GLContext;
import uk.ac.manchester.sisp.ribbon.opengl.IGL;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.IGLRunnable;
import uk.ac.manchester.sisp.ribbon.opengl.buffers.GLBuffer;
import uk.ac.manchester.sisp.ribbon.opengl.buffers.GLBufferPackage;
import uk.ac.manchester.sisp.ribbon.opengl.matrix.GLMatrix;
import uk.ac.manchester.sisp.ribbon.opengl.program.constants.GLVectorProgram;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EFillRule;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

public final class GLTextRenderer implements IDisposable {
	
	/* Member Variables. */
	private final String                    mAlphabet;
	private IFont                           mFont; /** TODO: Aim to decouple from the font and just store the glyph data instead! **/
	private GLBufferPackage<GLBuffer.XY_UV> mGLBufferPackage;
	
	/** TODO: How to dispose of the delegated variables? **/
	
	public GLTextRenderer(final String pAlphabet, final IFont pFont, final GLContext pGLContext, final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext) { /** TODO: Much better performance if we instantiate dependent text at runtime. **/
		/* Initialize Member Variables. */
		this.mAlphabet = pAlphabet;
		this.mFont     = pFont;
		/* Allocate the buffer indices array. */
		final int[]     lBufferIndices  = new   int[this.getAlphabet().length()];
		final float[][] lBufferVertices = new float[this.getAlphabet().length()][];
		/* Iteratively buffer each character. */
		for(int i = 0; i < this.getAlphabet().length(); i++) {
			/* Autobox the CurrentCharacter. */
			final Character lCurrentCharacter = DataUtils.getCachedChar(this.getAlphabet().charAt(i));
			/* Fetch the current FontGlyph. */
			final VectorPath lVectorPath      = pFont.onFetchFontGlyph(pFloatStore, pVectorPathContext, lCurrentCharacter).getVectorPath();
			/* Triangulate the VectorPath. */
			pVectorPathContext.onTriangulateFill(lVectorPath, pFloatStore, EFillRule.NON_ZERO);
			/* Embed the array data into the BufferVertices. */
			lBufferVertices[i] = pFloatStore.onProduceArray();
		}
		
		/* Define the OffsetCounter. */
		int lOffsetCounter = 0;
		/* Iterate through the BufferVertices. */
		for(int i = 0; i < lBufferVertices.length; i++) {
			/* Stream the BufferVertices into a single GLBuffer. */
			pFloatStore.store(lBufferVertices[i]);
			/* Update the OffsetCounter. */
			lOffsetCounter    += lBufferVertices[i].length;
			/* Track the Offset. */
			lBufferIndices[i]  = lOffsetCounter >> 2;
			/* Clear the BufferVertices to reduce total memory overhead during initialization. */
			lBufferVertices[i] = null;
		}
		
		/* Finally, wrap the accumulated BufferVertices in a GLBuffer.*/
		final GLBuffer.XY_UV lGLBuffer = new GLBuffer.XY_UV(DataUtils.delegateNative(pFloatStore.onProduceArray()), IGL.GL_ARRAY_BUFFER, IGL.GL_STATIC_DRAW);
		/* Create the GLBufferPackage. */
		this.mGLBufferPackage          = new GLBufferPackage<GLBuffer.XY_UV>(lGLBuffer, lBufferIndices);
		/* Allocate an active OpenGL context. */
		pGLContext.invokeLater(new IGLRunnable() { @Override public final void run(final IGLES20 pGLES20, final GLContext pGLContext) {
			/* Delegate the GLBuffer. */
			pGLContext.onHandleDelegates(EEntryMode.SUPPLY, pGLES20, lGLBuffer);
		} });
		
	}
	
	/** TODO: Is there a more efficient way for buffering text dimensions? **/
	/** TODO: Buffer all characters to a single buffer and bind once. Use BufferSegment. **/
	public final float onRenderText(final IGLES20 pGLES20, final GLContext pGLContext, final GLVectorProgram pGLVectorProgram, final String pText, final float pFontScale) {
		/* Allocate a variable to track how far the caret position has moved. */
		float lCaretOffset = 0.0f;
		/* Calculate the LineHeight. (Use the whole alphabet!) */
		final float lLineHeight = FontGlobal.onCalculateLineHeight(this.getFont(), pFontScale, this.getAlphabet());
		/* Bind to the GLBuffer using the GLVectorProgram. */
		this.getGLBufferPackage().getGLBuffer().bind(pGLES20, pGLVectorProgram);
		/* Iterate through each character in the Text String. */
		for(int i = 0; i < pText.length(); i++) {
			/* Fetch the CurrentCharacter. */
			final char lCurrentCharacter = pText.charAt(i);
			/* Fetch the current GlyphData. */
			final GlyphData lGlyphData = this.getFont().onFetchDetails(DataUtils.getCachedChar(lCurrentCharacter));
			/* Process the CurrentCharacter. */
			if(lCurrentCharacter == '\r') {
				/* Translate the Model Matrix in Y by the LineHeight, and withdraw the CaretOffset. */
				GLMatrix.translateM(pGLContext.getModelMatrix(), (-1.0f * lCaretOffset), lLineHeight, 0.0f);
				/* Reset the Caret Offset. */
				lCaretOffset = 0.0f;
				/* Continue iterating. */
				continue;
			}
			else if(lCurrentCharacter != ' ') {
				/* Find the index of the character within the backing array. */
				final int  lBufferIndex      = this.getAlphabet().indexOf(lCurrentCharacter);
				/* Supply the Model Matrix. */
				pGLVectorProgram.onSupplyModelMatrix(pGLES20, pGLContext);
				/* Find the StartIndex. */
				final int lBufferStart = (lBufferIndex == 0) ? 0 : this.getGLBufferPackage().getIndices()[lBufferIndex - 1];
				/* Draw the buffer. */
				pGLES20.glDrawArrays(IGL.GL_TRIANGLES, lBufferStart, this.getGLBufferPackage().getIndices()[lBufferIndex] - lBufferStart);
			}
			/* Translate to the next character position. */
			GLMatrix.translateM(pGLContext.getModelMatrix(), (lGlyphData.onCalculateWidth(pFontScale)), 0.0f, 0.0f);
			/* Append to the CaretOffset. */
			lCaretOffset += lGlyphData.onCalculateWidth(pFontScale);
		}
		/* Return the CaretOffset. */
		return lCaretOffset;
	}

	/** TODO: Abstract dependence upon the font. Just need the translation. **/
	@Override
	public final void dispose() {
		/* Dispose of the GLBufferPackage. */
		this.getGLBufferPackage().dispose();
		/* Nullify the GLBufferPackage. */
		this.mGLBufferPackage = null;
		/* Remove our reference to the Font. (Backing data is garbage collected.) */
		this.mFont            = null;
	}
	
	public final String getAlphabet() {
		return this.mAlphabet;
	}
	
	public final IFont getFont() {
		return this.mFont;
	}
	
	private final GLBufferPackage<GLBuffer.XY_UV> getGLBufferPackage() {
		return this.mGLBufferPackage;
	}

}
