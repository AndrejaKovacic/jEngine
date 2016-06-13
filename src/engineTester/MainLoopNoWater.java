package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
//import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.*;
import models.*;
import renderEngine.*;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;


public class MainLoopNoWater {
	
	
	
	public static void main(String[] args){

		Window.createWindow();
		Loader loader = new Loader();
		MasterRender renderer = new MasterRender(loader);

		
		List<Entity> entities = new ArrayList<Entity>();
		
		RawModel model = OBJLoader.loadObjModel("fern", loader);
		RawModel bunny = OBJLoader.loadObjModel("stanfordBunny", loader);
		ModelTexture texture = new ModelTexture(loader.loadTextures("fern"));
		ModelTexture skin = new ModelTexture(loader.loadTextures("uni"));
		
		RawModel rawTree = OBJLoader.loadObjModel("lowPolyTree", loader);
		ModelTexture treeTextureAtlas = new ModelTexture(loader.loadTextures("lowPolyTree"));
		treeTextureAtlas.setNumberOfRows(2);
		treeTextureAtlas.setShineDamper(10);
		treeTextureAtlas.setHasTransparency(true);
		
		TexturedModel tree = new TexturedModel(rawTree, treeTextureAtlas);
		
		skin.setReflectivity(0.5f);
		skin.setShineDamper(10);
		
		texture.setReflectivity(0.2f);
		texture.setShineDamper(10);
		texture.setHasTransparency(true);
		texture.setFakeLighting(true);
		TexturedModel fern = new TexturedModel(model,texture);
		TexturedModel stanfordBunny = new TexturedModel(bunny,skin);
		
		Player player = new Player(stanfordBunny,0,new Vector3f(0,0,0),0,180,0,1);
		
		
		//terrain multitexturing
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTextures("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTextures("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTextures("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTextures("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTextures("blendMap"));
		Terrain terrain = (new Terrain(-0.5f, -0.5f, loader,texturePack,blendMap,"heightMap"));
	
		Random random = new Random();
		for (int i = 0; i < 10; i++){
			float x = random.nextFloat() * 600 - 300;
			float z = random.nextFloat() * 600 - 300;
			entities.add(new Entity(fern,0,new Vector3f
					(x,terrain.getTerrainHeight(x, z), z),0,0,0,2));
			}
		
		for (int i = 0; i < 100; i++){
			float x = random.nextFloat() * 600 - 300;
			float z = random.nextFloat() * 600 - 300;
			entities.add(new Entity(tree,random.nextInt(4),new Vector3f
					(x,terrain.getTerrainHeight(x, z), z),0,0,0,2));
			}
		
		Camera camera = new Camera(player);
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(10000, 100000, -10000),new Vector3f(1,1,1));
		lights.add(sun);
		Light light2 = new Light(new Vector3f(300,1000,20),new Vector3f(0.2f,0.5f,0.3f));
		//lights.add(light2);
		Light light3 = new Light(new Vector3f(0,50,0),new Vector3f(2,2,0),new Vector3f(1,0.01f,0.7f));
		//lights.add(light3);
		
		
		  
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());
		
		while(!Display.isCloseRequested()){

			camera.move();
			player.move(terrain);
			picker.update();
			//System.out.println(picker.getCurrentRay());

			renderer.renderScene(entities,lights, terrain, camera,new Vector4f(0,-1,0,0));
			renderer.processEntity(player);


			Window.updateWindow();
		}
		

		renderer.cleanUp();
		loader.cleanUp();
		Window.closeWindow();
	}
	

}
