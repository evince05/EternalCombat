package gameobjects.player;

import java.awt.Color;
import java.awt.image.BufferedImage;

import audio.MP3Player;
import game.Game;
import gameobjects.LivingEntity;
import gameobjects.projectiles.Arrow;
import gamestates.DeathState;
import gamestates.PlayState;
import gfx.Animation;
import gfx.Animation.AnimationDirection;
import gfx.AnimationEvent;
import gfx.SpriteSheet;
import input.PlayerController;

/**
 * The player is equipped with a bow and arrow and is constantly
 * followed by hordes of skeletons. The program's user 
 * controls the player.
 * 
 * @author Elliott Vince
 */

public class Player extends LivingEntity {

	private PlayerController controller;
	
	private static final String WALKING_SPRITESHEET_PATH = 
			"sprites/player/walking.png";
	private SpriteSheet walkingSpriteSheet;
	
	private static final String BOW_SPRITESHEET_PATH = 
			"sprites/player/bow.png";
	private SpriteSheet bowSpriteSheet;
	
	// The default shooting delay (in milliseconds).
	public static final int DEFAULT_SHOOTING_DELAY = 50;
	
	// The default damage that the player deals.
	public static final int DEFAULT_DAMAGE = 20;
	
	// Idle Animations
	private Animation idleUp;
	private Animation idleLeft;
	private Animation idleDown;
	private Animation idleRight;
	
	// Walking Animations
	private Animation walkingUp;
	private Animation walkingLeft;
	private Animation walkingDown;
	private Animation walkingRight;
	
	// Shooting Animations
	private Animation shootingUp;
	private Animation shootingLeft;
	private Animation shootingDown;
	private Animation shootingRight;
	
	// Movement variables
	private boolean shooting;
	private boolean moving;
	
	// Damage
	private int damage;
	
	private AnimationDirection currentDirection;
	
	// Damage Cooldown Variables
	private double hitCooldown;
	private long timeSinceLastDamage;
	
	// Ammo Count
	private int ammoCount;
	private int maxAmmoCount;
	
	// Score
	private int score;

	/**
	 * Creates a new player object.
	 * @param playState An instance of the PlayState class.
	 * @param controller The player's controller (used for inputs and movement).
	 * @param posX The x location where the player should spawn.
	 * @param posY The y location where the player should spawn.
	 * @param health The amount of health which the player should start with.
	 * @param maxHealth The maximum amount of health which the player should have.
	 */
	
	public Player(PlayState playState, PlayerController controller, int posX, int posY, 
			int health, int maxHealth) {
		
		// Calls the parent class's constructor.
		super(playState, posX, posY, health, maxHealth);
		
		// Initializes the player's controller.
		this.controller = controller;
		
		// Initializes the sprite sheets.
		walkingSpriteSheet = new SpriteSheet(WALKING_SPRITESHEET_PATH, 64, 64);
		bowSpriteSheet = new SpriteSheet(BOW_SPRITESHEET_PATH, 64, 64);
		super.setSpriteSheet(walkingSpriteSheet);
		
		// Initalizes all of the player's animations.
		initAnimations();
		
		// Initializes and sets default animations / directions.
		setAnimation(idleRight);
		currentDirection = AnimationDirection.RIGHT;
		setDirection(currentDirection);
		
		// Sets the speed of the player to twice the default speed.
		speed = 2;
		moving = false;
		
		// Sets the player's score.
		score = 0;
		
		// Sets the damage that the player deals.
		damage = DEFAULT_DAMAGE;
		
		/*
		 * The default cooldown that must pass before 
		 * the player can be attacked again.
		 */
		
		hitCooldown = 0.5;
		
		// The time since the player was attacked
		timeSinceLastDamage = 0;
		
		// Sets the ammo that the player starts with.
		maxAmmoCount = 200;
		ammoCount = maxAmmoCount;
		
		// Set the color of the healthbar.
		healthbar.setForegroundColor(Color.GREEN);
	}
	
	/**
	 * Initializes all of the animations.
	 */
	
