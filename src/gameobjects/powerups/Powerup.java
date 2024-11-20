package gameobjects.powerups;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameobjects.player.Player;
import gamestates.PlayState;

/**
 * Powerups will be used to help the player survive. <br>
 * They have a chance of spawning when a skeleton is killed. <br>
 * Types of powerups: <br> <br>
 * Haste - decreases attack cooldown by 2x for 10 seconds. <br>
 * Healing - gives the player max health. <br>
 * Refill - gives the player max ammo. <br>
 * Strength - increases damage by 2x for 10 seconds.
 * 
 * @author Elliott Vince
 */
public class Powerup {

	// How much time the powerup will be spawned before it disappears.
	private static final int MAX_SPAWN_TIME = 40;
	
	private PlayState playState;
	private Player player;
	private PowerupType powerupType;
	
	private boolean active;
	
	private BufferedImage image;
	
	
	// Location / Position Variables
	private int posX;
	private int posY;
	private int width;
	private int height;
	
	// Timer Variables
	private long startTime;
	private long spawnTime;
	
	/**
	 * Creates a new powerup.
	 * @param playState An instance of the PlayState class.
	 * @param posX The x location where the powerup should spawn.
	 * @param posY The y location where the powerup should spawn.
	 * @param type The type of powerup that should spawn.
	 */
	
	public Powerup(PlayState playState, int posX, int posY, PowerupType type) {
		// Initializes the play state.
		this.playState = playState;
		
		// Sets the location of the powerup.
		this.posX = posX;
		this.posY = posY;
		
		// Sets the powerup's type.
		this.powerupType = type;
		
		// Initializes the spawn time.
		spawnTime = System.currentTimeMillis();
		
		// Initializes the powerup.
		init();
	}
	
	/**
	 * Initializes any of the variables needed.
	 */
	
	public void init() {
		// Sets the player to the player in the playstate. 
		this.player = playState.getPlayer();
		
		// Initializes the image of the powerup.
		
		try {
			if (powerupType == PowerupType.HASTE) {
				image = ImageIO.read(new File
						("resources/sprites/powerups/hastepotion.png"));
			}
			else if (powerupType == PowerupType.MAX_AMMO) {
				image = ImageIO.read(new File
						("resources/sprites/powerups/maxammo.png"));
			}
			else if (powerupType == PowerupType.MAX_HEALTH) {
				image = ImageIO.read(new File
						("resources/sprites/powerups/maxhealth.png"));
			}
			else if (powerupType == PowerupType.STRENGTH) {
				image = ImageIO.read(new File
						("resources/sprites/powerups/strengthpotion.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Sets the width and height of the powerup.
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	/**
	 * Updates the powerup.
	 */

	public void update() {
		
		if (!active) {
			// Gets the player's bounds.
			Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(),
					player.getWidth(), player.getHeight());
			
			// Gets the powerup's bounds.
			Rectangle powerupRect = new Rectangle(posX, posY, width, height);
			
			// Applies the powerup if the player collides with it.
			if (playerRect.intersects(powerupRect)) {
				// Applies the powerup to the player.
				active = true;
				applyPowerup();
			}
		}
		else {
			// Removes the powerup if it's timer has finished.
			
			if (powerupType.hasDuration()) {
				int elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
				
				if (elapsedTime >= powerupType.getDuration()) {
					// The timer has expired, so the powerup is removed.
					removePowerup();
					playState.removePowerup(this);
					
				}
			}
			else {
				// Removes the powerup if it is an instant powerup.
				playState.removePowerup(this);
			}
		}
		
		// Removes the powerup if its spawn time has elapsed.
		int elapsedTime = (int) (System.currentTimeMillis() - spawnTime) / 1000;
		if (elapsedTime >= MAX_SPAWN_TIME) {
			playState.removePowerup(this);
		}
		
	}
	
	/**
	 * Draws the powerup to the screen.
	 */
	
	public void draw(Graphics2D graphics) {
		if (!active) {
			graphics.drawImage(image, posX - playState.getCamera().getPosX(), 
					posY - playState.getCamera().getPosY(), null);
		}
	}
	
	/**
	 * Sets the time (in milliseconds) since the last spawn occured. <br>
	 * <b><u>NOTE:</b></u> This should only be used while the game is paused.
	 */
	
	public void updateSpawnTime() {
		this.spawnTime = System.currentTimeMillis() - spawnTime;
	}
	
	/**
	 * Applies the powerup to the player.
	 */
	
	private void applyPowerup() {
		int duration = powerupType.getDuration();
		
		if (duration > 0) {
			// Starts the timer if the powerup has a duration.
			startTime = System.currentTimeMillis();
		}
		
		if (powerupType == PowerupType.HASTE) {
			player.setAttackSpeed(Player.DEFAULT_SHOOTING_DELAY / 2);
		}
		else if (powerupType == PowerupType.MAX_HEALTH) {
			player.setMaxHealth();
		}
		else if (powerupType == PowerupType.MAX_AMMO) {
			player.refillAmmo();
		}
		else if (powerupType == PowerupType.STRENGTH) {
			player.setDamage(Player.DEFAULT_DAMAGE * 2);
		}
	}
	
	/**
	 * Removes the powerup from the player.
	 */
	
	private void removePowerup() {
		// Only the powerups with durations can be reset.
		if (powerupType == PowerupType.HASTE) {
			// Resets the player's attack speed to its default value.
			player.setAttackSpeed(Player.DEFAULT_SHOOTING_DELAY);
		}
		else if (powerupType == PowerupType.STRENGTH) {
			// Resets the player's damage to its default value.
			player.setDamage(Player.DEFAULT_DAMAGE);
		}
	}
	
	/**
	 * Gets the width of the powerup's image.
	 */
	
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the powerup's image.
	 */
	
	public int getHeight() {
		return height;
	}
	
	
}
