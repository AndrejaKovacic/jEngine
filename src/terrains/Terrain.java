package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.*;
import toolbox.Maths;

public class Terrain {
	//for water
	//private static final float SIZE = 300;
	private static final float SIZE = 550;
	private static final float MAX_HEIGHT = 40;
	private static final float MIN_HEIGHT = -40;
	
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	private float x;
	private float z;
	
	
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	public Terrain(float gridX, float gridZ, Loader loader, TerrainTexturePack texturePack, 
			TerrainTexture blendMap,String heightMap){
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader,heightMap);
	}

	public float getTerrainHeight(float worldX, float worldZ){
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / (float)(heights.length - 1);
		int gridX = (int)Math.floor(terrainX/gridSquareSize);
		int gridZ = (int)Math.floor(terrainZ/gridSquareSize);
		if ( gridX < 0 || gridX >= heights.length -1 || gridZ < 0 || gridZ >= heights.length -1)
			return 0;
		float xCoord = (terrainX % gridSquareSize);
		float zCoord = (terrainZ % gridSquareSize);
		float h;
		if (xCoord <= (1-zCoord)) {
			h = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			h = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return h;
	}
	
	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int VERTEX_COUNT = image.getHeight();
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVao(vertices, textureCoords, normals, indices);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	private float getHeight(int x, int z, BufferedImage image){
		if ( x < 0 || x > image.getWidth() || z < 0 || z > image.getHeight())
			return 0;
		float height  = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		
		return height;
	}
}
