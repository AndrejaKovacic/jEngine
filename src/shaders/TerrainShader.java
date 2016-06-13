package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 3;

	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int[] locationLightPosition;
	private int[] locationLightColour;
	private int[] locationAttenuation;
	private int locationReflectivity;
	private int locationShineDamper;
	private int locationSkyColour;
	private int locationBackgroundTexture;
	private int locationrTexture;
	private int locationgTexture;
	private int locationbTexture;
	private int locationBlendMap;
	private int locationPlane;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1,"texture");
		super.bindAttribute(2, "normal");
		
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationSkyColour = super.getUniformLocation("skyColour");
		locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
		locationrTexture = super.getUniformLocation("rTexture");
		locationgTexture = super.getUniformLocation("gTexture");
		locationbTexture = super.getUniformLocation("bTexture");
		locationBlendMap = super.getUniformLocation("blendMap");
		locationPlane = super.getUniformLocation("plane");
		
		locationLightPosition = new int[MAX_LIGHTS];
		locationLightColour = new int[MAX_LIGHTS];
		locationAttenuation = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++){
			locationLightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			locationLightColour[i] = super.getUniformLocation("lightColour["+i+"]");
			locationAttenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
	} 
	
	public void connectTextureUnits(){
		super.loadInt(locationBlendMap, 4);
		super.loadInt(locationbTexture, 3);
		super.loadInt(locationgTexture, 2);
		super.loadInt(locationrTexture, 1);
		super.loadInt(locationBackgroundTexture, 0);
	}
	
	public void loadClipPlane(Vector4f plane){
		super.loadVector(locationPlane, plane);
	}
	
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(locationSkyColour, new Vector3f(r,g,b));
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	public void loadLights(List<Light> lights){
		for(int i = 0; i < MAX_LIGHTS; i++)
			if(i < lights.size()){
				super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
				super.loadVector(locationLightColour[i], lights.get(i).getColour());
				super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
			}
			else{
				super.loadVector(locationLightPosition[i], new Vector3f(0,0,0));
				super.loadVector(locationLightColour[i], new Vector3f(0,0,0));
				super.loadVector(locationAttenuation[i], new Vector3f(1,0,0));
			}
	}

	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(locationProjectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
}
