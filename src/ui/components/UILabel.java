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

/**
 * The UILabel is a UIComponent that displays a line of text.
 * @author Elliott Vince
 */

public class UILabel extends UIComponent {

private BufferedImage image;
	
	private String text;
	
	// Text Font
	private Font font;
	
	// Text Location
	private int textX;
	private int textY;
	
	// Component Dimensions
	private int width;
	private int height;
	
	// Text Color
	private Color textColor;
	
	// Path to label image.
	private static final String IMAGE_PATH =
			"resources/ui/UILabel.png";

	/**
	 * Creates a new UILabel.
	 * @param text The UILabel's text.
	 */
	
	public UILabel(String text) {
		// Initializes the UILabel.
		init();
		this.text = text;
	}
	
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
		
		// Updates the UILabel's image and dimensions.
		setImage(image);
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		// Updates the UILabel's font and text color.
		font = new Font("Arial", Font.PLAIN, 28);
		textColor = Color.WHITE;
	}
	
	/**
	 * Gets the labels's text.
	 */
	
	public String getText() {
		return text;
	}
	
	/**
	 * Sets the text of the label.
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
	 * Gets the text's font.
	 */
	
	public Font getTextFont() {
		return font;
	}
	
	/**
	 * Sets the font of the label.
	 * @param font The new font of the label.
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
	 * Handles mouseClicked events for the UILabel.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// Calls the parent class's mouseClicked event.
		super.mouseClicked(e);
	}
	
	/**
	 * Rendes the UILabel to the screen.
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
	 * Centers the text in the label.
	 */
	
	private void centerText(Graphics2D graphics) {
		int textWidth = graphics.getFontMetrics().stringWidth(text);
		int textHeight = graphics.getFontMetrics().getHeight();
		
		/**
		 * Apparently the height of the text isn't the actual
		 * height of the text, so the text's ascent has to be added
		 * as well.
		 */
		
		int ascent = graphics.getFontMetrics().getAscent();
		
		textX = posX + width / 2 - textWidth / 2;
		textY = posY + (height - textHeight) / 2 + ascent;
	}
	
	@Override
	public void onClick() {
	}

}
