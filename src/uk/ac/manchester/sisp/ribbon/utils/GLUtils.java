package uk.ac.manchester.sisp.ribbon.utils;

import java.util.HashMap;

import uk.ac.manchester.sisp.ribbon.opengl.IGL;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.matrix.IGLMatrixSource;
import uk.ac.manchester.sisp.ribbon.opengl.program.IGLUniformProvider;
import uk.ac.manchester.sisp.ribbon.ui.global.UIGlobal;

public final class GLUtils {

	private static final HashMap<Integer, String> OPENGL_ERROR_TABLE = new HashMap<Integer, String>();
	
	/* Initialize the Error Table. */
	static {
		GLUtils.OPENGL_ERROR_TABLE.put(IGL.GL_NONE, "GL_NONE");
		GLUtils.OPENGL_ERROR_TABLE.put(IGL.GL_INVALID_ENUM,  "GL_INVALID_ENUM");
		GLUtils.OPENGL_ERROR_TABLE.put(IGL.GL_INVALID_VALUE, "GL_INVALID_VALUE");
		GLUtils.OPENGL_ERROR_TABLE.put(IGL.GL_INVALID_OPERATION, "GL_INVALID_OPERATION");
		GLUtils.OPENGL_ERROR_TABLE.put(0x0503, "GL_STACK_OVERFLOW");
		GLUtils.OPENGL_ERROR_TABLE.put(0x0504, "GL_STACK_UNDERFLOW");
		GLUtils.OPENGL_ERROR_TABLE.put(IGL.GL_OUT_OF_MEMORY, "GL_OUT_OF_MEMORY");
		GLUtils.OPENGL_ERROR_TABLE.put(IGL.GL_INVALID_FRAMEBUFFER_OPERATION, "GL_INVALID_FRAMEBUFFER_OPERATION");
		GLUtils.OPENGL_ERROR_TABLE.put(0x8031, "GL_TABLE_TOO_LARGE");
	}
	
	public static final String getErrorMessage(final IGL pGL) {
		return GLUtils.OPENGL_ERROR_TABLE.get(pGL.glGetError());
	}
	
	public static final boolean isUnsuccessful(final int pGLCode) {
		return pGLCode == IGL.GL_NONE;
	}
	
	public static final void onUpdateWorldMatrices(final IGLES20 pGLES20, final IGLUniformProvider.WorldMatrix pWorldMatrixProvider, final IGLMatrixSource pGLMatrixSource) {
		/* Supply the Projection Matrix. */
		pWorldMatrixProvider.onSupplyProjectionMatrix(pGLES20, pGLMatrixSource);
		/* Supply the Model Matrix. */
		pWorldMatrixProvider.onSupplyModelMatrix(pGLES20, pGLMatrixSource);
		/* Supply the View Matrix. */
		pWorldMatrixProvider.onSupplyViewMatrix(pGLES20, pGLMatrixSource);
	}
	
	/** TODO: Force 2D in SupplyScale! **/
	public static final void onReinitializeScale(final IGLES20 pGLES20, final IGLUniformProvider.Scale pScaleProvider) {
		pScaleProvider.onSupplyScale(pGLES20, UIGlobal.UI_UNITY, UIGlobal.UI_UNITY);
	}
	
	/* Prevent direct instantiation of this class. */
	private GLUtils() {}
	
}