	private void initAnimations() {
		// Initializes the idle animations.
		
		idleUp = new Animation(new BufferedImage[] {
				walkingSpriteSheet.getImage(0, 0)
		});
		
		idleLeft = new Animation(new BufferedImage[] {
				walkingSpriteSheet.getImage(0, 1)
		});
		
		idleDown = new Animation(new BufferedImage[] {
				walkingSpriteSheet.getImage(0, 2)
		});
		
		idleRight = new Animation(new BufferedImage[] {
				walkingSpriteSheet.getImage(0, 3)
		});
		
		// Initializes the walking animations.
		
		walkingUp = new Animation(walkingSpriteSheet.getImages(0, 0, 8));
		walkingLeft = new Animation(walkingSpriteSheet.getImages(1, 0, 8));
		walkingDown = new Animation(walkingSpriteSheet.getImages(2, 0, 8));
		walkingRight = new Animation(walkingSpriteSheet.getImages(3, 0, 8));
		
		/*
		 * Initializes the shooting animations and sets 
		 * the events at the appropriate frames. 
		 */
		
		shootingUp = new Animation(bowSpriteSheet.getImages(0, 0, 12));
		shootingUp.setAnimationEvent(9, new AnimationEvent() {

			@Override
			public void run() {
				shootArrow();
			}
			
		});
		
		shootingUp.setDelay((long) (shootingUp.getDelay() / 1.5));
		
		
		shootingLeft = new Animation(bowSpriteSheet.getImages(1, 0, 12));
		shootingLeft.setAnimationEvent(9, new AnimationEvent() {

			@Override
			public void run() {
				shootArrow();			
			}
			
		});
		shootingLeft.setDelay((long) (shootingLeft.getDelay() / 1.5));
		
		shootingDown = new Animation(bowSpriteSheet.getImages(2, 0, 12));
		shootingDown.setAnimationEvent(9, new AnimationEvent() {

			@Override
			public void run() {
				shootArrow();
			}
			
		});
		shootingDown.setDelay((long) (shootingDown.getDelay() / 1.5));
		
		shootingRight = new Animation(bowSpriteSheet.getImages(3, 0, 12));
		shootingRight.setAnimationEvent(9, new AnimationEvent() {

			@Override
			public void run() {
				shootArrow();
			}
			
		});
		shootingRight.setDelay((long) (shootingRight.getDelay() / 1.5));
	}
	
	/**
	 * Updates the player.
	 */
	
	@Override
	public void update() {
		
		// Calls the parent class's update method.
		super.update();
		
		if (shooting) {
			if (animation.hasPlayedOnce()) {
				
				// Resets the shooting animation when it finishes.
				animation.reset();
				shooting = false;
				setIdle();
			}
		}
		else {
			// The player can move and shoot if they are not already shooting.
			
			if (controller.isRequestingUp()) {
				if (moveUp(speed)) {
					// Moves the player upwards.
					
					// Resets the last animation if the animation changes.
					resetAnimation(animation, walkingUp);
					
					// Changes the animation.
					setAnimation(walkingUp);
					currentDirection = AnimationDirection.UP;
					moving = true;
				}

			}
			else if (controller.isRequestingLeft()) {
				if (moveLeft(speed)) {
					// Moves the player to the left.
					
					// Resets the last animation if the animation changes.
					resetAnimation(animation, walkingLeft);
					
					// Changes the animation.
					setAnimation(walkingLeft);
					currentDirection = AnimationDirection.LEFT;
					moving = true;
				}
			}
			else if (controller.isRequestingDown()) {
				if (moveDown(speed)) {
					// Moves the player downwards.
					
					// Resets the last animation if the animation changes.
					resetAnimation(animation, walkingDown);
					
					// Changes the animation.
					setAnimation(walkingDown);
					currentDirection = AnimationDirection.DOWN;
					moving = true;
				}
			}
			else if (controller.isRequestingRight()) {
				if (moveRight(speed)) {
					// Moves the player to the right.
					
					// Resets the last animation if the animation changes.
					resetAnimation(animation, walkingRight);
					
					// Changes the animation.
					setAnimation(walkingRight);
					currentDirection = AnimationDirection.RIGHT;
					moving = true;
				}
			}
			else {
				// If the player is not moving, they become idle.
				moving = false;
				if (!shooting) {
					// Resets the animation and makes the player idle.
					animation.reset();
					setIdle();
					
				}
			}

			// Handles arrow shooting.
			if (controller.isRequestingLeftClick()) {
				if (!moving && !shooting) {

					/*
					 * Only shoots if the player is not moving and they 
					 * aren't in the middle of shooting.
					 */

					/*
					 * Also, the player will only shoot if they have ammo.
					 * Note: The ammo is decreased once 
					 */

					if (ammoCount > 0) {
						
						// Sets the shooting animation.
						if (currentDirection == AnimationDirection.UP) {
							setAnimation(shootingUp);
							shooting = true;
						}
						else if (currentDirection == AnimationDirection.LEFT) {
							setAnimation(shootingLeft);
							shooting = true;
						}
						else if (currentDirection == AnimationDirection.DOWN) {
							setAnimation(shootingDown);
							shooting = true;
						}
						else if (currentDirection == AnimationDirection.RIGHT) {
							setAnimation(shootingRight);
							shooting = true;
						}
					}
				}
			}
		}
		
		// Sets the player's direction.
		setDirection(currentDirection);
	}
	
	/**
	 * Sets the animation to the idle animation of
	 * whichever direction the player is facing.
	 */
	
	private void setIdle() {
		
		if (currentDirection == AnimationDirection.UP) {
			setAnimation(idleUp);
		}
		else if (currentDirection == AnimationDirection.LEFT) {
			setAnimation(idleLeft);
		}
		else if (currentDirection == AnimationDirection.DOWN) {
			setAnimation(idleDown);
		}
		else if (currentDirection == AnimationDirection.RIGHT) {
			setAnimation(idleRight);
		}
	}
	
	/**
	 * Shoots an arrow.
	 */
	
