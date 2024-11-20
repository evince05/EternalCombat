package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import gamestates.PlayState;
import gfx.Animation;
import gfx.Animation.AnimationDirection;
import gfx.SpriteSheet;

/**
 * A GameObject is an object that can be rendered to the
 * screen. It can move around the screen and it is a parent
 * class for many objects such as the Player and Skeletons.
 * @author Elliott Vince
 *
 */
public abstract class GameObject {
	
	// Graphics / Animation fields
	
	protected SpriteSheet spriteSheet;
	protected Animation animation;
	
	protected PlayState playState;
	
	// Location and size
	protected int posX;
	protected int posY;
	protected int width;
	protected int height;
	
	/**
	 * Creates a new game object.
	 * @param playState An instance of the PlayState class.
	 * @param posX The x position of the game object.
	 * @param posY The y position of the game object.
	 */
	
	public GameObject(PlayState playState, int posX, int posY) {
		this.playState = playState;
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * Gets the X position of the game object on the screen.
	 */
	
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Gets the Y position of the game object on the screen.
	 */
	
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Gets the height of the game object.
	 */
	
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the width of the game object.
	 */
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Sets the location of the game object.
	 * @param posX The gameobject's new x location.
	 * @param posY The gameobject's new y location.
	 */
	
	public void setLocation(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * Sets the SpriteSheet of the LivingEntity. <br> <br>
	 * <b><u>NOTE:</b></u> This <b>MUST</b> be called in the constructor <br>
	 * in order for the LivingEntity to be drawn to the screen.
	 * @param spriteSheet The new sprite sheet of the living entity.
	 */
	
	public void setSpriteSheet(SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
		this.width = spriteSheet.getWidth();
		this.height = spriteSheet.getHeight();
	}
	
	/**
	 * Gets the animation that is currently being displayed.
	 */
	
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * Sets the animation of the LivingEntity. <br> <br>
	 * <b><u>NOTE:</b></u> This <b>MUST</b> be called in the constructor of <br>
	 * the child class so it will specifiy which animation should be <br>
	 * used when the LivingEntity is first added to the screen. <br> <br>
	 * 
	 * This method is used to change the current animation that is playing. <br>
	 * For example, this is useful when a LivingEntity switches directions.
	 */
	
	protected void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	/**
	 * Gets the current direction that the animation is facing.
	 */
	
	protected AnimationDirection getDirection() {
		return animation.getDirection();
	}
	
	/**
	 * Sets the current direction that the animation is facing.
	 * @param direction The new direction that the animation is facing.
	 */
	
	protected void setDirection(AnimationDirection direction) {
		animation.setDirection(direction);
	}

	/**
	 * Updates the GameObject.
	 */
	
	public void update() {
		animation.update();
		
		/*
		 * Destroys the gameobject if it has moved 
		 * out of the camera bounds. This will reduce computer
		 * resource usage because the gameobject will not need to
		 * update or be drawn to the screen.
		 */
		
		Rectangle camBounds = playState.getCameraBounds();
		if (posX < camBounds.getMinX() - width ||
				posX > camBounds.getMaxX() + width ||
				posY < camBounds.getMinX() - height ||
				posY > camBounds.getMaxY() + height) {
			destroy();
		}
		
		
	}
	
	/**
	 * Draws the GameObject to the screen.
	 * @param graphics The graphics object which will be used.
	 */

	public void draw(Graphics2D graphics) {
		graphics.drawImage(animation.getImage(), posX - playState.getCamera().getPosX(), 
				posY - playState.getCamera().getPosY(), null);
	}
	
	/**
	 * Removes the gameobject from the game.
	 */
	
	public void destroy() {
		playState.removeGameObject(this);
	}
	
	/**
	 * Moves the GameObject up.
	 * @param speed The speed at which the GameObject should move.
	 * @return True If the object could move, false if it could not.
	 */
	
	public boolean moveUp(int speed) {
		if (posY - speed >= playState.getBounds().getMinY()) {
			posY -= speed;
			return true;
		}
		return false;
	}
	
	/**
	 * Moves the GameObject to the left.
	 * @param speed The speed at which the GameObject should move.
	 * @return True If the object could move, false if it could not.
	 */
	
	public boolean moveLeft(int speed) {
		if (posX - speed >= playState.getBounds().getMinX()) {
			posX -= speed;
			return true;
		}
		return false;
	}
	
	/**
	 * Moves the GameObject down.
	 * @param speed The speed at which the GameObject should move.
	 * @return True If the object could move, false if it could not.
	 */
	
	public boolean moveDown(int speed) {
		if (posY + speed <= playState.getBounds().getMaxY()) {
			posY += speed;
			return true;
		}
		return false;
	}
	
	/**
	 * Moves the GameObject to the right.
	 * @param speed The speed at which the GameObject should move.
	 * @return True If the object could move, false if it could not.
	 */
	
	public boolean moveRight(int speed) {
		if (posX + speed <= playState.getBounds().getMaxX()) {
			posX += speed;
			return true;
		}
		return false;
	}

}
