package uk.ac.manchester.sisp.ribbon.opengl.program;

import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.IScreenParameters;
import uk.ac.manchester.sisp.ribbon.opengl.matrix.IGLMatrixSource;

public interface IGLUniformProvider {
	
	public interface Color       extends IGLUniformProvider { public abstract void       onSupplyColor(final IGLES20 pGLES20, final float[] pColorRGBA);                      }
	public interface Scale       extends IGLUniformProvider { public abstract void       onSupplyScale(final IGLES20 pGLES20, final float ... pScale);                        }
	public interface Opacity     extends IGLUniformProvider { public abstract void     onSupplyOpacity(final IGLES20 pGLES20, final float pOpacity);                          }
	public interface Sampler2D   extends IGLUniformProvider { public abstract void   onSupplySampler2D(final IGLES20 pGLES20, final int pActiveTextureLocation);              }
	public interface GlobalTime  extends IGLUniformProvider { public abstract void  onSupplyGlobalTime(final IGLES20 pGLES20, final float pCurrentTimeSeconds);               }
	public interface Resolution  extends IGLUniformProvider { public abstract void  onSupplyResolution(final IGLES20 pGLES20, final IScreenParameters pGLScreenParameters); }
	public interface TextureSize extends IGLUniformProvider { public abstract void onSupplyTextureSize(final IGLES20 pGLES20, final int pWidth, final int pHeight);           }
	
	public interface WorldMatrix extends IGLUniformProvider { 
		public abstract void onSupplyModelMatrix(final IGLES20 pGLES20, final IGLMatrixSource pGLMatrixSource); 
		public abstract void onSupplyViewMatrix(final IGLES20 pGLES20, final IGLMatrixSource pGLMatrixSource); 
		public abstract void onSupplyProjectionMatrix(final IGLES20 pGLES20, final IGLMatrixSource pGLMatrixSource); 
	}
	
}