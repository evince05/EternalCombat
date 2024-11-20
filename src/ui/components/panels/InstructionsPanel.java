package ui.components.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Scanner;

import ui.containers.UIPanel;

/**
 * This panel displays the "How to Play" instructions.
 * @author Elliott Vince
 */

public class InstructionsPanel extends UIPanel {
	
	// Positioning Constants.
	private static final int TEXT_MARGIN_Y = 60;
	private static final int TEXT_OFFSET_Y = 25;
	private static final int MAX_TEXT_WIDTH = 250;
	
	// Text Color and Font
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);
	
	private String text;
	
	/**
	 * Creates a new instructions panel.
	 */
	
	public InstructionsPanel() {
		// Initializes the panel.
		init();
	}
	
	/**
	 * Initializes the instructions panel.
	 */
	
	@Override
	protected void init() {
		super.init();
		
		// Initialize the text. This will be automatically split between multiple lines.
		
		text = "Fight endless waves of skeletons using a bow and arrow! " +
			"Each wave of skeletons gets more difficult, so make sure " +
			"to not get attacked! Use WASD or the arrow keys to move around " +
			"the map. Left Click to shoot skeletons. Your score increases as " + 
			"you shoot skeletons, but you get the most score if you kill them " + 
			"quickly. Skeletons have a chance of dropping power ups that will " +
			"help you survive longer. Good luck!";
	}
	
	/**
	 * This method is overriden so it can draw the text to the screen.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		// Calls the render method of the parent class.
		super.render(graphics);
		
		// Renders the text.
		
		graphics.setColor(TEXT_COLOR);
		graphics.setFont(DEFAULT_FONT);
		
		// Creates an empty string.
		String line = "";
		
		// Creates a counter variable for the current line of text.
		int currentLine = 0;
		
		// Creates a scanner for the text.
		Scanner scanner = new Scanner(text);
		
		// Gets the font metrics of the graphics.
		FontMetrics fontMetrics = graphics.getFontMetrics();
		
		int textWidth = fontMetrics.stringWidth(line);
		
		while (scanner.hasNext()) {
			String word = scanner.next();
			
			/*
			 * Determines if the width of the string (in pixels) 
			 * is less than or equal to the max text width.
			 * If it is, the word is added to the line of text.
			 * If it is greater, the line is rendered and a new line
			 * begins.
			 */
			
			if (fontMetrics.stringWidth(line + word) <= MAX_TEXT_WIDTH) {
				// The word is added to the line of text.
				line += word + " ";
				
				// Updates the text width
				textWidth = fontMetrics.stringWidth(line);
				
				// If the scanner is finished iterating, the text is rendered.
				if (!scanner.hasNext()) {
					
					graphics.drawString(line, posX + width / 2 - textWidth / 2,
							posY + TEXT_MARGIN_Y + TEXT_OFFSET_Y * currentLine + 
							fontMetrics.getHeight() + fontMetrics.getDescent());
					
					currentLine++;
				}
			}
			else {
				/*
				 * The width of the string is greated than the max text width,
				 * so the line is rendered and a new line begins.
				 */
				
				graphics.drawString(line, posX + width / 2 - textWidth / 2, 
						posY + TEXT_MARGIN_Y + TEXT_OFFSET_Y * currentLine + 
						fontMetrics.getHeight() + fontMetrics.getDescent());
				
				currentLine++;
				
				// Resets the line of text and adds the word to it.
				line = "";
				line += word + " ";
				
				textWidth = fontMetrics.stringWidth(line);
				
				// If the scanner is finished iterating, the text is rendered.
				if (!scanner.hasNext()) {
					
					graphics.drawString(line, posX + width / 2 - textWidth / 2,
							posY + TEXT_MARGIN_Y + TEXT_OFFSET_Y * currentLine + 
							fontMetrics.getHeight() + fontMetrics.getDescent());
					
					currentLine++;
				}
			}
		}
		
		// Closes the scanner
		scanner.close();
	}
	
	
	
	

}
