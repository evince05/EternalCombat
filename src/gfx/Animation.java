package gfx;

import java.awt.image.BufferedImage;

/**
 * Animations are used to make GameObjects look like
 * they are moving.
 * 
 * @author Elliott Vince
 */

public class Animation {
	
	private BufferedImage[] frames;
	private boolean playedOnce;
	private long delay;
	private int currentFrame;
	
	private long startTime;
	
	private boolean onlyPlayOnce;
	
	private AnimationDirection direction;
	private AnimationEvent[] events;
	
	/**
	 * Creates a new animation object. <br> <br>
	 * When creating a new gameobject with an animation, <br>
	 * create a new Animation object in the gameobject's <br>
	 * constructor.
	 * 
	 * @param frames An array of images to be used in the animation.
	 */
	
	public Animation(BufferedImage[] frames) {
		
		// Initializes all the variables of the animation.
		this.frames = frames;
		this.events = new AnimationEvent[frames.length];
		this.delay = getDefaultDelay(frames.length);
		playedOnce = false;
		onlyPlayOnce = false;
		
		// Sets the current frame at the first frame of the animation.
		currentFrame = 0;
		
		// Sets the start time of the animation at the current time in milliseconds.
		startTime = System.currentTimeMillis();
		
		// Sets the default animation direction if it is not null.
		if (direction == null) {
			direction = AnimationDirection.RIGHT;
		}
	}
	
	/**
	 * Creates a new animation object. <br> <br>
	 * When creating a new gameobject with an animation, <br>
	 * create a new Animation object in the gameobject's <br>
	 * constructor.
	 * 
	 * @param frames An array of images to be used in the animation.
	 * @param direction The direction that the animation is facing.
	 */
	
	public Animation(BufferedImage[] frames, AnimationDirection direction) {
		this.frames = frames;
		this.events = new AnimationEvent[frames.length];
		this.delay = getDefaultDelay(frames.length);
		playedOnce = false;
		
		// Sets the current frame at the first frame of the animation.
		currentFrame = 0;
		
		// Sets the start time of the animation at the current time in milliseconds.
		startTime = System.currentTimeMillis();
		
		this.direction = direction;
	}
	
	/**
	 * Gets the delay (in milliseconds) between each frame.
	 */
	
	public long getDelay() {
		return delay;
	}
	
	/**
	 * Sets the delay (in milliseconds) between each frame.
	 * @param delay The new delay (in milliseconds) between each frame.
	 */
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	/**
	 * Gets the index of the current frame.
	 */
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	/**
	 * Updates the current frame in the animation.
	 * If there is a delay, the image will update 
	 * at the speed of the delay.
	 */
	
	public void update() {
		if (delay < 0 || frames.length == 1 || (onlyPlayOnce && playedOnce)) {
			/*
			 * If the delay is negative, the animation cannot update.
			 * Also, if the animation only contains one frame, it does not
			 * need to update.
			 * If the animation has been set to only play once, it will not
			 * update if it has already played.
			 */
			
			return;
		}
		
		// Gets how many milliseconds have passed since the animation started.
		long elapsed = System.currentTimeMillis() - startTime;
		
		// Updates the current frame if the delay has been completed.
		if (elapsed > delay) {
			currentFrame++;
			// Resets the start time of the animation
			startTime = System.currentTimeMillis();
		}
	
		/*
		 * Checks if the animation is current displaying the last frame.
		 * If it is true, the animation repeats by going back to the first
		 * frame.
		 */
		
		if (currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
		
		/*
		 * Checks if the current frame has any events that should occur.
		 * If it does, the events occur. If not, the animation continues.
		 */
		
		if (events[currentFrame] != null) {
			events[currentFrame].run();
		}
	}
	
	/**
	 * Resets the animation back to the first frame.
	 */
	
	public void reset() {
		playedOnce = false;
		currentFrame = 0;
	}
	
	/**
	 * Gets the image of the frame that is currently being
	 * displayed.
	 * @return The image of the frame that is currently being
	 * displayed.
	 */
	
	public BufferedImage getImage() {
		return frames[currentFrame];
	}
	
	/**
	 * Checks if the animation has played once.
	 * @return True if the animation has played once from start to finish,
	 * false if otherwise.
	 */
	
	public boolean hasPlayedOnce() {
		return playedOnce;
	}
	
	/**
	 * Determines if the animation should only play once.
	 * @param playOnce True if the animation should only play once, <br>
	 * false if it should repeat.
	 */
	public void setPlayOnce(boolean onlyPlayOnce) {
		this.onlyPlayOnce = onlyPlayOnce;
	}
	
	/**
	 * Gets the number of frames in the animation.
	 */
	
	public int getNumFrames() {
		return frames.length;
	}
	
	/**
	 * Returns the delay needed for the entire animation 
	 * to play once every second. <br> <br>
	 * Formula: delay = 1000 / frames
	 */
	
	private long getDefaultDelay(int frames) {
		return 1000 / frames;
	}
	
	/**
	 * Used to determine which way a
	 * game object is facing.
	 */
	
	public enum AnimationDirection {
		UP, DOWN, LEFT, RIGHT
	}
	
	/**
	 * Gets which direction the animation is facing.
	 */
	
	public AnimationDirection getDirection() {
		return direction;
	}
	
	/**
	 * Sets the current direction that the animation is facing.
	 * @param direction The new direction that the animation is facing.
	 */
	
	public void setDirection(AnimationDirection direction) {
		this.direction = direction;
	}
	
	/**
	 * Sets an event to occur whenever a specific frame is played.
	 * @param frame The frame in which the event should occur.
	 * <b><u>Note:</b></u> This is the frame, not the index.
	 * @param event The event that should occur.
	 */
	
	public void setAnimationEvent(int frame, AnimationEvent event) {
		// Makes sure the frame is a part of the animation.
		if (frame > -1 && frame <= frames.length) {
			events[frame - 1] = event;
		}
	}
	
	/**
	 * Moves the animation ahead by one frame. <br>
	 * If the animation is on the last frame, it sets
	 * the frame to 0.
	 */
	
	public void nextFrame() {
		if (currentFrame + 1 < frames.length) {
			currentFrame++;
		}
		else {
			currentFrame = 0;
			playedOnce = true;
		}
	}

}
