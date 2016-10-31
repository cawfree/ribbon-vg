package uk.ac.manchester.sisp.ribbon.opengl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import uk.ac.manchester.sisp.ribbon.global.EReleaseMode;
import uk.ac.manchester.sisp.ribbon.global.RibbonGlobal;
import uk.ac.manchester.sisp.ribbon.opengl.exception.GLException;
import uk.ac.manchester.sisp.ribbon.opengl.global.GLGlobal;
import uk.ac.manchester.sisp.ribbon.opengl.matrix.IGLMatrixSource;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.ResourceUtils;

public abstract class GLContext implements IGLScreenParameters, IGLEventListener, IGLMatrixSource {
	
	private static final float DEFAULT_DPI = 96.0f;
	
	/* Track screen configuration. */
	private final float mDotsPerInch;
	
	/* Efficient multithreaded FIFO. */
	private transient volatile ConcurrentLinkedQueue<IGLRunnable> mRunnableQueue;
	
	/* Screen Dimensions. */
	private int mScreenX;
	private int mScreenY;
	private int mScreenWidth;
	private int mScreenHeight;
	
	/* OpenGL Matrices. */
	private final float[] mModelMatrix;
	private final float[] mViewMatrix;
	private final float[] mProjectionMatrix;
	
	/* GLMatrix Stack. */
	private final LinkedList<float[]> mMatrixStack;
	
	/* Delegate Management. */
	private final List<GLDelegate> mDelegates;
	
	/* Timing. */
	private float mFrameSeconds;
	
	public GLContext(final float pDotsPerInch) {
		/* Define the screen Configuration. */
		this.mDotsPerInch     = (Float.isInfinite(pDotsPerInch) || Float.isNaN(pDotsPerInch) || pDotsPerInch == 0) ? GLContext.DEFAULT_DPI : pDotsPerInch;
		/* Instantiate efficient multithreaded FIFO. */
		this.mRunnableQueue = new ConcurrentLinkedQueue<IGLRunnable>();
		/* Initialze Screen Dimensions. */
		this.mScreenX      = 0;
		this.mScreenY      = 0;
		this.mScreenWidth  = 0;
		this.mScreenHeight = 0;
		/* Instantiate OpenGL Matrices. */
		this.mModelMatrix      = new float[GLGlobal.GL_MATRIX_SIZE];
		this.mViewMatrix       = new float[GLGlobal.GL_MATRIX_SIZE];
		this.mProjectionMatrix = new float[GLGlobal.GL_MATRIX_SIZE];
		/* Instantiate the ModelMatrixStack. */
		this.mMatrixStack  = new LinkedList<float[]>();
		/* Initialize Delegate Management. */
		this.mDelegates    = new ArrayList<GLDelegate>();
		/* Initialize the FrameSeconds. */
		this.mFrameSeconds = ResourceUtils.getSystemTimeSeconds();
	}

