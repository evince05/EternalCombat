package gameobjects.projectiles;

import gameobjects.GameObject;
import gamestates.PlayState;
import gfx.Animation.AnimationDirection;

/**
 * A projectile is a GameObject which is always moving.
 * @author Elliott Vince
 */

public abstract class Projectile extends GameObject {

	private int velocity;
	
	/**
	 * Creates a new projectile at the pos (posX, posY).
	 * @param playState An instance of the PlayState class.
	 * @param posX The x location where the projectile should spawn.
	 * @param posY The y location where the projectile should spawn.
	 */
	
	public Projectile(PlayState playState, int posX, int posY) {
		// Calls the parent class's constructor.
		super(playState, posX, posY);
		
		// Sets the default velocity.
		velocity = 1;
	}
	
	/**
	 * Gets the velocity of the projectile. <br>
	 * The default velocity of the projectile is 1.
	 */
	
	public int getVelocity() {
		return velocity;
	}
	
	/**
	 * Sets the velocity of the projectile. <br>
	 * The default velocity of the projectile is 1.
	 * @param velocity The new velocity of the projectile.
	 */
	
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Updates the projectile.
	 */
	
	@Override
	public void update() {
		// Calls the parent class's update method.
		super.update();
		
		/*
		 * The projectile should only move in 1 direction at a time.
		 * This will move the animation accordingly in the proper direction.
		 */
		
		if (animation.getDirection() == AnimationDirection.UP) {
			// The projectile moves up (y position decreases)
			posY -= velocity;
		}
		else if (animation.getDirection() == AnimationDirection.LEFT) {
			// The projectile moves left (x position decreases)
			posX -= velocity;
		}
		else if (animation.getDirection() == AnimationDirection.DOWN) {
			// The projectile moves down (y position increases)
			posY += velocity;
		}
		else if (animation.getDirection() == AnimationDirection.RIGHT) {
			// The projectile moves right (x position increases)
			posX += velocity;
		}
	}
	
	/**
	 * This method is called whenever the projectile hits a gameobject.
	 */
	
	public abstract void onHit(GameObject object);

}
