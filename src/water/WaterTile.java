package water;

public class WaterTile {
	
	public static final float TILE_SIZE = 30;
	
	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height){
		this.x = centerX + TILE_SIZE;
		this.z = centerZ + TILE_SIZE;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
