package ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ui.UIComponent;
import ui.events.ButtonClickAction;

/**
 * A UIButton is a UIComponent that performs actions when
 * it is clicked.
 * 
 * @author Elliott Vince
 */

public class UIButton extends UIComponent {
	
	private BufferedImage image;
	
	// Component Dimensions
	private int width;
	private int height;
	
	private String text;
	
	// Text Font
	private Font font;
	
	// Text Location
	private int textX;
	private int textY;
	
	// Text Color
	private Color textColor;
	
	// Path to button image.
	private static final String IMAGE_PATH =
			"resources/ui/UIButton.png";
	
	// The ButtonClickEvent to occur when the button is clicked.
	private ButtonClickAction action;

	/**
	 * Creates a new UIButton.
	 * @param container The parent container.
	 */
	
	public UIButton() {
		// Initializes the UIButton.
		init();
	}
	
	/**
	 * Initializes the UIButton.
	 */
	
	@Override
	protected void init() {
		// Calls the parent class's init method.
		super.init();
		
		// Initializes the image.
		try {
			image = ImageIO.read(new File(IMAGE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Sets the UIButton's image.
		setImage(image);
		
		// Sets the width and height of the image.
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		// Sets the button's font and text color.
		font = new Font("Arial", Font.PLAIN, 28);
		textColor = Color.WHITE;
	}
	
	/**
	 * Gets the button's text.
	 */
	
	public String getText() {
		return text;
	}
	
	/**
	 * Sets the text of the button.
	 * @param text The text to be displayed.
	 */
	
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Gets the x location of the text.
	 */
	
	public int getTextX() {
		return textX;
	}

	/**
	 * Gets the y location of the text.
	 */
	
	public int getTextY() {
		return textY;
	}
	
	/**
	 * Gets the width of the UIButton.
	 */
	
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the UIButton.
	 */
	
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the text's font.
	 */
	
	public Font getTextFont() {
		return font;
	}
	
	/**
	 * Sets the font of the button.
	 * @param font The new font of the button.
	 */
	
	public void setTextFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Sets the text's font size.
	 * @param fontSize The new font size.
	 */
	
	public void setFontSize(int fontSize) {
		// Updates the font.
		this.font = new Font(font.getName(), font.getStyle(), fontSize);
	}
	
	/**
	 * Gets the color of the text.
	 */
	
	public Color getForeground() {
		return textColor;
	}
	
	/**
	 * Sets the color of the text.
	 * @param color The new color of the text.
	 */
	
	public void setForeground(Color color) {
		this.textColor = color;
	}
	
	/**
	 * Sets the action that occurs when the button is clicked.
	 * @param The action that should occur when the button is clicked.
	 */
	
	public void setActionOnClick(ButtonClickAction action) {
		this.action = action;
	}
	
	/**
	 * Handles the mouseClicked event.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// Calls the parent class's mouseClicked event.
		super.mouseClicked(e);
	}

	/**
	 * Performs an action when the UIButton is clicked.
	 */
	
	@Override
	public void onClick() {
		
		if (action != null) {
			
			// Runs the UIButton's action.
			action.run();
		}
	}
	
	/**
	 * Draws the UIButton to the screen.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		super.render(graphics);
		
		if (text != null && !text.equals("")) {
			
			graphics.setFont(font);
			graphics.setColor(textColor);
			
			// Centers the text.
			centerText(graphics);
			// Draws the text to the screen.
			graphics.drawString(text, textX, textY);
		}
	}
	
	/**
	 * Centers the text in the button.
	 */
	
	private void centerText(Graphics2D graphics) {
		int textWidth = graphics.getFontMetrics().stringWidth(text);
		int textHeight = graphics.getFontMetrics().getHeight();
		int ascent = graphics.getFontMetrics().getAscent();
		
		// Updates the UIButton's text location.
		textX = posX + image.getWidth() / 2 - textWidth / 2;
		textY = posY + (height - textHeight) / 2 + ascent;
	}

}