	private void shootArrow() {
		
		// Only shoots if the player has enough ammo.
		
		/* 
		 * Creates the arrow, but sets the appropriate 
		 * location before adding it to the game.
		 */
			
		Arrow arrow = new Arrow(playState, this, 0, 0, currentDirection);
		// Sets the arrow's damage.
		arrow.setDamage(damage);
				
		if (currentDirection == AnimationDirection.UP) {
			arrow.setLocation(posX + 28, posY - 10);
		}
		else if (currentDirection == AnimationDirection.LEFT) {
			arrow.setLocation(posX - 32, posY + 29);
		}
		else if (currentDirection == AnimationDirection.DOWN) {
			arrow.setLocation(posX + 28, posY + 10);
		}
		else if (currentDirection == AnimationDirection.RIGHT) {
			arrow.setLocation(posX + 32, posY + 29);
		}
		
		// Adds the arrow to the playstate.
		playState.addGameObject(arrow);
		
		// Plays the shooting sound effect.
		MP3Player audioPlayer = new MP3Player(playState.getGame(), 
				"resources/audio/sfx/shoot-arrow.mp3");
		audioPlayer.play();
			
		/*
		 * Moves the animation to the next frame.
		 * This prevents multiple arrows being added at once.
		 */
			
		animation.nextFrame();
			
		// Subtracts 1 from the ammoCount.
		ammoCount--;
	}
	
	/**
	 * Overrides the damage method so the player's cooldown timer can
	 * be implemented.
	 */
	
	@Override
	public void damage(int amount) {
		super.damage(amount);
		
		// Sets the current time as the time since last damage.
		timeSinceLastDamage = System.currentTimeMillis();
	}
	
	/**
	 * Gets the amount of time during which the player <br>
	 * cannot be attacked. While this is active, the player <br>
	 * is temporarily invincible.
	 */
	
	public double getHitCooldown() {
		return hitCooldown;
	}
	
	/**
	 * Determines if the hit cooldown is finished.
	 * @return true If the hit cooldown is finished, false if otherwise.
	 */
	
	public boolean isHitCooldownFinished() {
		return timeSinceLastDamage + hitCooldown * 1000 < System.currentTimeMillis();
	}
	
	/**
	 * Sets the amount of time during which the player <br>
	 * cannot be attcked. While this is active, the player <br>
	 * is temporarily invincible.
	 * @param cooldown The new hit cooldown.
	 */
	
	public void setHitCooldown(double cooldown) {
		this.hitCooldown = cooldown;
	}
	
	/**
	 * Sets the player's health to their max health.
	 */
	
	public void setMaxHealth() {
		this.health = maxHealth;
	}
	
	/**
	 * Gets the player's ammo count.
	 */
	
	public int getAmmo() {
		return ammoCount;
	}
	
	/**
	 * Gets the player's maximum ammo count.
	 */
	
	public int getMaxAmmo() {
		return maxAmmoCount;
	}
	
	/**
	 * Sets the player's max ammo count.
	 * @param maxAmmoCount The player's new max ammo count.
	 */
	
	public void setMaxAmmo(int maxAmmoCount) {
		this.maxAmmoCount = maxAmmoCount;
	}
	
	/**
	 * Refills the player's ammo.
	 */
	
	public void refillAmmo() {
		this.ammoCount = maxAmmoCount;
	}
	
	/**
	 * Gets the player's score.
	 */
	
	public int getScore() {
		return score;
	}
	
	/**
	 * Sets the player's score.
	 * @param score The player's new score.
	 */
	
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Sets the player's attack speed.
	 * @param attackSpeed The player's new attack speed (in milliseconds).
	 */
	
	public void setAttackSpeed(int attackSpeed) {
		// Updates the delays of the shooting animations.
		shootingUp.setDelay((long) attackSpeed);
		shootingLeft.setDelay((long) attackSpeed);
		shootingDown.setDelay((long) attackSpeed);
		shootingRight.setDelay((long) attackSpeed);
	}
	
	/**
	 * Sets the amount of damage that the player deals.
	 * @param damage The amount of damage that the player should deal.
	 */
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	/**
	 * Resets the specified animation if it is not equal to the
	 * other specified animation.
	 * @param lastAnimation The animation to be reset.
	 * @param nextAnimation The new animation.
	 */
	
	public void resetAnimation(Animation lastAnimation, 
			Animation nextAnimation) {
		
		if (!lastAnimation.equals(nextAnimation)) {
			lastAnimation.reset();
		}
	}
	
	/**
	 * Handles the player's death. In this case, the death menu appears
	 * and the game ends.
	 */
	
	@Override
	public void onDeath() {
		Game game = playState.getGame();
		
		// Stops the soundtrack player.
		game.getSoundtrackPlayer().stop();
		
		// Plays the death sound.
		MP3Player soundPlayer = new MP3Player(playState.getGame(),
				"resources/audio/music/gameover.mp3");
		soundPlayer.play();
		
		// Opens the death state.
		game.setCurrentState(new DeathState(playState, game, game.getInput()));
		
	}
		
}