package ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * A UIComponent can be displayed in the screen and added to parent
 * containers. It has methods that perform similarly to JComponents.
 *
 * @author Elliott Vince
 */

public abstract class UIComponent {
	
	// Location
	protected int posX;
	protected int posY;
	
	protected UIContainer parent;
	
	private BufferedImage image;
	
	private boolean visible;
	
	/**
	 * Creates a UI Component. <br>
	 * The default position is (0, 0), but you can change it <br>
	 * by using setPosition(int posX, int posY);
	 */
	
	public UIComponent() {
		// Initializes the 
		init();
	}
	
	/**
	 * Initializes any of the necessary variables 
	 * for the component.
	 */
	protected void init() {
		this.posX = 0;
		this.posY = 0;
		parent = null;
		
		// By default, the component is visible.
		visible = true;
	}
	
	/**
	 * Gets the x position of the upper left corner <br>
	 * of the UI Component.
	 */
	
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Gets the y position of the upper left corner <br>
	 * of the UI Component.
	 */
	
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Sets the location of the UI Component. <br>
	 * Note: The location is added to the location of the container.
	 * @param posX The new x position of the UI Component.
	 * @param posY The new y position of the UI Component.
	 */
	
	public void setLocation(int posX, int posY) {
		
		// Adds the container's x and y location to the specified location.
		int x = posX + parent.getPosX();
		int y = posY + parent.getPosY();
		
		// Sets the location if the specified location is within the container.
		if (x >= parent.getPosX() &&
				x <= parent.getPosX() + parent.getImage().getWidth() &&
				y >= parent.getPosY() &&
				y <= parent.getPosY() + parent.getImage().getHeight()) {
			this.posX = x;
			this.posY = y;
		}
		
	}
	
	/**
	 * Gets the centered x location for the image.
	 * @return The centered x location for the image.
	 */
	
	public int getCenteredX() {
		return parent.getImage().getWidth() / 2 -
				image.getWidth() / 2;
	}
	
	/**
	 * Sets the parent of the UIComponent.
	 * Only call this when adding the component to a parent.
	 * @param parent The UIContainer which the component will
	 * be added to.
	 */
	
	public void setParent(UIContainer parent) {
		this.parent = parent;
		this.posX = parent.getPosX() + posX;
		this.posY = parent.getPosY() + posY;
	}
	
	/**
	 * Gets if the UIComponent is visible.
	 */
	
	public boolean isVisbile() {
		return visible;
	}
	
	/**
	 * Sets the visibility of the UIComponent.
	 * @param visible True if the component is visible, 
	 * false if otherwise.
	 */
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Gets the image of the UI component.
	 */
	
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Sets the image of the component.
	 * @param image The new image of the component.
	 */
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Draws the component to the screen. <br>
	 * If the image is null, it will not be drawn.
	 */
	
	public void render(Graphics2D graphics) {
		if (image != null) {
			graphics.drawImage(image, posX, posY, null);
		}
	}
	
	/**
	 * This is called whenever the mouse is clicked.
	 */
	
	public void mouseClicked(MouseEvent e) {
		// Determines if the mouse clicked the image.
		if (e.getX() >= posX &&
				e.getX() <= posX + image.getWidth() &&
				e.getY() >= posY &&
				e.getY() <= posY + image.getHeight()) {
			
			// Calls the component's specified onclick method.
			onClick();
		}
	}
	
	/**
	 * This method is called when the component has been clicked.
	 */
	
	public abstract void onClick();

}
