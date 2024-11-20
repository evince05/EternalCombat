/*
 * Programmer: Elliott Vince
 * Date: November 16, 2020
 * Purpose: Defeat endless waves of skeletons in this 2D Java game!
 */

package game;

/**
 * The main class of Eternal Combat.
 * This class launches a new game.
 * @author Elliott Vince
 */

public class GameLauncher {
	
	public static void main(String[] args) {
		
		// Starts and opens a new game.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Thread(new Game()).start();
			}
		});
	}

}
