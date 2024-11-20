package ui;

import java.awt.Color;
import java.awt.Graphics2D;

import gameobjects.LivingEntity;
import gamestates.PlayState;

/**
 * Healthbars are used to display the health of Living Entities
 * such as the player and skeletons.
 * 
 * @author Elliott Vince
 */
public class Healthbar {
	
	// Healthbar colors.
	private Color bgColor;
	private Color fgColor;
	private Color borderColor;
	
	// Health Fields
	private int maxHealth;
	private int health;
	
	// Rectangle Dimensions
	private int xPos;
	private int yPos;
	private int width;
	private int height;
	
	// Dimensions.
	private static final int DEFAULT_WIDTH = 64;
	private static final int DEFAULT_HEIGHT = 5;
	
	private LivingEntity entity;
	
	/**
	 * Creates a new Healthbar. <br> <br>
	 * The entity specified in the constructor is the entity <br>
	 * which this healthbar belongs to. To use this healthbar, <br>
	 * call its update method during the LivingEntity's update method. <br>
	 * This will cause the the UI of the healthbar to update.
	 * 
	 * @param entity The LivingEntity which this healthbar belongs to.
	 */
	
	public Healthbar(LivingEntity entity) {
		this.entity = entity;
		
		maxHealth = entity.getMaxHealth();
		health = entity.getHealth();
		xPos = entity.getPosX();
		yPos = entity.getPosY() + DEFAULT_WIDTH + 10;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		
		/*
		 * Set the default colors of the healthbar.
		 * Background: Light Gray
		 * Foreground: Red
		 * Border: Black
		 */
		
		bgColor = Color.LIGHT_GRAY;
		fgColor = Color.RED;
		borderColor = Color.BLACK;
	}
	
	/**
	 * Similarly to the draw(Graphics2D graphics) method seen in some classes,
	 * this method will simply update the location and values of the healthbar,
	 * while drawing it to the screen. <br> <br>
	 * To use this method, call it in the draw method of the living entity.
	 * @param playState 
	 */
	
	public void update(PlayState playState, Graphics2D graphics) {
		// Updates the position and health variables.
		xPos = entity.getPosX() - playState.getCamera().getPosX();
		yPos = entity.getPosY() - playState.getCamera().getPosY();
		
		health = entity.getHealth();
		maxHealth = entity.getMaxHealth();
		
		/*
		 * Draws three rectangles in the order of background, 
		 * foreground, and then the border.
		 */
		
		// Draws and fills the background of the rectangle.
		graphics.setColor(bgColor);
		graphics.fillRect(xPos, yPos, width, height);
		
		/*
		 * Draws and fills the foreground of the rectangle.
		 * Here, the health percentage (health 
		 */
		graphics.setColor(fgColor);
		graphics.fillRect(xPos, yPos, (int) (width * getHealthPercentage()), height);
		
		// Draws ONLY the border of the rectangle.
		graphics.setColor(borderColor);
		graphics.drawRect(xPos, yPos, width, height);
	}
	
	/**
	 * Sets the bounds of the healthbar.
	 * @param xPos The x location of the healthbar.
	 * @param yPos The y location of the healthbar.
	 * @param width The width of the healthbar.
	 * @param height The height of the healthbar.
	 */
	
	public void setBounds(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Sets the background color of the healthbar.
	 * @param bgColor The new background color.
	 */
	
	public void setBackgroundColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
	/**
	 * Sets the foreground color of the healthbar.
	 * @param fgColor The new foreground color.
	 */
	
	public void setForegroundColor(Color fgColor) {
		this.fgColor = fgColor;
	}
	
	/**
	 * Sets the border color of the healthbar.
	 * @param borderColor The new border color.
	 */
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	/**
	 * Gets the health percentage. This is used to
	 * calculate the width of the foreground rectangle of the
	 * healthbar.
	 */
	
	private double getHealthPercentage() {
		return (double) health / maxHealth;
	}

}
