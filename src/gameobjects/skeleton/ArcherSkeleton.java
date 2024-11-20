package gameobjects.skeleton;

import java.awt.image.BufferedImage;
import java.util.Random;

import audio.MP3Player;
import gameobjects.LivingEntity;
import gameobjects.player.Player;
import gameobjects.powerups.Powerup;
import gameobjects.powerups.PowerupType;
import gameobjects.projectiles.Arrow;
import gamestates.PlayState;
import gfx.Animation;
import gfx.AnimationEvent;
import gfx.SpriteSheet;
import gfx.Animation.AnimationDirection;

/**
 * The Archer Skeleton is a skeleton equipped with a bow and arrow.
 * It follows and shoots the player from range.
 * @author Elliott Vince
 */

public class ArcherSkeleton extends LivingEntity {

	// Spritesheets.
	private SpriteSheet walkingSpriteSheet;
	private static final String WALKING_SPRITESHEET_PATH = 
			"sprites/skeleton/archer/walking.png";
	
	private SpriteSheet shootingSpriteSheet;
	private static final String SHOOTING_SPRITESHEET_PATH =
			"sprites/skeleton/archer/shooting.png";
	
	private static final int MAX_SCORE_PER_KILL = 25;
	
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
	
	private AnimationDirection currentDirection;
	
	// The player is the gameobject's target.
	private Player target;
	
	// Movement variables
	private boolean shooting;
	
	private long spawnTime;
	
	/**
	 * Creates a new skeleton archer.
	 * @param playState An instance of the PlayState class.
	 * @param posX The x position where the skeleton should spawn.
	 * @param posY The y position where the skeleton should spawn.
	 * @param health The starting health of the skeleton.
	 * @param maxHealth The maximum health of the skeleton.
	 */
	
	public ArcherSkeleton(PlayState playState, int posX, int posY, int health, int maxHealth) {
		super(playState, posX, posY, health, maxHealth);
		
		/*
		 * Initializes the spritesheets using the walking and shooting 
		 * spritesheets and the default GameObject size.
		 */
		
		walkingSpriteSheet = new SpriteSheet(WALKING_SPRITESHEET_PATH,
				64, 64);
		shootingSpriteSheet = new SpriteSheet(SHOOTING_SPRITESHEET_PATH,
				64, 64);
		
		super.setSpriteSheet(walkingSpriteSheet);
		
		// Initializes all the animations.
		initAnimations();
		
		// Initilaizes the default animations / directions.
		setAnimation(walkingRight);
		currentDirection = AnimationDirection.RIGHT;
		setDirection(currentDirection);
		
		// Sets the target of the skeleton.
		this.target = playState.getPlayer();
		shooting = false;
		
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

		// Initialize the shooting animations
		shootingUp = new Animation(shootingSpriteSheet.getImages(0, 0, 12));

		shootingLeft = new Animation(shootingSpriteSheet.getImages(1, 0, 12));

		shootingDown = new Animation(shootingSpriteSheet.getImages(2, 0, 12));

		shootingRight = new Animation(shootingSpriteSheet.getImages(3, 0, 12));

		// Creates the damage animation event.
		AnimationEvent shootArrow = new AnimationEvent() {

			@Override
			public void run() {
				spawnArrow();

				// Skips to the next frame so it doesnt attack twice.
				animation.nextFrame();
			}

		};

		/*
		 * Since each attack animation needs this event, 
		 * we can add it to each animation's AnimationEvent array.
		 */

		shootingUp.setAnimationEvent(9, shootArrow);
		shootingLeft.setAnimationEvent(9, shootArrow);
		shootingDown.setAnimationEvent(9, shootArrow);
		shootingRight.setAnimationEvent(9, shootArrow);

	}
	
	/**
	 * Updates the archer skeleton.
	 */
	@Override
	public void update() {
		super.update();
		
		if (shooting) {
			if (animation.hasPlayedOnce()) {
				
				// Resets the shooting animation when it finishes.
				animation.reset();
				setIdle();
				shooting = false;
			}
		}
		else {
			
			// Shoots an arrow if the player is within attacking range.
			
			if (target.getPosX() == posX) {
				
				// The skeleton must be within range to shoot.
				if (target.getPosY() - posY >= -(64 * 5) &&
						target.getPosY() - posY <= 64 * 5) {
					
					/*
					 * The skeleton is within attacking range so it
					 * shoots an arrow.
					 */
					
					shoot();
				}
			}
			else if (target.getPosY() == posY) {
				
				// The skeleton must be within range to shoot.
				if (target.getPosX() - posX >= -(64 * 5) &&
						target.getPosX() - posX <= 64 * 5) {
					/*
					 * The skeleton is within attacking range so it
					 * shoots an arrow.
					 */
					shoot();
				}
			}
			
			if (!shooting) {
				
				// The skeleton can only move when it is not shooting.

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

					
					/*
					 * Calculates the difference between the player's location
					 * and the archer's location.
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
			}
		}
		
		// Updates the current direction variable.
		setDirection(currentDirection);
	}
	
	/**
	 * Sets the current animation to the shooting animation
	 * of whichever direction the skeleton is facing.
	 */
	
	private void shoot() {
		
		if (target.isHitCooldownFinished()) {
			/* The player's hit cooldown has expired,
			 * so they can now be attacked.
			 */
			
			// Turns the skeleton towards the player.
			
			if (target.getPosX() > posX) {
				// Turn to the right.
				currentDirection = AnimationDirection.RIGHT;
			}
			else if (target.getPosX() < posX) {
				// Turn to the left.
				currentDirection = AnimationDirection.LEFT;
			}
			else if (target.getPosY() > posY) {
				// Turn downwards.
				currentDirection = AnimationDirection.DOWN;
			}
			else if (target.getPosY() < posY) {
				// Turn upwards.
				currentDirection = AnimationDirection.UP;
			}
			
			// Switches to the correct animation and shoots the arrow.
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
	
	/**
	 * Spawns an arrow.
	 */
	
	private void spawnArrow() {
		
		/* 
		 * Creates the arrow, but sets the appropriate 
		 * location before adding it to the game.
		 */
			
		Arrow arrow = new Arrow(playState, this, 0, 0, currentDirection);
				
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
		playState.addGameObject(arrow);
		
		// Plays the shooting sound effect.
		MP3Player audioPlayer = new MP3Player(playState.getGame(),
				"resources/audio/sfx/shoot-arrow.mp3");
		audioPlayer.play();
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
		
		// A max ammo powerup spawns with a 50% chance.
		Random random = new Random();
		
		int chance = random.nextInt(100) + 1;
		if (chance <= 50) {
			// Creates and spawns a max ammo powerup.
			Powerup maxAmmo = new Powerup(playState, posX + width / 2, posY + height - 16, 
					PowerupType.MAX_AMMO);
			playState.addPowerup(maxAmmo);
			
		}
		
	}

}
