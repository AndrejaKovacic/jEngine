package textures;

public class ModelTexture {
	
	private int textureID;
	private int normalMap;

	private float shineDamper = 1;
	private float reflectivity = 0;
	
	
	private boolean hasTransparency = false;
	private boolean fakeLighting = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	public int getID(){
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isFakeLighting() {
		return fakeLighting;
	}

	public void setFakeLighting(boolean fakeLighting) {
		this.fakeLighting = fakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
