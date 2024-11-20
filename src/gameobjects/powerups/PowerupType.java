package gameobjects.powerups;

/**
 * A simple enum of powerup types.
 * @author Elliott Vince
 */

public enum PowerupType {
	HASTE(10, "resources/sprites/powerups/hastepotion.png"),
	MAX_HEALTH(0, "resources/sprites/powerups/maxhealth.png"),
	MAX_AMMO(0, "resources/sprites/powerups/maxammo.png"),
	STRENGTH(10, "resources/sprites/powerups/strengthpotion.png");
	
	// The duration of the powerup (in seconds).
	private int duration;
	
	// The filepath of the powerup's image.
	private String filepath;
	
	/**
	 * Creates a new powerup type.
	 * @param duration The powerup's duration (in seconds).
	 * @param filepath The filepath of the powerup's image.
	 */
	
	private PowerupType(int duration, String filepath) {
		this.duration = duration;
		this.filepath = filepath;
	}
	
	/**
	 * Gets the powerup's duration (in seconds).
	 */
	
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Returns wheter the powerup type has a duration.
	 * @return true If the duration is greater than 0, otherwise false.
	 */
	
	public boolean hasDuration() {
		return duration > 0;
	}
	
	/**
	 * Gets the filepath of the powerup's image.
	 */
	
	public String getPath() {
		return filepath;
	}
	
}
