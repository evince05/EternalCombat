package ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A UIContainer is used to hold other UIComponets.
 * Its core features act similarly to those of a JPanel.
 * 
 * @author Elliott Vince
 */

public abstract class UIContainer {
	
	private BufferedImage image;
	
	// Dimensions
	protected int posX;
	protected int posY;
	
	private List<UIComponent> components;
	
	private boolean visible;
	private boolean showImage;
	
	/**
	 * Creates a UIContainer. <br>
	 * By default, the position is (0, 0). <br>
	 * To change this, use setPosition(int posX, int posY). <br> <br>
	 * Also, an image must be set using setImage(BufferedImage image) <br>
	 * in order for the image to appear.
	 */
	
	public UIContainer() {
		// Initializes the UIContainer.
		init();
	}
	
	/**
	 * Initializes any of the variables that the container
	 * needs.
	 */
	
	protected void init() {
		image = null;
		posX = 0;
		posY = 0;
		this.components = new ArrayList<UIComponent>();
		
		// By default, the container is visible and its image is shown.
		visible = true;
		showImage = true;
	}
	
	/**
	 * Gets the x position of the top left corner of the container.
	 */
	
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Gets the y position of the top left corner of the container.
	 */
	
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Sets the location of the top left corner of the container.
	 * @param posX The new x location.
	 * @param posY The new y location.
	 */
	
	public void setLocation(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * Determines if the container is visible.
	 */
	
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Sets the visibility of the container.
	 * @param visible True if the container should be visible,
	 * false if otherwise.
	 */
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Determines if the image is shown.
	 */
	
	public boolean isImageShown() {
		return showImage;
	}
	
	/**
	 * Set whether to show the image or not.
	 * @param True if the image should be visible,
	 * false if otherwise.
	 */
	
	public void setImageVisibility(boolean visibility) {
		this.showImage = visibility;
	}
	
	/**
	 * Adds a child component to this container.
	 * @param component The component to be added.
	 */
	
	public void add(UIComponent component) {
		component.setParent(this);
		components.add(component);
	}
	
	/**
	 * Centers a specific child component in the middle
	 * of the X axis.
	 * 
	 * @param component The component to be centered.
	 */
	
	public void centerComponent(UIComponent component) {
		int containerWidth = image.getWidth();
			
		int compWidth = component.getImage().getWidth();
			
		// Sets the location of the component to the middle of the container.
		component.setLocation(containerWidth / 2 - compWidth / 2, 
			component.getPosY());
	}
	
	/**
	 * Gets the image of the UIContainer.
	 */
	
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Sets the image of the UIContainer.
	 * @param image The new image of the UIContainer.
	 */
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	
	/**
	 * This method is called whenever the input's mouse is
	 * clicked.
	 */
	
	public void mouseClicked(MouseEvent e) {
		
		// Determines if the mouse clicked the container.
		if (e.getX() >= posX &&
				e.getX() <= posX + image.getWidth() &&
				e.getY() >= posY &&
				e.getY() <= posY + image.getHeight()) {
			
			for (UIComponent component : components) {
				component.mouseClicked(e);
			}
		}
	}
	
	/**
	 * This method renders each child component.
	 */
	
	public void render(Graphics2D graphics) {
		
		// Renders the container if it is visible.
		if (visible) {
			
			if (image != null && showImage) {
				graphics.drawImage(image, posX, posY, null);
			}
			
			for (UIComponent component : components) {
				
				// Renders the component if it is visible.
				if (component.isVisbile()) {
					component.render(graphics);
				}
				
			}
		}
		
		
	}

}
