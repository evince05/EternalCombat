package levels;

import java.awt.Rectangle;
import java.util.Random;

import gameobjects.LivingEntity;
import gameobjects.skeleton.ArcherSkeleton;
import gameobjects.skeleton.Skeleton;
import gameobjects.skeleton.SkeletonKnight;
import gamestates.PlayState;

/**
 * The LevelManager will be used to handle skeleton spawning and other
 * level-specific functions throughout the game.
 * 
 * @author Elliott Vince
 */

public class LevelManager {
	
	private PlayState playState;
	
	// Level Variables and Multipliers
	private static final double SPAWN_INTERVAL_DECREMENT = 0.375;
	private static final double TOTAL_SKELETON_INCREMENENT = 2;
	private static final int FINAL_UPDATE_LEVEL = 20;
	
	private int level;
	
	private int totalSkeletons;
	private int remainingSkeletons;
	private int aliveSkeletons;
	
	private long lastSpawnTime;
	
	private double spawnInterval;
	
	// Random variable (used for spawn locations)
	private Random random;
	
	/**
	 * Create a LevelManager object. This will be initialized <br>
	 * at level 1 with a total skeleton count of 10. <br>
	 * As the levels increase, an increasing amount of skeletons <br>
	 * will spawn.
	 * 
	 * @param playState An instance of the PlayState class.
	 */
	
	public LevelManager(PlayState playState) {
		this.playState = playState;
		
		// The default fields for level 1 are initialized.
		this.level = 1;
		this.totalSkeletons = 5;
		this.remainingSkeletons = totalSkeletons;
		this.aliveSkeletons = totalSkeletons;
		
		this.spawnInterval = 7;
		
		this.lastSpawnTime = System.currentTimeMillis();
		
		// Creates the random object (used for spawn locations).
		this.random = new Random();
	}
	
	/**
	 * Updates the current level.
	 */
	
	public void update() {
		
		/*
		 * Sets the next level if all of the skeletons in the level
		 * have been killed.
		 */
		
		if (aliveSkeletons == 0) {
			setNextLevel();
			return;
		}
		
		/*
		 * Checks if the lastSpawnTime + spawnInterval 
		 * (converted to milliseconds) is less than the current
		 * time.
		 */
		
		if (lastSpawnTime + spawnInterval * 1000.0 < 
				System.currentTimeMillis()) {
			
			if (remainingSkeletons > 0) {
				
				/*
				 * Spawns a different number of skeletons based on the level.
				 * Note: If the specified amount of skeletons cannot be spawned,
				 * the remaining amount of skeletons will be spawned.
				 */
				
				if (level <= 9) {
					spawnSkeletons(1);
				}
				else if (level <= 19) {
					spawnSkeletons(2);
				}
				else if (level >= 20) {
					spawnSkeletons(3);
				}
				
			}
			
			// Sets the last spawn time to the current time.
			lastSpawnTime = System.currentTimeMillis();
			
		}
		
	}
	
	/**
	 * Gets the current level of the game.
	 */
	
	public int getCurrentLevel() {
		return level;
	}
	
	/**
	 * Gets the time (in seconds) that will elapse between
	 * each skeleton spawning period.
	 */
	
	public double getSpawnInterval() {
		return spawnInterval;
	}
	
	/**
	 * Sets the time (in seconds) that will elapse between each
	 * skeleton spawning period.
	 * @param spawnInterval The new time (in seconds) between
	 * each skeleton spawning period.
	 */
	
	public void setSpawnInterval(double spawnInterval) {
		this.spawnInterval = spawnInterval;
	}
	
	/**
	 * Sets the time (in milliseconds) since the last spawn occured. <br>
	 * <b><u>NOTE:</b></u> This should only be used while the game is paused.
	 */
	
	public void updateLastSpawnTime() {
		this.lastSpawnTime = System.currentTimeMillis() - lastSpawnTime;
	}
	
	/**
	 * Gets how many skeletons will be spawned 
	 * during the current level.
	 */
	
	public int getTotalSkeletons() {
		return totalSkeletons;
	}
	
	/**
	 * Gets the remaining amount of skeletons
	 * to be spawned during the current level.
	 */
	
	public int getRemainingSkeletons() {
		return remainingSkeletons;
	}
	
	/**
	 * Gets the amount of skeletons that have yet
	 * to been killed during the current level.
	 */
	
	public int getAliveSkeletons() {
		return aliveSkeletons;
	}
	/**
	 * Spawns the specified amount of skeletons at random locations. <br>
	 * If the specified amount of skeletons cannot be spawned, <br>
	 * the maximum number of skeletons that can be spawned will spawn.
	 * @param amount The amount of skeletons to be spawned.
	 */
	
	private void spawnSkeletons(int amount) {
		
		/*
		 * Sets the amount to however many skeletons are remaining 
		 * if the specified amount cannot be spawned. 
		 */
		if (amount >= remainingSkeletons || remainingSkeletons - amount <= 0) {
			amount = remainingSkeletons;
		}
		
		// Gets the map bounds from the PlayState.
		Rectangle mapBounds = playState.getBounds();
		
		// Iterates through each skeleton that should be spawned.
		for (int count = 0; count < amount; count++) {
			
			// Gets a random x coordinate from within the map bounds.
			int posX = random.nextInt((int) (mapBounds.getMaxX() - 
					mapBounds.getMinX() + 1)) + (int) mapBounds.getMinX() - 64;
			
			// Gets a random y coordinate from within the map bounds.
			int posY = random.nextInt((int) (mapBounds.getMaxY() - 
					mapBounds.getMinY() + 1)) + (int) mapBounds.getMinY() - 64;
			
			/**
			 * Spawns a specific type of skeleton using a chance system.
			 * Note:
			 * Archers are introduced after level 3
			 * Knights are introduced after level 5.
			 */
			
			Random generator = new Random();
			int chance = generator.nextInt(100) + 1;
			
			// Cleaned up the spawning code a bit.
			LivingEntity enemy = null;
			
			if (level >= 5 && chance <= 15) {
				enemy = new SkeletonKnight(playState, posX, posY, 250, 250);
			}
			else if (level >= 5 && chance <= 45 || level >= 3 && chance <= 30) {
				enemy = new ArcherSkeleton(playState, posX, posY, 150, 150);
			}
			else {
				enemy = new Skeleton(playState, posX, posY, 100, 100);
			}
			
			playState.addGameObject(enemy);
			remainingSkeletons--;		
		}
	}
	
	/**
	 * Sets the current level to the next level. <br>
	 * Throughout each level, the level variables change. <br> 
	 * The level variables will stop updating after level 20.
	 */
	
	private void setNextLevel() {
		level++;
		
		if (level <= FINAL_UPDATE_LEVEL) {
			// Increases the total skeleton count by 10.
			totalSkeletons += TOTAL_SKELETON_INCREMENENT;
			
			// Sets the remaining skeletons to the total skeletons.
			remainingSkeletons = totalSkeletons;
			
			// Sets the alive skeletons to the total skeletons.
			aliveSkeletons = totalSkeletons;
			
			// Decreases the spawn interval by 0.375.
			spawnInterval -= SPAWN_INTERVAL_DECREMENT;
		}
		
		// Resets the spawn timer.
		lastSpawnTime = System.currentTimeMillis();
		
		// Refills the player's ammo.
		playState.getPlayer().setMaxAmmo(playState.getPlayer().getMaxAmmo() + 20);
		playState.getPlayer().refillAmmo();
	}
	
	
	/**
	 * This method should be called when a skeleton is killed.
	 */
	
	public void onSkeletonDeath() {
		aliveSkeletons--;
	}

}
