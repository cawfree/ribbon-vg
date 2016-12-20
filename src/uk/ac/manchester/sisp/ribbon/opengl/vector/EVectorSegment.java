package uk.ac.manchester.sisp.ribbon.opengl.vector;

public enum EVectorSegment {

	MOVE_TO(2), LINE_TO(2), BEZIER_TO(4), CLOSE(0);
	
	private final int mNumberOfComponents;
	
	EVectorSegment(final int pNumberOfComponents) {
		this.mNumberOfComponents = pNumberOfComponents;
	}
	
	public final int getNumberOfComponents() {
		return this.mNumberOfComponents;
	}
	
	public static final EVectorSegment onFetchSegment(final VectorPath pVectorPath, final int pPosition) {
		return EVectorSegment.values()[(int)pVectorPath.getPathData()[pPosition]];
	}
	
}
