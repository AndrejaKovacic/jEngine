package renderEngine;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import models.RawModel;
import textures.TextureData;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

		public RawModel loadToVao(float[] positions,float[] textureCoordinates,float[] normals, int[] indices){
			//creates vertex array object, loads positions in it, returns RawModel containing it
			int vaoID = createVao();
			bindIndicesBuffer(indices);
			storeDataInAttributeList(positions,3,0);
			storeDataInAttributeList(textureCoordinates,2,1);
			storeDataInAttributeList(normals, 3, 2);
			unbindVao();
			//position.lenght/3 -> each vertex has 3 floats
			return new RawModel(vaoID,indices.length);
			
		}
		
		public RawModel loadToVao(float[] positions,float[] textureCoordinates,float[] normals,
				float[] tangents, int[] indices){
			//creates vertex array object, loads positions in it, returns RawModel containing it
			int vaoID = createVao();
			bindIndicesBuffer(indices);
			storeDataInAttributeList(positions,3,0);
			storeDataInAttributeList(textureCoordinates,2,1);
			storeDataInAttributeList(normals, 3, 2);
			storeDataInAttributeList(tangents, 3, 3);
			
			unbindVao();
			//position.lenght/3 -> each vertex has 3 floats
			return new RawModel(vaoID,indices.length);
			
		}
		
		public RawModel loadToVao(float[] positions, int dimensions){
			int vaoID = createVao();
			this.storeDataInAttributeList(positions, dimensions, 0);
			unbindVao();
			return new RawModel(vaoID, positions.length/dimensions);
		}
		
		
		private int createVao(){
			int vaoID = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoID);
			vaos.add(vaoID);
			return vaoID;
			
		}
		
		public int loadCubeMap(String[] textureFiles){
			int texID = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,texID);
			
			for(int i = 0; i < textureFiles.length; i++){
				TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
						data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, 
						GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			}
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			textures.add(texID);
			return texID;
		}
		
		private TextureData decodeTextureFile(String fileName) {
			int width = 0;
			int height = 0;
			ByteBuffer buffer = null;
			try {
				FileInputStream in = new FileInputStream(fileName);
				PNGDecoder decoder = new PNGDecoder(in);
				width = decoder.getWidth();
				height = decoder.getHeight();
				buffer = ByteBuffer.allocateDirect(4 * width * height);
				decoder.decode(buffer, width * 4, Format.RGBA);
				buffer.flip();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Tried to load texture " + fileName + ", didn't work");
				System.exit(-1);
			}
			return new TextureData(buffer, width, height);
		}
		
		private void storeDataInAttributeList(float[] data,int coordinateSize, int attributeNumber){
			int glVBO = GL15.glGenBuffers();
			vbos.add(glVBO);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, glVBO);
			//openGl requires data in floatBuffer
			FloatBuffer buffer = storeInFloatBuffer(data);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
			// Vertex buffer object into vertex array object
			GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
		
		private void unbindVao(){
			//unbinds currently bound
			GL30.glBindVertexArray(0);	
		}
		
		private FloatBuffer storeInFloatBuffer(float[] data){
			FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
			buffer.put(data);
			//preparing to be read from
			buffer.flip();
			return buffer;
		}
		
		public void cleanUp(){
			for (int vao:vaos)
				GL30.glDeleteVertexArrays(vao);
			
			for (int vbo:vbos)
				GL15.glDeleteBuffers(vbo);
			
			for (int texture:textures)
				GL11.glDeleteTextures(texture);
		}
		
		private void bindIndicesBuffer(int[] indices){
			int vboID = GL15.glGenBuffers();
			vbos.add(vboID);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
			
			IntBuffer buffer = storeInIntBuffer(indices);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
		}
		
		private IntBuffer storeInIntBuffer(int[] data){
			IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
			buffer.put(data);
			buffer.flip();
			return buffer;
		}
		
		public int loadTextures(String fileName){
			Texture texture = null;
			try {
				texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
				GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int textureID = texture.getTextureID();
			textures.add(textureID);
			return textureID;
		}
}