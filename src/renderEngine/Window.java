package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Window {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 700;
	private static final String TITLE = "engine";
	private static final int FPS_CAP = 60;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void createWindow(){
		Display.setTitle(TITLE);
		

		ContextAttribs attribs = new ContextAttribs(3,2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs);
			
		}
		catch(LWJGLException e){
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateWindow(){
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = ( currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static void closeWindow(){
		Display.destroy();
	}
	
	private static long getCurrentTime(){
		return System.currentTimeMillis();
	}
	
	public static float getFrameTimeSeconds(){
		return delta;
	}
	
	public static int getWidth(){
		return WIDTH;
	}
	
	public static int getHeight(){
		return HEIGHT;
	}
	

}
