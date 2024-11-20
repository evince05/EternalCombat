package ui.events;

/**
 * A simple interface which calls a method whenever a
 * UIButton is clicked.
 * 
 * @author Elliott Vince
 */

public interface ButtonClickAction extends Runnable {
	
	/**
	 * This method is called whenever a UIButton is clicked.
	 */
	
	@Override
	public abstract void run();

}
