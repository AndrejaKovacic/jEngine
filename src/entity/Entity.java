package entity;

import org.lwjgl.util.vector.Vector3f;

import models.*;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rx;
	private float ry;
	private float rz;
	private float scale;
	
	private int textureIndex = 0;
	
	
	public Entity(TexturedModel model,int index, Vector3f position, float rx, float ry, float rz, float scale) {
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
	}

	public float getTextureXOffset(){
		int collumn = textureIndex % model.getModelTexture().getNumberOfRows();
		return (float) collumn / (float) model.getModelTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex / model.getModelTexture().getNumberOfRows();
		return (float) row / (float) model.getModelTexture().getNumberOfRows();
	}
	
	public void increasePositon(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float drx, float dry, float drz){
		this.rx += drx;
		this.ry += dry;
		this.rz += drz;
	}

	public TexturedModel getModel() {
		return model;
	}


	public Vector3f getPosition() {
		return position;
	}


	public float getRx() {
		return rx;
	}


	public float getRy() {
		return ry;
	}


	public float getRz() {
		return rz;
	}


	public float getScale() {
		return scale;
	}


	public int getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}


	public void setPosition(Vector3f position) {
		this.position = position;
	}


	public void setRx(float rx) {
		this.rx = rx;
	}


	public void setRy(float ry) {
		this.ry = ry;
	}


	public void setRz(float rz) {
		this.rz = rz;
	}


	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
}
