package input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * The PlayerController handles the input for the player.
 * @author Elliott Vince
 */

public class PlayerController implements Controller {
	
	private Input input;
	
	/**
	 * Creates a new PlayerController object.
	 * @param input An instance of the input class.
	 */
	
	public PlayerController(Input input) {
		this.input = input;
	}

	/**
	 * Determines if either W or the Up Arrow is held.
	 * @return true If one of the specified keys is held, otherwise false.
	 */
	
	@Override
	public boolean isRequestingUp() {
		return input.isKeyHeld(KeyEvent.VK_W) ||
			input.isKeyHeld(KeyEvent.VK_UP);
	}
	
	/**
	 * Determines if either A or the Left Arrow is held.
	 * @return true If one of the specified keys is held, otherwise false.
	 */

	@Override
	public boolean isRequestingLeft() {
		return input.isKeyHeld(KeyEvent.VK_A) ||
			input.isKeyHeld(KeyEvent.VK_LEFT);
	}

	/**
	 * Determines if either S or the Down Arrow is held.
	 * @return true If one of the specified keys is held, otherwise false.
	 */
	
	@Override
	public boolean isRequestingDown() {
		return input.isKeyHeld(KeyEvent.VK_S) ||
			input.isKeyHeld(KeyEvent.VK_DOWN);
	}

	/**
	 * Determines if either D or the Right Arrow is held.
	 * @return true If one of the specified keys is held, otherwise false.
	 */
	
	@Override
	public boolean isRequestingRight() {
		return input.isKeyHeld(KeyEvent.VK_D) ||
			input.isKeyHeld(KeyEvent.VK_RIGHT);
	}

	/**
	 * Determines if the left click button is being clicked.
	 * @return true If the left click button is being clicked, otherwise false.
	 */
	
	@Override
	public boolean isRequestingLeftClick() {
		return input.isButtonDown(MouseEvent.BUTTON1) ||
			input.isButtonHeld(MouseEvent.BUTTON1);
	}

}
