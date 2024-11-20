package gfx;

/**
 * A simple interface with a method that is run whenever
 * a specific animation plays a certain frame.
 * 
 * @author Elliott Vince.
 */

public interface AnimationEvent extends Runnable {
	
	/**
	 * This method is called when the animation plays a
	 * certain frame.
	 */
	
	@Override
	public abstract void run();

}
