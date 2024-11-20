package gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A spritesheet is a collection of images that continuosly switch
 * to give GameObjects the appearance that they are moving.
 * 
 * @author Elliott Vince
 */

public class SpriteSheet {
	
	// Dimensions
	private int width;
	private int height; 
	
	private BufferedImage image;
	
	/**
	 * Creates a spritesheet.
	 * @param width The width of each cell
	 * @param height The height of each cell
	 * @param path The file path of the spritesheet.
	 */
	
	public SpriteSheet(String path, int width, int height) {
		this.width = width;
		this.height = height;
		
		try {
			// Initializes the image.
			this.image = ImageIO.read(new File("resources/" + path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the image at the cell with the top left corner of (x, y). <br>
	 * The dimensions of the image are the width and the height specified <br>
	 * in the constructor.
	 * 
	 * @param x The x cell of the image.
	 * @param y The y cell of the image.
	 * @return The image at cell (x, y) with the width and height <br>
	 * specified in the constructor.
	 */
	
	public BufferedImage getImage(int x, int y) {
		if (image != null) {
			return image.getSubimage(x * width, y * height, width, height);
		}
		return null;
	}
	
	/**
	 * Gets an array of images in row <b>(row)</b> of the spritesheet, <br>
	 * spanning from cell <b>(start)</b> to cell <b>(end</b>.
	 * 
	 * @param row The row of the spritesheet where the desired frames are located.
	 * @param startIndex The index of the first cell to be used.
	 * @param endIndex The index of the last cell to be used.
	 */

	public BufferedImage[] getImages(int row, int startIndex, int endIndex) {
		int size = endIndex - startIndex + 1;
		BufferedImage[] images = new BufferedImage[size];
		
		for (int count = 0; count < images.length; count++) {
			images[count] = getImage(count + startIndex, row);
		}
		return images;
	}
	
	/**
	 * Gets the width of each cell.
	 */
	
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of each cell.
	 */
	
	public int getHeight() {
		return height;
	}
}
