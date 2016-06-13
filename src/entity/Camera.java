package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	

	private float yaw;
	private Vector3f position = new Vector3f(0,20,0);
	
	private Player player;
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float pitch = 20;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		calculateZoom();
		calculateAngleAroundPlayer();
		calculatePitch();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		yaw = 180 - (player.getRy() + angleAroundPlayer);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			position.y += 0.5f;
		else if (Keyboard.isKeyDown(Keyboard.KEY_E))
			position.y -= 0.5f;
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x += 0.5f;
		else if (Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x -= 0.5f;
		else if (Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= 0.5f;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += 0.5f;
		
		
	}
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch(){
		//0 - left mouse button
		//1 - right mouse button
		if (Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
		float theta = player.getRy() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
	}
}
