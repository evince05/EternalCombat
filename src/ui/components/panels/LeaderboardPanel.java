package ui.components.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.Game;
import game.Leaderboard;
import ui.containers.UIPanel;

/**
 * The leaderboard panel is a panel that automatically
 * displays the data of the leaderboard.
 * 
 * @author Elliott Vince
 */

public class LeaderboardPanel extends UIPanel {
	
	// Text Positioning and Properties
	private static final int TEXT_MARGIN_Y = 100;
	private static final int TEXT_OFFSET_Y = 10;
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);
	
	private Leaderboard leaderboard;
	private Game game;
	
	/**
	 * Creates a new leaderboard panel.
	 * @param game An instance of the game class.
	 */
	
	public LeaderboardPanel(Game game) {
		this.game = game;
		this.leaderboard = game.getLeaderboard();
		
		// Calls the parent class's init method.
		super.init();
	}
	
	/**
	 * Renders the panel and the leaderboard.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		super.render(graphics);
		
		leaderboard = game.getLeaderboard();
		
		graphics.setColor(TEXT_COLOR);
		graphics.setFont(DEFAULT_FONT);
		
		for (int index = 0; index < Leaderboard.NUM_SCORES; index++) {
			
			// Formats the text.
			String text = "#" + (index + 1) + ": " + leaderboard.getName(index) 
				+ ": " + leaderboard.getScore(index);
			
			// Calculates the text's x and y location
			int textPosX = width / 2 - 
					graphics.getFontMetrics().stringWidth(text) / 2;

			int textPosY = TEXT_MARGIN_Y + TEXT_OFFSET_Y * index +
					graphics.getFontMetrics().getHeight() * index;
			
			// Draws the text to the screen.
			graphics.drawString(text, posX + textPosX, posY + textPosY);
			
		}
	}

}
