package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import gamestates.PlayState;
import util.Methods;

/**
 * The HUD is a UI component that will display the player's health,
 * amount of ammo, and other things in the game such as how many
 * skeletons are alive, the current level, and the player's score.
 * 
 * @author Elliott Vince
 */

public class HUD {
	
	// The default font and text color.
	private static Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16);
	private static Color DEFAULT_TEXT_COLOR = Color.WHITE;
	
	private PlayState playState;
	
	// The location of the HUD.
	private int posX;
	private int posY;
	
	// The HUD's image.
	private Image image;
	
	// Variables that will be displayed on the HUD.
	
	/*
	 * The player's health is a string becaused it is displayed
	 * in the HUD as "health / max health HP".
	 */
	
	private String health;
	
	/*
	 * The player's ammo is a string becaused it is displayed
	 * in the HUD as "ammo / max ammo".
	 */
	
	private String ammoRemaining;
	
	/*
	 * The number of skeletons remaining is also formatted as
	 * a string.
	 */
	
	private String skeletonsRemaining;
	
	
	// The game's current level.
	private int level;
	
	// The player's score.
	private int score;
	
	/**
	 * Creates a new HUD object.
	 * @param playState An instance of the PlayState class.
	 */
	
	public HUD(PlayState playState) {
		// Initialize each of the variables
		this.playState = playState;
		
		// Sets the location of the HUD to the top left corner.
		this.posX = 20;
		this.posY = 20;
		
		// Initializes and scales the HUD's image.
		
		this.image = Methods.scaleImage(new ImageIcon
				("resources/ui/HUD.png").getImage(), 146, 210);
		
		// Initializes the variables that should be displayed.
		this.health = playState.getPlayer().getHealth() + " / " +
				playState.getPlayer().getMaxHealth() + " HP";
		
		this.ammoRemaining = playState.getPlayer().getAmmo() + " / " +
				playState.getPlayer().getMaxAmmo();
		
		this.level = playState.getLevelManager().getCurrentLevel();
		
		this.skeletonsRemaining = playState.getLevelManager()
				.getAliveSkeletons() + " / " + playState.getLevelManager()
				.getTotalSkeletons();
		
		this.score = playState.getPlayer().getScore();
		
	}
	
	/**
	 * Gets the y position of the HUD.
	 */
	
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Gets the x position of the HUD.
	 */
	
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Sets the location of the HUD.
	 * @param x The new x location of the HUD.
	 * @param y The new y location of the HUD.
	 */
	
	public void setLocation(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	
	/**
	 * Updates the HUD. This will update any of its components
	 * and variables, such as the player's health, the amount of skeletons
	 * remaining, the player's ammo, etc.
	 */
	
	public void update() {
		
		/*
		 * Updates the HUD variables.
		 */
		
		this.health = playState.getPlayer().getHealth() + " / " +
				playState.getPlayer().getMaxHealth();
		
		this.ammoRemaining = playState.getPlayer().getAmmo() + " / " +
				playState.getPlayer().getMaxAmmo();
		
		this.level = playState.getLevelManager().getCurrentLevel();
		
		this.skeletonsRemaining = playState.getLevelManager()
				.getAliveSkeletons() + " / " + playState.getLevelManager()
				.getTotalSkeletons();
		
		this.score = playState.getPlayer().getScore();
		
		
		
	}
	
	/**
	 * Renders the HUD to the screen.
	 * @param graphics The graphics object which will render the HUD.
	 */
	
	public void render(Graphics2D graphics) {
		
		// Draws the HUD's image.
		graphics.drawImage(image, posX, posY, null);
		
		// Sets the color and font of the graphics.
		graphics.setColor(DEFAULT_TEXT_COLOR);
		graphics.setFont(DEFAULT_FONT);
		
		// Draws the player's current ammo count to the HUD.
		graphics.drawString(ammoRemaining, posX + 60, posY + 28);
		
		// Draws the player's current health to the HUD.
		graphics.drawString(health, posX + 60, posY + 69);
		
		// Formats and draws the current level to the HUD.
		graphics.drawString("Level " + level, posX + 60, posY + 111);
		
		// Draws the number of remaining skeletons to the HUD.
		graphics.drawString(skeletonsRemaining, posX + 60, posY + 153);
		
		// Draws the player's score to the HUD.
		graphics.drawString("Score: " + score, posX + 20, posY + 195);
	}
	
	

}
