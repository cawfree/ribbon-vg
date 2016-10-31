package uk.ac.manchester.sisp.ribbon.opengl.buffers;

public interface IGLAttributeProvider {
	
	public static interface Position extends IGLAttributeProvider {
		public abstract int getAttributePosition();
	}
	
	public static interface Procedural extends IGLAttributeProvider {
		public abstract int getAttributeProcedural();
	}
	
	public static interface Color extends IGLAttributeProvider {
		public abstract int getAttributeColor();
	}

}
