package uk.ac.manchester.sisp.ribbon.opengl.textures;

import java.util.Map;

public class GLTextureAtlas <T> { /** TODO: Implement IDisposable? What about OpenGL? **/
	
	/* Member Variables. */
	private final GLTexture       mGLTexture;
	private final Map<T, float[]> mTextureMap; /** TODO: How to encapsulate the expectation of X, Y, Width, Height? **/
	
	public GLTextureAtlas(final GLTexture pGLTexture, final Map<T, float[]> pTextureMap) {
		/* Initialize Member Variables. */
		this.mGLTexture  = pGLTexture;
		this.mTextureMap = pTextureMap;
	}
	
	public final GLTexture getGLTexture() {
		return this.mGLTexture;
	}
	
	public final Map<T, float[]> getTextureMap() {
		return this.mTextureMap;
	}
	
}
