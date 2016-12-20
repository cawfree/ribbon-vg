package uk.ac.manchester.sisp.ribbon.opengl.program;

import uk.ac.manchester.sisp.ribbon.global.EReleaseMode;
import uk.ac.manchester.sisp.ribbon.global.RibbonGlobal;
import uk.ac.manchester.sisp.ribbon.opengl.GLDelegate;
import uk.ac.manchester.sisp.ribbon.opengl.IGLBindable;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.shaders.GLShader;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.GLUtils;

public abstract class GLProgram extends GLDelegate implements IGLBindable {
	
	/* Member Variables. */
	private final GLShader.Vertex   mVertexShader;
	private final GLShader.Fragment mFragmentShader;

	public GLProgram(final GLShader.Vertex pVertexShader, final GLShader.Fragment pFragmentShader) {
		/* Initialize Member Variables. */
		this.mVertexShader   = pVertexShader;
		this.mFragmentShader = pFragmentShader;
	}
	
	public final GLShader.Vertex getVertexShader() {
		return this.mVertexShader;
	}
	
	public final GLShader.Fragment getFragmentShader() {
		return this.mFragmentShader;
	}
	
	@Override
	protected final int onLoad(final IGLES20 pGLES20) {
		/* Attempt to allocate a pointer towards the program. */
		final int lProgramId = pGLES20.glCreateProgram();
		/* Determine whether initialization was a success. */
		if(GLUtils.isUnsuccessful(lProgramId)) {
			throw new RuntimeException(this.getClass().getSimpleName() + " failed to allocate a pointer. Aborting...");
		}
		/* Attach the Vertex Shader. */
		pGLES20.glAttachShader(lProgramId, this.getVertexShader().getGLObjectId());
		/* Attach the Fragment Shader. */
		pGLES20.glAttachShader(lProgramId, this.getFragmentShader().getGLObjectId());
		/* Link the Program. */
		pGLES20.glLinkProgram(lProgramId);
		/* Push the linker status into the buffer. */
		pGLES20.glGetProgramiv(lProgramId, IGLES20.GL_LINK_STATUS, this.getDelegationBuffer(), 0);
		/* Process the status. */
		if(this.isDelegationBufferBadStatus()) {
			/* Delete the program. */
			pGLES20.glDeleteProgram(lProgramId);
			/* Throw a RuntimeException. */
			throw new RuntimeException(this.getClass().getSimpleName() + " failed to link! Aborting...");
		}
		/* Return the ID. */
		return lProgramId;
	}

	@Override
	protected final void onUnload(final IGLES20 pGLES20) {
		/* Delete the program. */
		pGLES20.glDeleteProgram(this.getGLObjectId());
	}

	@Override
	public final void bind(final IGLES20 pGLES20) {
		/* Configure OpenGL to use the program. */
		pGLES20.glUseProgram(this.getGLObjectId());
	}

	@Override
	public final void unbind(final IGLES20 pGLES20) {
		/* Configure OpenGL to use a null program. */
		pGLES20.glUseProgram(IGLES20.GL_NONE);
	}
	
	public final int getUniformLocation(final IGLES20 pGLES20, final String pUniformTag) {
		/* Fetch the UniformLocation. */
		final int lUniformLocation = pGLES20.glGetUniformLocation(this.getGLObjectId(), pUniformTag);
		/* Enable debugging tools. */
		if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEVELOPMENT)) {
			if(lUniformLocation == DataUtils.JAVA_NULL_INDEX) {
				//throw new GLException("Uniform "+pUniformTag+" could not be found!");
			}
		}
		/* Return the UniformLocation. */
		return lUniformLocation;
	}
	
	public final int getAttributeLocation(final IGLES20 pGLES20, final String pAttributeTag) {
		/* Fetch the AttributeLocation. */
		final int lAttributeLocation = pGLES20.glGetAttribLocation(this.getGLObjectId(), pAttributeTag);
		/* Enable debugging tools. */
		if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEVELOPMENT)) {
			if(lAttributeLocation == DataUtils.JAVA_NULL_INDEX) {
				//throw new GLException("Attribute "+pAttributeTag+" could not be found!");
			}
		}
		/* Return the AttributeLocation. */
		return lAttributeLocation;
	}

}