	@Override
	public void onInitialize(final IGLES20 pGLES20) {
		/* Print the OpenGL Configuration. */
		if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEVELOPMENT)) {
			System.out.println("GL_VENDOR: " + pGLES20.glGetString(IGL.GL_VENDOR) + "\nGL_RENDERER: " + pGLES20.glGetString(IGL.GL_RENDERER) + "\nGL_VERSION: (Minimum Support: GLSL 1.3): " + pGLES20.glGetString(IGL.GL_VERSION)+"\nGL_MONITOR_DPI: "+this.getDotsPerInch());
		}
		/* Support Transparency Blending. */
		pGLES20.glEnable(IGL.GL_BLEND);
		/* Enable Multisampling. */
		pGLES20.glEnable(IGL.GL_MULTISAMPLE);
		/* Reference the vector Blending Function. */
		pGLES20.glBlendFunc(IGL.GL_SRC_ALPHA, IGL.GL_ONE_MINUS_SRC_ALPHA);
		/* Enable the Scissor Test. */
		pGLES20.glEnable(IGL.GL_SCISSOR_TEST);
		/* If we're restoring the OpenGL context, then re-deploy all GLDelegates. */
		for(final GLDelegate lGLDelegate : this.getDelegates()) {
			/* Load the GLDelegate. */
			lGLDelegate.load(pGLES20);
		}
	}

	@Override
	public final void onResized(final IGLES20 pGLES20, final int pX, final int pY, final int pWidth, final int pHeight) {
		/* Track the current dimensions of the screen. */
		this.mScreenX      = pX;
		this.mScreenY      = pY;
		this.mScreenWidth  = pWidth;
		this.mScreenHeight = pHeight;
		/* Configure the Viewport to match the screen. */
		pGLES20.glViewport(pX, pY, pWidth, pHeight);
		/* Ensure no pixels are drawn beyond the Viewport. */
		pGLES20.glScissor(pX, pY, pWidth, pHeight);
	}
	
	@Override
	public final void onDisplay(final IGLES20 pGLES20) {
		/* Handle the RunnableQueue. */
		while(DataUtils.isNotNull(this.getRunnableQueue().peek())) {
			/* Process the Runnable. */
			IGLRunnable lGLRunnable = this.getRunnableQueue().poll();
			/* Execute the Runnable. */
			lGLRunnable.run(pGLES20, this);
		}
		/* Initialize the diagram's background colour. */
		pGLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		/* Clear the screen. */
		pGLES20.glClear(IGLES20.GL_STENCIL_BUFFER_BIT | IGLES20.GL_COLOR_BUFFER_BIT | IGLES20.GL_DEPTH_BUFFER_BIT);
		/* Write the FrameSeconds. */
		this.mFrameSeconds = ResourceUtils.getSystemTimeSeconds();
		/* Render the application. */
		this.onRenderFrame(pGLES20, this.getFrameSeconds());
		/* Finally, flush these updates down to the GPU. */
		pGLES20.glFlush();
		/* Finish OpenGL I/O. */
		pGLES20.glFinish();
		/* Clear the MatrixStack in preparation for the next iteration. */
		while(DataUtils.isNotNull(this.getMatrixStack().poll()));
	}
	
	/** Allocates the GLDelegates with active OpenGL context management. **/
	public final void onSupplyDelegates(final IGLES20 pGLES20, final GLDelegate... pGLDelegates) {
		/* Iterate over each GLDelegate. */
		for(final GLDelegate lGLDelegate : pGLDelegates) {
			/* Check that the GLDelegate is not already managed. */
			if(!this.getDelegates().contains(lGLDelegate)) {
				/* Load the GLDelegate. */
				lGLDelegate.load(pGLES20);
				/* Add the GLDelegate to the GLDelegates list. */
				this.getDelegates().add(lGLDelegate);
			}
			else {
				throw new GLException("Attempted to load a loaded delegate!");
			}
		}
	}
	
	/** Destroys active OpenGL context management for the GLDelegates. **/
	public final void onWithdrawDelegates(final IGLES20 pGLES20, final GLDelegate... pGLDelegates) {
		/* Iterate over each GLDelegate. */
		for(final GLDelegate lGLDelegate : pGLDelegates) {
			/* Determine if the supplied GLDelegate is not already managed. */
			if(this.getDelegates().contains(lGLDelegate)) {
				/* Load the GLDelegate. */
				lGLDelegate.unload(pGLES20);
				/* Add the GLDelegate to the GLDelegates list. */
				this.getDelegates().remove(lGLDelegate);
			}
			else {
				throw new GLException("Attempted to unload an unloaded delegate!");
			}
		}
	}
	
	@Override
	public final void onDispose(final IGLES20 pGLES20) {
		/* Temporarily destroy all resources. (Initialize later!) */
		for(final GLDelegate lGLDelegate : this.getDelegates()) {
			/* Unload the GLDelegate. (But don't destroy our reference to it!) */
			lGLDelegate.unload(pGLES20);
		}
	}

	public final <T extends IGLRunnable> T invokeLater(final T pGLRunnable) {
		/* Offer the GLRunnable to the RunnableQueue. */
		this.getRunnableQueue().offer(pGLRunnable);
		/* Return the GLRunnable. */
		return pGLRunnable;
	}

	public final <T extends IGLRunnable> T invokeAndWait(final T pGLRunnable) {
		/* Create a synchronized form of the GLRunnable. */
		final IGLRunnable lLockedRunnable = new IGLRunnable() { @Override public final void run(final IGLES20 pGLES20, final GLContext pGLContext) { synchronized(this) { pGLRunnable.run(pGLES20, pGLContext); this.notify(); } } };
		/* Obtain synchronized access to the LockedRunnable. */
		synchronized(lLockedRunnable) {
			try {
				/* Offer the LockedRunnable to the RunnableQueue. */
				this.getRunnableQueue().offer(lLockedRunnable);
				/* Wait upon the LockedRunnable. */
				lLockedRunnable.wait();
			} catch (final InterruptedException pInterruptedException) { /* Ignore exceptions. */ }
		}
		/* Return the GLRunnable. */
		return pGLRunnable;
	}
	
	protected abstract void onRenderFrame(final IGLES20 pGLES20, final float pCurrentTimeSeconds);

	@Override
	public final float getDotsPerInch() {
		return this.mDotsPerInch;
	}
	
	private final ConcurrentLinkedQueue<IGLRunnable> getRunnableQueue() {
		return this.mRunnableQueue;
	}

	@Override
	public final LinkedList<float[]> getMatrixStack() {
		return this.mMatrixStack;
	}

	@Override
	public final int getScreenX() {
		return this.mScreenX;
	}

	@Override
	public final int getScreenY() {
		return this.mScreenY;
	}

	@Override
	public final int getScreenWidth() {
		return this.mScreenWidth;
	}

	@Override
	public final int getScreenHeight() {
		return this.mScreenHeight;
	}
	
	@Override
	public final float[] getModelMatrix() {
		return this.mModelMatrix;
	}
	
	@Override
	public final float[] getViewMatrix() {
		return this.mViewMatrix;
	}
	
	@Override
	public final float[] getProjectionMatrix() {
		return this.mProjectionMatrix;
	}
	
	private final List<GLDelegate> getDelegates() {
		return this.mDelegates;
	}
	
	public final float getFrameSeconds() {
		return this.mFrameSeconds;
	}

}
