package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entity.Camera;
import renderEngine.Window;
import shaders.ShaderProgram;
import toolbox.Maths;
 
public class SkyboxShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int locationFogColour;
    private int locationBlendFactor;
    private int locationCubeMap;
    private int locationCubeMap2;
    
    private static final float ROTATE_SPEED = 1f;
    
    private float rotation = 0;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATE_SPEED * Window.getFrameTimeSeconds();
        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        locationFogColour = super.getUniformLocation("fogColour");
        locationBlendFactor = super.getUniformLocation("blendFactor");
        locationCubeMap = super.getUniformLocation("cubeMap");
        locationCubeMap2 = super.getUniformLocation("cubeMap2");
    }
    
    public void connectTextureUnits(){
    	super.loadInt(locationCubeMap,0);
    	super.loadInt(locationCubeMap2, 1);
    }
    
    public void loadBlendFactor(float factor){
    	super.loadFloat(locationBlendFactor, factor);
    }
 
    public void loadFogColour(float r, float g, float b){
    	super.loadVector(locationFogColour, new Vector3f(r,g,b));
    }
    
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}
