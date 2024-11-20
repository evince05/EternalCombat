package gamestates;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * GameStates will be used to handle different <br>
 * screens and parts of the game, such as the main menu, <br>
 * the pause menu, and the play state.
 * <br> <br>
 * Each gamestate has an update, a render and a draw <br>
 * method that will <br>
 * be called every time the game updates. <br>
 * 
 * In the game class, there is a GameState object called currentState, <br>
 * which is the only game state that will be updated every update.
 * 
 * @author Elliott Vince
 */

public abstract class GameState {

	/**
	 * Any variables used in the game state should
	 * be initialzed here.
	 */
	
	public abstract void init();
	
	/**
	 * Updates to GameObjects, HUD, UI components, <br>
	 * etc. should be done here.
	 */
	
	public abstract void update();
	
	/**
	 * Any GameObjects or UI components that need to be drawn every <br>
	 * update should be drawn here.
	 */
	public abstract void render(Graphics2D graphics);
	
	/**
	 * All components that should be added to the gamestate by default <br>
	 * (eg. HUD, background, etc) should be drawn to the screen here. <br>
	 * This method is only called once.
	 */
	
	public abstract void draw(Graphics2D graphics);
	
	/**
	 * This method is called whenever the mouse ic clicked.
	 */
	
	public abstract void mouseClicked(MouseEvent e);

}
