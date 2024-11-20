package ui.components.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import ui.containers.UIPanel;

/**
 * The controls panel displays the list of controls.
 * @author Elliott Vince
 */

public class ControlsPanel extends UIPanel {
	
	// Controls array
	private String[] controls;
	
	// Constants
	private static final int TEXT_MARGIN_Y = 150;
	private static final int TEXT_OFFSET_Y = 20;
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);
	
	/**
	 * Create a new controls panel. This displays a list
	 * of all the controls in the game.
	 */
	
	public ControlsPanel() {
		// Initializes the controls panel.
		init();
	}
	
	/**
	 * Initializes the objects and variables in the parent class
	 * and initializes the controls array.
	 */
	
	@Override
	protected void init() {
		super.init();
		
		// Initializes the array of controls.
		controls = new String[] {
			"W - Move Up",
			"A - Move Left",
			"S - Move Down",
			"D - Move Right",
			"Left Click - Shoot",
			"Esc - Pause"
		};
	}
	
	/**
	 * Renders the controls panel to the screen.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		super.render(graphics);
		
		graphics.setColor(TEXT_COLOR);
		graphics.setFont(DEFAULT_FONT);
		
		// Draws each line of text at the appropriate position.
		for (int index = 0; index < controls.length; index++) {

			// Gets the line of text at the current index
			String text = controls[index];
			
			// Calculates the text's x and y location
			int textPosX = width / 2 - 
				graphics.getFontMetrics().stringWidth(text) / 2;

			int textPosY = TEXT_MARGIN_Y + TEXT_OFFSET_Y * index +
				graphics.getFontMetrics().getHeight() * index;

			// Draws the text to the screen.
			graphics.drawString(text, posX + textPosX, posY + textPosY);
		}
	}
	
	
	
	

}
