package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;

public interface IVectorPathGroup extends IPathDefinition.Group {
	
	/* Static Declarations. */
	public static final IVectorPathGroup[] GROUP_NULL_LAYOUT = new IVectorPathGroup[]{ };
	
	public static class Impl implements IVectorPathGroup.W {
		
		/* Member Variables. */
		private VectorPath[]      mVectorPaths;
		private IPathDefinition[] mPathDefinitions;
		
		public Impl(final VectorPath pVectorPath, final IPathDefinition pPathDefinition) {
			this(new VectorPath[] { pVectorPath }, new IPathDefinition[] { pPathDefinition });
		}
		
		public Impl(final VectorPath[] pVectorPaths, final IPathDefinition[] pPathDefinitions) {
			/* Initialize Member Variables. */
			this.mVectorPaths     = pVectorPaths;
			this.mPathDefinitions = pPathDefinitions;
		}
		
		public void              setVectorPaths(final VectorPath[] pVectorPaths)              { this.mVectorPaths = pVectorPaths;         }
		public VectorPath[]      getVectorPaths()                                             { return this.mVectorPaths;                 }
		
		public void              setPathDefinitions(final IPathDefinition[] pPathDefinitions) { this.mPathDefinitions = pPathDefinitions; }
		public IPathDefinition[] getPathDefinitions()                                         { return this.mPathDefinitions;             }
		
	}
	
	public static interface W extends IVectorPathGroup {
		
		public abstract void setPathDefinitions(final IPathDefinition[] pPathDefinitions);
		public abstract void setVectorPaths(final VectorPath[] pVectorPaths);
		
	}
	
	public abstract VectorPath[] getVectorPaths();
	
}