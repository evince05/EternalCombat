package gameobjects.skeleton;

import java.awt.image.BufferedImage;
import java.util.Random;

import audio.MP3Player;
import gameobjects.LivingEntity;
import gameobjects.player.Player;
import gameobjects.powerups.Powerup;
import gameobjects.powerups.PowerupType;
import gamestates.PlayState;
import gfx.Animation;
import gfx.Animation.AnimationDirection;
import gfx.AnimationEvent;
import gfx.SpriteSheet;

/**
 * The Skeleton Knight is a stronger version of the skeleton
 * with more health and higher strength.
 * @author Elliott Vince
 */
public class SkeletonKnight extends LivingEntity {
	
	private static final int MAX_SCORE_PER_KILL = 50;
	
	// Spritesheets
	private SpriteSheet walkingSpriteSheet;
	private static final String WALKING_SPRITESHEET_PATH = 
			"sprites/skeleton/knight/walking.png";
	
	private SpriteSheet attackingSpriteSheet;
	private static final String ATTACKING_SPRITESHEET_PATH =
			"sprites/skeleton/knight/slash.png";
	
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
	
	// Attacking Animations
	private Animation attackingUp;
	private Animation attackingLeft;
	private Animation attackingDown;
	private Animation attackingRight;
	
	private AnimationDirection currentDirection;
	
	// The player is the gameobject's target.
	private Player target;
	
	// Used to determine if the skeleton is attacking.
	private boolean attacking;
	
	// Damage Amount
	private int damage;
	
	// Spawn Time.
	private long spawnTime;

	/**
	 * Creates a new Skeleton Knight.
	 * @param playState An instance of the PlayState class (used for pathfinding).
	 * @param posX The x position where the skeleton should spawn.
	 * @param posY The y position where the skeleton should spawn.
	 * @param health The health of the skeleton.
	 * @param maxHealth The max health of the skeleton.
	 */
	
	public SkeletonKnight(PlayState playState, int posX, int posY, int health, 
			int maxHealth) {
		super(playState, posX, posY, health, maxHealth);
		
		/*
		 * Initializes the spritesheets using the walking and attacking 
		 * spritesheets and the default GameObject size.
		 */
		
		walkingSpriteSheet = new SpriteSheet(WALKING_SPRITESHEET_PATH,
				64, 64);
		attackingSpriteSheet = new SpriteSheet(ATTACKING_SPRITESHEET_PATH,
				64, 64);
		super.setSpriteSheet(walkingSpriteSheet);
		
		// Initializes all of the skeleton's animation.
		initAnimations();
		
		// Sets the default animation.
		setAnimation(walkingRight);
		currentDirection = AnimationDirection.RIGHT;
		setDirection(currentDirection);
		
		// Sets the target of the skeleton.
		this.target = playState.getPlayer();
		attacking = false;
		
		// Sets the damage.
		damage = 32;
		
		speed = 1;
		
		/**
		 * Initializes the spawn time.
		 * This will be used for score per kill
		 * calculations.
		 */
		
		spawnTime = System.currentTimeMillis();
		
	}
	
	/**
	 * Initializes the animations.
	 */
	
	private void initAnimations() {
		
		// Initialze the idle animations
		
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
		
		// Initialize the walking animations
		walkingUp = new Animation(walkingSpriteSheet.getImages(0, 0, 8));
		walkingLeft = new Animation(walkingSpriteSheet.getImages(1, 0, 8));
		walkingDown = new Animation(walkingSpriteSheet.getImages(2, 0, 8));
		walkingRight = new Animation(walkingSpriteSheet.getImages(3, 0, 8));
		
		// Initialize the attacking animations
		attackingUp = new Animation(attackingSpriteSheet.getImages(0, 0, 5));
		attackingUp.setDelay(attackingUp.getDelay() / 2);
		
		attackingLeft = new Animation(attackingSpriteSheet.getImages(1, 0, 5));
		attackingLeft.setDelay(attackingLeft.getDelay() / 2);
		
		attackingDown = new Animation(attackingSpriteSheet.getImages(2, 0, 5));
		attackingDown.setDelay(attackingDown.getDelay() / 2);
		
		attackingRight = new Animation(attackingSpriteSheet.getImages(3, 0, 5));
		attackingRight.setDelay(attackingRight.getDelay() / 2);
		
		// Creates the damage animation event.
		AnimationEvent attackPlayer = new AnimationEvent() {

			@Override
			public void run() {
				target.damage(damage);
				
				// Skips to the next frame so it doesnt attack twice.
				animation.nextFrame();
				
				if (animation.hasPlayedOnce()) {
					setIdle();
					attacking = false;
				}
			}
			
		};
		
		/*
		 * Since each attack animation needs this event, 
		 * we can add it to each animation's AnimationEvent array.
		 */
		
		attackingUp.setAnimationEvent(6, attackPlayer);
		attackingLeft.setAnimationEvent(6, attackPlayer);
		attackingDown.setAnimationEvent(6, attackPlayer);
		attackingRight.setAnimationEvent(6, attackPlayer);
		
	}
	
	/**
	 * Updates the Skeleton Knight.
	 */
	
