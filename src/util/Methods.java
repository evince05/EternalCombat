package util;

import java.awt.Image;

/**
 * A class of static utility methods.
 * @author Elliott Vince
 */

public class Methods {

	/**
	 * Resizes an image and retains the image's quality.
	 * @param image The image to be scaled.
	 * @param width The desired width of the image.
	 * @param height The desired height of the image.
	 * 
	 * @return The scaled image.
	 */
	
	public static Image scaleImage(Image image, int width, int height) {
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
}
