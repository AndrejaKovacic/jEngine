package water;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entity.Camera;
import entity.Light;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertex";
	private final static String FRAGMENT_FILE = "src/water/waterFragment";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int locationReflectionTexture;
	private int locationRefractionTexture;
	private int locationDuDvMap;
	private int locationMoveFactor;
	private int locationCameraPosition;
	private int locationNormalMap;
	private int locationLightColour;
	private int locationLightPosition;
	private int locationDepthMap;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		locationReflectionTexture = getUniformLocation("reflectionTexture");
		locationRefractionTexture = getUniformLocation("refractionTexture");
		locationDuDvMap = getUniformLocation("dudvMap");
		locationMoveFactor = getUniformLocation("moveFactor");
		locationCameraPosition = getUniformLocation("cameraPosition");
		locationNormalMap = getUniformLocation("normalMap");
		locationLightColour = getUniformLocation("lightColour");
		locationLightPosition = getUniformLocation("lightPosition");
		locationDepthMap = getUniformLocation("depthMap");
	}
	
	public void connectTextureUnits(){
		super.loadInt(locationReflectionTexture,0);
		super.loadInt(locationRefractionTexture, 1);
		super.loadInt(locationDuDvMap, 2);
		super.loadInt(locationNormalMap, 3);
		super.loadInt(locationDepthMap, 4);
	}
	
	public void loadMoveFactor(float factor){
		super.loadFloat(locationMoveFactor, factor);
	}
	
	public void loadLight(Light sun){
		super.loadVector(locationLightColour,sun.getColour());
		super.loadVector(locationLightPosition, sun.getPosition());
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(locationCameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