	@Override
	public void update() {
		super.update();
		
		
		
		if (target.getPosX() - posX >= -32 &&
				target.getPosX() - posX <= 32 &&
				target.getPosY() - posY >= -32 &&
				target.getPosY() - posY <= 32) {
			/*
			 * The skeleton is within attacking range
			 * of the player.
			 */
			if (!attacking) {
				if (target.isHitCooldownFinished()) {
					/* The player's hit cooldown has expired,
					 * so they can now be attacked.
					 */
					attack();
				}
				
			}
			
		}
		else {
			attacking = false;
			// The skeleton needs to move.
			
			/*
			 * Some basic pathfinding.
			 */
			
			if (target.getPosY() == posY) {
				// The skeleton is directly to the left or to the right of the player.
				
				if (target.getPosX() > posX) {
					// Skeleton needs to move right.
					
					setAnimation(walkingRight);
					currentDirection = AnimationDirection.RIGHT;
					posX += speed;
				}
				else if (target.getPosX() < posX) {
					// Skeleton needs to move left.
					
					setAnimation(walkingLeft);
					currentDirection = AnimationDirection.LEFT;
					posX -= speed;
				}
			}
			else if (target.getPosX() == posX) {
				// The skeleton is at directly in front of or behind the player. 
				
				if (target.getPosY() > posY) {
					// The skeleton needs to move down.
					
					setAnimation(walkingDown);
					currentDirection = AnimationDirection.DOWN;
					posY += speed;
				}
				else if (target.getPosY() < posY) {
					// The skeleton needs to move up.
					
					setAnimation(walkingUp);
					currentDirection = AnimationDirection.UP;
					posY -= speed;
				}
			}
			else {
				// The skeleton needs to move in both directions to reach the player.
				
				// The skeleton will first move in the direction that is closer to the player.
				
				/*
				 * Note: Math.abs() returns the absolute value of an integer.
				 * If the integer is negative, it will convert it to positive.
				 * Eg: Math.abs(26) returns 26.
				 *     Math.abs(-17) returns 17.
				 */
				
				int differenceX = Math.abs(posX - target.getPosX());
				int differenceY = Math.abs(posY - target.getPosY());
				
				if (differenceX > differenceY) {
					// The y location of the skeleton is closer to the player. 
					
					if (target.getPosY() > posY) {
						// The skeleton needs to move down.
						
						setAnimation(walkingDown);
						currentDirection = AnimationDirection.DOWN;
						posY += speed;
					}
					else if (target.getPosY() < posY) {
						// The skeleton needs to move up.
						
						setAnimation(walkingUp);
						currentDirection = AnimationDirection.UP;
						posY -= speed;
					}
					
					
				}
				else {
					
					/*
					 * Either the x location of the skeleton is closer to the player
					 * or both differenceX and differenceY are equal. In this case,
					 * the skeleton moves on the x axis first.
					 */
					
					if (target.getPosX() > posX) {
						// Skeleton needs to move right.
						
						setAnimation(walkingRight);
						currentDirection = AnimationDirection.RIGHT;
						posX += speed;
					}
					else if (target.getPosX() < posX) {
						// Skeleton needs to move left.
						
						setAnimation(walkingLeft);
						currentDirection = AnimationDirection.LEFT;
						posX -= speed;
					}
					
				}
			}
			
			// Updates the current direction variable.
			setDirection(currentDirection);
		}
		
		
	}
	
	/**
	 * Sets the current animation to the attack animation
	 * of whichever direction the skeleton is facing.
	 */
	
	private void attack() {
		
		// Plays the attacking sound.
		MP3Player audioPlayer = new MP3Player(playState.getGame(),
				"resources/audio/sfx/sword-attack.mp3");
		audioPlayer.play();
				
		// Sets the skeleton's direction.
				
		if (currentDirection == AnimationDirection.UP) {
			setAnimation(attackingUp);
			attacking = true;
		}
		else if (currentDirection == AnimationDirection.LEFT) {
			setAnimation(attackingLeft);
			attacking = true;
		}
		else if (currentDirection == AnimationDirection.DOWN) {
			setAnimation(attackingDown);
			attacking = true;
		}
		else if (currentDirection == AnimationDirection.RIGHT) {
			setAnimation(attackingRight);
			attacking = true;
		}
	}
	
	/**
	 * Sets the current animation to the idle animation
	 * of whichever direction the skeleton is facing.
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
	 * Handles the skeleton's death.
	 */
	
	@Override
	public void onDeath() {
		
		/**
		 * Increases the player's score.
		 * Every 30 seconds that the skeleton is alive,
		 * the score that will be gained decreases by 1.
		 */
		
		int score = MAX_SCORE_PER_KILL;
		
		long deathTime = System.currentTimeMillis();
		
		// Determines how many seconds have elapsed.
		int elapsedTime = (int) (deathTime - spawnTime) / 1000;
		int decrement = (int) (Math.round(elapsedTime / 30));
		
		// Decreases the score if the decrement is valid.
		if (decrement >= 1) {
			if (score - decrement >= 0) {
				score -= decrement;
			}
			else {
				score = 0;
			}
		}
		
		// Sets the player's score.
		
		int playerScore = playState.getPlayer().getScore();
		playState.getPlayer().setScore(playerScore + score);
		
		// Decreases the skeleton count by 1.
		playState.getLevelManager().onSkeletonDeath();
		
		// A strength powerup spawns with a 50% chance.
		Random random = new Random();

		int chance = random.nextInt(100) + 1;
		if (chance <= 50) {
			
			// Creates and spawns a strength powerup.
			Powerup strength = new Powerup(playState, posX + width / 2, posY + height - 16, 
					PowerupType.STRENGTH);
			playState.addPowerup(strength);

		}
		
	}
	
	

}

