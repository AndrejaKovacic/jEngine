package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.Window;
import terrains.Terrain;

public class Player extends Entity{
	
	private static final float  RUN_SPEED = 20;
	private static final float  TURN_SPEED = 100;
	private static final float  GRAVITY = -50;
	private static final float  JUMP_POWER = 40;
	
	private boolean isGrounded = true;
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currentUpwardsSpeed = 0;
	

	public Player(TexturedModel model,int index, Vector3f position, float rx, float ry, float rz, float scale) {
		super(model,index, position, rx, ry, rz, scale);
		super.increasePositon(0, 0 , 0);
	}
	
	
	public void move(Terrain terrain){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * Window.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * Window.getFrameTimeSeconds();
		
		float dx = distance * ((float)Math.sin(Math.toRadians(super.getRy())));
		float dz = distance * ((float)Math.cos(Math.toRadians(super.getRy())));
		super.increasePositon(dx, 0, dz);
		
																																																																																																																																							
		currentUpwardsSpeed += GRAVITY * Window.getFrameTimeSeconds();
		float terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
		super.increasePositon(0, currentUpwardsSpeed * Window.getFrameTimeSeconds(),  0);
		if (super.getPosition().y < terrainHeight){
			currentUpwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
			isGrounded = true;
			//super.increasePositon(0, TERRAIN_HEIGHT - super.getPosition().y, 0);;
		}
	}
	
	private void jump(){
		if (isGrounded)
		this.currentUpwardsSpeed = JUMP_POWER;
	}
	
	private void checkInputs(){
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			this.currentSpeed = RUN_SPEED;
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			this.currentSpeed = -RUN_SPEED;
		else
			this.currentSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			this.currentTurnSpeed = -TURN_SPEED;
		else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			this.currentTurnSpeed = TURN_SPEED;
		else
			this.currentTurnSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
			isGrounded = false;
		}
	}

}
