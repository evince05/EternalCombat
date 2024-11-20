package gameobjects;

import java.awt.Graphics2D;

import gamestates.PlayState;
import ui.Healthbar;

/**
 * A LivingEntity is a game object with health.
 * It has fields such as speed, damage, and health.
 * @author Elliott Vince.
 */

public abstract class LivingEntity extends GameObject {
	
	// Health
	protected int health;
	protected int maxHealth;
	
	protected int speed;
	
	// Healthbar
	protected Healthbar healthbar;

	/**
	 * Creates a new living entity at the location (posX, posY).
	 * @param playState An instance of the PlayState class.
	 * @param posX The x position where the entity should spawn.
	 * @param posY The y position where the entity should spawn.
	 * @param health The health that the living entity spawns with.
	 * @param maxHealth The maximum health of the living entity. 
	 */
	
	public LivingEntity(PlayState playState, int posX, int posY, int health, int maxHealth) {
		super(playState, posX, posY);
		this.speed = 1;
		this.health = health;
		this.maxHealth = maxHealth;
		
		this.healthbar = new Healthbar(this);
	}
	
	/**
	 * Sets the speed of the LivingEntity. 
	 * This is used for movement speed.
	 * If this method is never called, the speed
	 * is set to 1 by default.
	 * 
	 * @param speed The new speed of the LivingEntity.
	 */
	
	protected void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Gets the speed of the LivingEntity. 
	 * This is used for movement speed.
	 */
	
	protected int getSpeed() {
		return speed;
	}
	
	/**
	 * Gets the health of the LivingEntity.
	 */
	
	public int getHealth() {
		return health;
	}
	
	/**
	 * Sets the health of the LivingEntity.
	 * @param health The new health of the living entity.
	 */
	
	protected void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * Gets the max health of the LivingEntity.
	 */
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Sets the max health of the LivingEntity.
	 * @param maxHealth The new max health of the LivingEntity.
	 */
	protected void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * Reduces the LivingEntity's health by the specified amount.
	 * @param amount The amount of damage which will be dealt.
	 */
	
	public void damage(int amount) {
		/*
		 * Damages the LivingEntity by the specified amount,
		 * but if the LivingEntity cannot be damaged by that
		 * amount, the LivingEntity's health is set to 0.
		 */
		
		if (health - amount < 0) {
			health = 0;
		}
		else {
			health -= amount;
		}
	}
	
	/**
	 * Heals the LivingEntity by the specified amount.
	 * @param amount The amount of health the LivingEntity will gain.
	 */
	
	public void heal(int amount) {
		
		/*
		 * Heals the LivingEntity by the specified amount,
		 * but if the LivingEntity cannot be healed by that
		 * amount, the LivingEntity's health is set to 0.
		 */
		if (health + amount > maxHealth) {
			health = maxHealth;
		}
		else {
			health += amount;
		}
	}
	
	/**
	 * Renders the LivingEntity to the screen.
	 */
	
	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		healthbar.update(playState, graphics);
	}
	
	/**
	 * Updates the LivingEntity.
	 */
	
	@Override
	public void update() {
		super.update();
		
		// Handles death
		if (health <= 0) {
			onDeath();
			destroy();
		}
	}
	
	/**
	 * This method is called when the entity dies.
	 */
	
	public abstract void onDeath();
}
