package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import game.Game;
import input.Input;

/**
 * The Death State is displayed when the player dies. <br>
 * It handles the leaderboard system, as well as returning to <br>
 * the main menu.
 * 
 * @author Elliott Vince
 *
 */
public class DeathState extends GameState {
	
	private static final Color BACKGROUND_COLOR = Color.BLACK;
	
	// The default text color.
	private static final Color TEXT_COLOR = Color.RED;
	
	// The default font.
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 60);
	
	// How long this screen will be displayed (in seconds).
	private static final int DISPLAY_TIME = 5;
	
	private PlayState playState;
	private Game game;
	private Input input;
	
	private String username;
	
	// The time when this screen was first shown (in milliseconds).
	private long startTime;
	
	// The elapsed time since the start time (in seconds).
	private int elapsedTime;
	
	/**
	 * Creates a new DeathState. This occurs when the player dies.
	 * @param playState An instance of the playstate which just ended.
	 * @param game An instance of the game class.
	 * @param input An instance of the input class.
	 */
	
	public DeathState(PlayState playState, Game game, Input input) {
		this.playState = playState;
		this.game = game;
		this.input = input;
		
		// Initializes the death state.
		init();
	}
	
	/**
	 * Gets the player to input a name using an input dialog.
	 * @return The player's name. 
	 */
	
	public String getName() {
		return JOptionPane.showInputDialog(game.getFrame(), "Enter your name", 
			"Leaderboard", JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Initializes any variables and fields needed.
	 */
	
	@Override
	public void init() {
		this.username = null;
		
		// Initializes the start time.
		startTime = System.currentTimeMillis();
	}

	/**
	 * Updates the death state.
	 */
	
	@Override
	public void update() {
		
		/*
		 * The game over screen is displayed for 5 seconds
		 * before returning to the home screen or updating
		 * the leaderboard.
		 */
		
		// Calculates how much time has elapsed since the start time.
		elapsedTime = (int) (System.currentTimeMillis() - startTime) / 1000;
		
		if (elapsedTime >= DISPLAY_TIME) {
			/*
			 * Either opens the leaderboard or returns to
			 * the main menu if the screen has been displayed.
			 * for enough time.
			 */
			
			MainMenuState menu = new MainMenuState(game, input);
			
			if (username == null && playState.getPlayer().getScore() >
					game.getLeaderboard().getScore(9)) {
		
				/*
				 * Asks the user to input a name if their score will
				 * be added to the leaderboard. 
				 */
				username = getName();
				
				/*
				 * Opens the game's leaderboard panel if the player
				 * achieved a high score.
				 */
				
				if (game.getLeaderboard().sortNewEntry(username, 
						playState.getPlayer().getScore())) {
					 
					// Saves the leaderboard to the file.
					game.getLeaderboard().saveToFile();
					
					// Opens the leaderboard.
					menu.openLeaderboard();
				
				}
			}
			else {
				/*
				 * Opens the game's navigation panel if the player
				 * did not get a high score.
				 */
				menu.openNavigationPanel();
			}
			
			// Returns to the main menu.
			game.setCurrentState(menu);
		}
	}

	/**
	 * Renders the "Game Over" text to the screen.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		// Sets the color and font of the graphics.
		graphics.setColor(TEXT_COLOR);
		graphics.setFont(DEFAULT_FONT);
		
		String text = "Game Over";
		
		// Calculates the width and height of the text.
		int textWidth = graphics.getFontMetrics().stringWidth(text);
		int textHeight = graphics.getFontMetrics().getHeight();
		
		// Draws the text to the screen.
		graphics.drawString(text, Game.WIDTH / 2 - textWidth / 2, 
				Game.HEIGHT / 2 - textHeight / 2);
		
	}

	/**
	 * Draws the black background to the screen.
	 */
	
	@Override
	public void draw(Graphics2D graphics) {
		// Draws the background.
		graphics.setColor(BACKGROUND_COLOR);
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
	}

	/**
	 * Handles mouse click events, but this method is not used
	 * in this GameState.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

}
