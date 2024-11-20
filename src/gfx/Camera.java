package gfx;

import java.util.Optional;

import gameobjects.GameObject;
import gamestates.PlayState;

/**
 * The camera follows the player at all times. <br>
 * It moves around the screen when the player moves. <br>
 * 
 * The code for some parts of the camera was inspired by a YouTuber <br>
 * named M3832. The link to the video can be found here: <br>
 * https://www.youtube.com/watch?v=48bXkCemgB8&list=PLfHzvwt1lruOl0h6nn72545ISqh6Y1aPb&index=14
 * 
 * @author Elliott Vince
 */

public class Camera {
	
	private PlayState playState;
	
	private int posX;
	private int posY;
	
	public int width;
	public int height;
	
	private Optional<GameObject> focusedObject;

	/**
	 * Creates a new camera object.
	 * @param playState An instance of the playState class.
	 * @param width The width of the camera.
	 * @param height The height of the camera.
	 */
	
	public Camera(PlayState playState, int width, int height) {
		this.playState = playState;
		this.width = width;
		this.height = height;
		
		this.posX = 0;
		this.posY = 0;
	}
	
	/**
	 * Updates the camera.
	 */
	
	public void update() {
		// Checks if the focused object exists.
		if (focusedObject.isPresent()) {
			
			// Get the x and y position of the focused object.
			
			/*
			 * The number 32 is used because it is half of the width and height
			 * of the player.
			 */
			
			int x = focusedObject.get().getPosX() + 
					focusedObject.get().getWidth() / 2 - width / 2;
			
			int y = focusedObject.get().getPosY() +
					focusedObject.get().getHeight() / 2 - height / 2;
			
			/*
			 * Set the position of the camera to the focused object's position.
			 * Note: This will only update if the camera will not render any
			 * blank space behind the image.
			 */
			
			if (x >= playState.getCameraBounds().getMinX() &&
					x <= playState.getCameraBounds().getMaxX() - width) {
				posX = x;
			}
			
			if (y >= playState.getCameraBounds().getMinY() &&
					y <= playState.getCameraBounds().getMaxY() - height) {
				posY = y;
			}
			
			
		}
	}
	
	/**
	 * Set the object that the camera should focus on.
	 * @param gameObject The object that the camera should focus on.
	 */

	public void setFocusedObject(GameObject gameObject) {
		this.focusedObject = Optional.of(gameObject);
	}
	
	/**
	 * Sets the position of the camera.
	 * @param posX The new x position of the camera.
	 * @param posY The new y position of the camera.
	 */
	
	public void setPosition(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * Gets the x location of the camera.
	 */
	
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Gets the y location of the camera.
	 */
	
	public int getPosY() {
		return posY;
	}

}
