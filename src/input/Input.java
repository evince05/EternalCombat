package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gamestates.GameState;

/**
 * Most of the methods used in this class were created by
 * Majoolwip.
 * 
 * This class handles input. It also records input frame-by-frame,
 * which is very beneficial for this game because it is able to
 * precisely when a button is clicked and released. This is crucial
 * for things such as projectile creation, player attacks, etc.
 * 
 * Link: https://www.youtube.com/watch?v=bNq1UxL2cmE
 */

public class Input implements KeyListener, MouseListener {
	
	private GameState state;
	
	// Standard keyboards have 256 keys.
	private final int NUM_KEYS = 256;
	
	// A standard mouse has 3 buttons.
	private final int NUM_BUTTONS = 4;
	
	private boolean[] pressedKeys;
	private boolean[] pressedKeysLast;
	
	private boolean[] pressedButtons;
	private boolean[] pressedButtonsLast;
	
	/**
	 * Creates a new input object. <br>
	 * <b><u>NOTE:</b></u> The crrent state must be set using 
	 * setState(GameState currentState);
	 */
	public Input() {
		
		/*
		 * Creates two boolean arrays, each with a size of 256.
		 * 
		 * Also, creating an array is useful because it is easier to tell
		 * if multiple keys are pressed at the same time (eg. CTRL + W).
		 * 
		 * Another advantage of using two arrays is that one array can
		 * detect input in the current frame, and the other array stores
		 * the inputs given in the last frame.
		 */
		
		pressedKeys = new boolean[NUM_KEYS];
		pressedKeysLast = new boolean[NUM_KEYS];
		
		/*
		 * Creates a boolean array for with a size of 4.
		 * Index 0: No Button 
		 * Index 1: Mouse Button 1 (Left Click?)
		 * Index 2: Mouse Button 2 (Middle Click?)
		 * Index 3: Mouse Button 3 (Right Click?)
		 * 
		 * Here, two arrays are created for the same reasons specified above.
		 */
		
		pressedButtons = new boolean[NUM_BUTTONS];
		pressedButtonsLast = new boolean[NUM_BUTTONS];
	}
	
	/**
	 * Sets the game state which the input will handle.
	 * @param state The new GameState which the input will handle.
	 */
	
	public void setGameState(GameState state) {
		this.state = state;
	}
	
	/**
	 * Determines if a specific key has just been pressed.
	 * @param keyCode The key to be checked.
	 * @return True if the key is pressed, false if it is not.
	 */
	
	public boolean isKeyDown(int keyCode) {
		/*
		 * This will return true if the key has been pressed in
		 * this frame, but not in the last frame.
		 */
		return pressedKeys[keyCode] && !pressedKeysLast[keyCode];
	}
	
	/**
	 * Determines if a specific key is held down.
	 * @param keyCode The key to be checked.
	 * @return True if the key is held down, false if it is not.
	 */
	
	public boolean isKeyHeld(int keyCode) {
		return pressedKeys[keyCode];
	}
	
	/**
	 * Determines if a specific key has just been released.
	 * @param keyCode The key to be checked.
	 * @return True if the key is not pressed, false if it still is.
	 */
	
	public boolean isKeyUp(int keyCode) {
		/*
		 * This will return true if the key has been pressed in
		 * the last frame, but not in this frame.
		 */
		return !pressedKeys[keyCode] && pressedKeysLast[keyCode];
	}
	
	/**
	 * Determines if a specific mouse button has just been pressed.
	 * @param mouseButton The mouse button to be checked.
	 * @return True if the button is pressed, false if it is not.
	 */
	
	public boolean isButtonDown(int mouseButton) {
		/*
		 * This will return true if the button has been pressed in
		 * this frame, but not in the last frame.
		 */
		return pressedButtons[mouseButton] && !pressedButtonsLast[mouseButton];
	}
	
	/**
	 * Determines if a specific mouse button is held down.
	 * @param mouseButton The mouse button to be checked.
	 * @return True if the button is held down, false if it is not.
	 */
	
	public boolean isButtonHeld(int mouseButton) {
		return pressedButtons[mouseButton];
	}
	
	/**
	 * Determines if a specific mouse button has just been released.
	 * @param mouseButton The mouse button to be checked.
	 * @return True if the button is not pressed, false if it still is.
	 */
	
	public boolean isButtonUp(int mouseButton) {
		/*
		 * This will return true if the button has been pressed in
		 * the last frame, but not in this frame.
		 */
		return !pressedButtons[mouseButton] && pressedButtonsLast[mouseButton];
	}

	/**
	 * Calls the mouseClicked event for the current state.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		state.mouseClicked(e);
	}

	/**
	 * Handles the mousePressed event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		pressedButtons[e.getButton()] = true;
	}

	/**
	 * Handles the mouseReleased event.
	 */
	
	@Override
	public void mouseReleased(MouseEvent e) {
		pressedButtons[e.getButton()] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Handles the keyPressed event.
	 */
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() <= NUM_KEYS) {
			// Presses the key if it is valid.
			pressedKeys[e.getKeyCode()] = true;
		}
		
	}

	/**
	 * Handles the keyReleased event.
	 */
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() <= NUM_KEYS) {
			// Releases the key if it is valid.
			pressedKeys[e.getKeyCode()] = false;
		}
		
	}
	
	/**
	 * Updates the input class and updates the input arrays.
	 */
	
	public void update() {
		
		/*
		 * Sets the values of last frame's pressed keys
		 * to this frame's pressed keys.
		 */
		for (int index = 0; index < NUM_KEYS; index++) {
			pressedKeysLast[index] = pressedKeys[index];
		}
		
		/*
		 * Sets the values of last frame's pressed buttons
		 * to this frame's pressed buttons.
		 */
		
		for (int index = 0; index < NUM_BUTTONS; index++) {
			pressedButtonsLast[index] = pressedButtons[index];
		}
	}

}
