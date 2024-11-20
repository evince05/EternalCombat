package gameobjects.projectiles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.MP3Player;
import gameobjects.GameObject;
import gameobjects.LivingEntity;
import gamestates.PlayState;
import gfx.Animation;
import gfx.Animation.AnimationDirection;
import gfx.SpriteSheet;

/**
 * A projectile that deals damage when it interacts with a LivingEntity.
 * @author Elliott Vince
 */

public class Arrow extends Projectile {
	
	private LivingEntity source;
	/*
	 * Horizontal and vertical arrows are stored on different spritesheets.
	 * arrowSpriteSheetUD: Up and Down arrows.
	 * arrowSpriteSheetLR: Left and Right arrows.
	 */
	private SpriteSheet arrowSpriteSheetUD;
	private SpriteSheet arrowSpriteSheetLR;
	
	private static final String UD_SPRITESHEET_PATH =
			"sprites/projectiles/arrow-vertical.png";
	private static final String LR_SPRITESHEET_PATH =
			"sprites/projectiles/arrow-horizontal.png";

	private int damage;
	
	/**
	 * Creates an arrow at the location (posX, posY).
	 * 
	 * @param playState An instance of the PlayState class.
	 * @param source The LivingEntity that shot the arrow.
	 * @param posX The x position where the arrow should spawn.
	 * @param posY The y position where the arrow should spawn.
	 * @param direction The direction which the arrow is facing.
	 */
	
	public Arrow(PlayState playState, LivingEntity source, int posX, int posY,
			AnimationDirection direction) {
		super(playState, posX, posY);
		
		// Initialize the source
		this.source = source;
		/*
		 * Initialize the spritesheets.
		 * Since the arrows are rectangular, using a simple square cell
		 * won't work. Therefore, different dimensions must be used.
		 */
		
		arrowSpriteSheetUD = new SpriteSheet(UD_SPRITESHEET_PATH, 6, 30);
		arrowSpriteSheetLR = new SpriteSheet(LR_SPRITESHEET_PATH, 30, 6);
		
		/*
		 * Sets the spritesheet to the spritesheet with the arrows that belong to
		 * the animation direction.
		 */
		
		if (direction == AnimationDirection.DOWN ||
				direction == AnimationDirection.UP) {
			
			// Needs up/down arrow spritesheet.
			setSpriteSheet(arrowSpriteSheetUD);
		}
		else if (direction == AnimationDirection.LEFT ||
				direction == AnimationDirection.RIGHT) {
			
			// Needs left/right arrow spritesheet.
			setSpriteSheet(arrowSpriteSheetLR);
		}
		
		// Set the default animation
		setAnimation(new Animation(new BufferedImage[] {
				(spriteSheet.getImage(0, 0))
		}));
		
		// Set the direction of the arrow.
		setDirection(direction);
		
		// Set the default damage to 20.
		this.damage = 20;
		
		// Set the default velocity to 6.
		this.setVelocity(6);
	}
	
	/**
	 * Gets the damage of the arrow. <br>
	 * The default damage is 20.
	 */
	
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Sets the damage of the arrow. <br>
	 * For reference, the default damage is 20.
	 * @param damage The new damage of the arrow.
	 */
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * This method is called when the arrow collides with a GameObject.
	 * @param object The GameObject which the arrow collided with.
	 */
	
	@Override
	public void onHit(GameObject object) {
		if (object instanceof LivingEntity && !object.equals(source)) {
			
			// Plays the hit sound effect.
			MP3Player audioPlayer = new MP3Player(playState.getGame(),
					"resources/audio/sfx/arrow-hit.mp3");
			audioPlayer.play();
			
			// Damages the entity and removes the projectle.
			LivingEntity entity = (LivingEntity) object;
			entity.damage(damage);
			destroy();
		}
	}
	
	/**
	 * Sets the direction of the arrow.
	 * @param direction The direction which the arrow should move in.
	 */
	
	@Override
	protected void setDirection(AnimationDirection direction) {
		// Calls the parent class's setDirection method.
		super.setDirection(direction);
		
		// Sets the direction of the arrow and updates the spritesheet.
		if (direction == AnimationDirection.UP) {
			setSpriteSheet(arrowSpriteSheetUD);
			
			// Set the animation to the up arrow sprite.
			setAnimation(new Animation(new BufferedImage[] {
				spriteSheet.getImage(1, 0)
			}, direction));
		}
		else if (direction == AnimationDirection.LEFT) {
			setSpriteSheet(arrowSpriteSheetLR);
			
			// Set the animation to the down arrow sprite.
			setAnimation(new Animation(new BufferedImage[] {
					spriteSheet.getImage(0, 0)
			}, direction));
		}
		else if (direction == AnimationDirection.DOWN) {
			setSpriteSheet(arrowSpriteSheetUD);
			
			// Set the animation to the down arrow sprite.
			setAnimation(new Animation(new BufferedImage[] {
					spriteSheet.getImage(0, 0)
			}, direction));
		}
		else if (direction == AnimationDirection.RIGHT) {
			setSpriteSheet(arrowSpriteSheetLR);
			
			// Set the animation to the right arrow sprite.
			setAnimation(new Animation(new BufferedImage[] {
					spriteSheet.getImage(0, 1)
			}, direction));
		}
	}
	
	/**
	 * Sets the location of the top left corner of the arrow.
	 * @param xPos The x position of the top left corner.
	 * @param yPos the y position of the top left corner.
	 */
	
	public void setLocation(int xPos, int yPos) {
		this.posX = xPos;
		this.posY = yPos;
	}
	
	/**
	 * Updates the arrow.
	 */
	
	@Override
	public void update() {
		// Calls the parent class's update method.
		super.update();
		
		/*
		 * Overrides the gameobjects update method.
		 * Every time this method is called, it will:
		 * - Move the arrow (Handled in Projectile class).
		 * - Detect and handle collisions with any of the gameobjects.
		 */
		
		Rectangle arrowRect = new Rectangle(posX, posY, width, height);
		
		// Iterates through each game object.
		for (GameObject gameObject : playState.getGameObjects()) {
			Rectangle gameObjectRect = new Rectangle(gameObject.getPosX(),
					gameObject.getPosY(), gameObject.getWidth(), gameObject.getHeight());
			
			if (arrowRect.intersects(gameObjectRect)) {
				
				/*
				 * The arrow rectangle is touching some part of the 
				 * game object's rectangle, which means they are colliding.
				 */
				
				onHit(gameObject);
				break; // Exits the for loop.
			}
		}
	}
}
