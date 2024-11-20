package ui.containers;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ui.UIComponent;
import ui.UIContainer;

/**
 * A UIPanel is a UIComponent that holds other UIComponets.
 * Its core features act similarly to those of a JPanel.
 * 
 * @author Elliott Vince
 */
public class UIPanel extends UIContainer {
	
	private BufferedImage image;
	
	// Image path.
	private static final String IMAGE_PATH =
			"resources/ui/UIPanel.png";

	// Dimensions
	protected int width;
	protected int height;
	
	/**
	 * Creates a new UIPanel.
	 */
	
	public UIPanel() {
		// Initializes the UIPanel.
		init();
	}
	
	/**
	 * Initializes the UIPanel.
	 */
	@Override
	protected void init() {
		super.init();
		
		// Initializes the image.
		
		try {
			// Loads the image.
			image = ImageIO.read(new File(IMAGE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Sets the image and the dimensions.
		setImage(image);
		width = image.getWidth();
		height = image.getHeight();
	}
	
	/**
	 * Handles mouseClicked events.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
	}
	
	/**
	 * Calls the default add method, but centers the component.
	 */
	@Override
	public void add(UIComponent component) {
		super.add(component);
		centerComponent(component);
	}
	
	/**
	 * Gets the width of the panel.
	 */
	
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the panel.
	 */
	
	public int getHeight() {
		return height;
	}
	
	

}
