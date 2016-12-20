package uk.ac.manchester.sisp.ribbon.opengl.shaders;

import uk.ac.manchester.sisp.ribbon.global.EReleaseMode;
import uk.ac.manchester.sisp.ribbon.global.RibbonGlobal;
import uk.ac.manchester.sisp.ribbon.opengl.GLDelegate;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.utils.GLUtils;

public abstract class GLShader extends GLDelegate {
	
	public static class Vertex extends GLShader {
		
		/* GLSL 130+ uses 'in' and 'out' instead of attribute and varying. */
		private static final String HEADER_VERSION_MIGRATION = "" +
		      "#if __VERSION__ >= 130"
		+"\n"+"\t"+"#define attribute in"
		+"\n"+"\t"+"#define varying   out"
		+"\n"+"#endif"
		+"\n";

		public Vertex(final String pShaderSourceCode) {
			super(IGLES20.GL_VERTEX_SHADER, GLShader.Vertex.HEADER_VERSION_MIGRATION + pShaderSourceCode);
		}
		
	}
	
	public static class Fragment extends GLShader {
		
		private static final String HEADER_PLATFORM_PRECISION = "" +
//				  "#if GL_ES >= 130" // or version
//		+"\n"+"\t"+"precision mediump float;"
//		+"\n"+"\t"+"precision mediump int;"
//		+"\n"+"#endif"
//		+"\n";
"";
		
		public Fragment(final String pShaderSourceCode) {
			super(IGLES20.GL_FRAGMENT_SHADER, GLShader.Fragment.HEADER_PLATFORM_PRECISION + pShaderSourceCode);
		}
		
	}
	
	private final int    mShaderType;
	private final String mShaderSourceCode;
	
	public GLShader(final int pShaderType, final String pShaderSourceCode) {
		super();
		this.mShaderType       = pShaderType;
		this.mShaderSourceCode = pShaderSourceCode;
	}
	
	@Override
	protected final int onLoad(final IGLES20 pGLES20) {
		/* Attempt to create a Shader. */
		final int lShaderObjectId = pGLES20.glCreateShader(this.getShaderType());
		/* Operate conditionally depending on whether Shader creation was successful. */
		if(!GLUtils.isUnsuccessful(lShaderObjectId)) {
			/* Upload the shader source. */
			pGLES20.glShaderSource(lShaderObjectId, this.getShaderSourceCode());
			/* Compile the shader. */
			pGLES20.glCompileShader(lShaderObjectId);
			/* Determine whether to assess the status. */
			if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEBUG)) {
				/* Acquire the compilation status. */
				pGLES20.glGetShaderiv(lShaderObjectId, IGLES20.GL_COMPILE_STATUS, this.getDelegationBuffer(), 0);
				/* Determine if compilation failed. */
				if(this.isDelegationBufferBadStatus()) {
					/* Delete the shader. */
					pGLES20.glDeleteShader(lShaderObjectId);
					/* Throw a RuntimeException. */
					throw new RuntimeException("Could not compile " + this.getClass().getSimpleName() + "!");
				}
			}
			/* Return the created shader. */
			return lShaderObjectId;
		}
		throw new RuntimeException("Could not create "+ this.getClass().getSimpleName() +"!");
	}

	@Override
	protected final void onUnload(final IGLES20 pGLES20) {
		/* Delete the shader. */
		pGLES20.glDeleteShader(this.getGLObjectId());
	}

	public final int getShaderType() {
		return this.mShaderType;
	}
	
	public final String getShaderSourceCode() {
		return this.mShaderSourceCode;
	}

}
