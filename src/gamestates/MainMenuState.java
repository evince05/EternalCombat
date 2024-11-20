package gamestates;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import game.Game;
import input.Input;
import ui.components.UIButton;
import ui.components.UILabel;
import ui.components.panels.ControlsPanel;
import ui.components.panels.InstructionsPanel;
import ui.components.panels.LeaderboardPanel;
import ui.containers.UIPanel;
import ui.events.ButtonClickAction;

/**
 * The MainMenuState is displayed when the game starts
 * and after the DeathState is displayed.
 * @author Elliott Vince
 *
 */
public class MainMenuState extends GameState {
	
	private Game game;
	
	// Image Paths
	private static final String BACKGROUND_PATH =
			"resources/backgrounds/mainmenu.jpg";
	
	private static final String BACK_BTN_PATH =
			"resources/ui/PrevButton.png";
	
	private static final int MENU_Y_MARGIN = 45;
	
	private Image bgImage;
	
	private BufferedImage titleLogo;
	
	private BufferedImage prevBtnImage;

	// Title Logo Positioning
	private int titleLogoX;
	private int titleLogoY;
	
	// UI Panels
	private UIPanel titlePanel;
	private UIPanel menuPanel;
	private LeaderboardPanel leaderboardPanel;
	private ControlsPanel controlsPanel;
	private InstructionsPanel instructionsPanel;
	
	private Input input;

	
	/**
	 * Creates a new MainMenuState.
	 * @param game An instance of the game class.
	 * @param input An instance of the input class.
	 */
	
	public MainMenuState(Game game, Input input) {
		this.game = game;
		this.input = input;
		init();
	}
	
	
	/**
	 * Initializes any variables that this state needs.
	 */
	
	@Override
	public void init() {
		// Initializes and scales the background image.
		this.bgImage = new ImageIcon(BACKGROUND_PATH).getImage()
				.getScaledInstance(Game.WIDTH, Game.HEIGHT, Image.SCALE_SMOOTH);
		
		// Initializes the title menu panel.
		this.titlePanel = new UIPanel();
		
		// Initializes the main menu panel.
		this.menuPanel = new UIPanel();
		
		// Initializes the leaderboard panel.
		this.leaderboardPanel = new LeaderboardPanel(game);
		
		// Initializes the controls panel.
		this.controlsPanel = new ControlsPanel();
		
		// Initializes the instructions panel.
		this.instructionsPanel = new InstructionsPanel();
		
		// Starts the soundtrack if it is not playing.
		if (!game.getSoundtrackPlayer().isPlaying() && 
				game.getSettings().isMusicEnabled()) {
			game.getSoundtrackPlayer().playNextSong();
		}
				
		/*
		 * Initializes any images that the components use.
		 */
		
		try {
			// Initializes the title logo
			this.titleLogo = ImageIO.read(new File("resources/ui/TitleLogo.png"));
			
			// Initializes the previous / next button images.
			this.prevBtnImage = ImageIO.read(new File(BACK_BTN_PATH));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Sets the location of the title logo
		titleLogoX = Game.WIDTH / 2 - titleLogo.getWidth() / 2;
		titleLogoY = Game.HEIGHT / 2 - titleLogo.getHeight() + 64;
		
		// Sets the location of the title panel.
		titlePanel.setLocation(Game.WIDTH / 2 - titlePanel.getWidth() / 2,
				Game.HEIGHT / 2 - titlePanel.getHeight() / 2 + 64);
		
		// Create and add the play button.
		UIButton playButton = new UIButton();
		titlePanel.add(playButton);
		
		// Sets the location of the play button.
		playButton.setLocation(playButton.getCenteredX(), 300);
		
		playButton.setText("Play");
		
		playButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				
				// Opens the game menu.
				titlePanel.setVisible(false);
				menuPanel.setVisible(true);
				
			}
			
		});
		
		/*
		 * Renders all of the components of the container 
		 * except for the container itself.
		 */
		
		titlePanel.setVisible(true);
		titlePanel.setImageVisibility(false);
		
		// Sets the location of the menuPanel.
		menuPanel.setLocation(Game.WIDTH / 2 - menuPanel.getWidth() / 2,
				Game.HEIGHT / 2 - menuPanel.getHeight() / 2);

		// Makes the panel hidden.
		menuPanel.setVisible(false);
		
		// Create and add the menu back button
		UIButton menuBackBtn = new UIButton();
		menuBackBtn.setImage(prevBtnImage);
		menuPanel.add(menuBackBtn);
		
		menuBackBtn.setLocation(12, 12);
		menuBackBtn.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Returns to the title screen.
				menuPanel.setVisible(false);
				titlePanel.setVisible(true);
			}
			
		});
		
		// Create and add the menu label
		UILabel menuLabel = new UILabel("Menu");
		menuPanel.add(menuLabel);
		menuLabel.setLocation(menuLabel.getCenteredX(), 
				menuPanel.getPosY() - 32);
		
		// Create and add the new game button.

		UIButton newGameButton = new UIButton();
		menuPanel.add(newGameButton);

		// Sets the location of the new game button.
		newGameButton.setLocation(newGameButton.getCenteredX(),
				MENU_Y_MARGIN + newGameButton.getHeight());

		// Set the text of the start button.
		newGameButton.setText("New Game");

		// Sets the action to occur when the button is clicked.
		newGameButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				
				/*
				 * Switches the game's current state to the PlayState.
				 * This starts a new game.
				 */
				game.setCurrentState(new PlayState(game, input));

			}

		});
		
		// Create and add the controls button.

		UIButton controlsButton = new UIButton();
		menuPanel.add(controlsButton);

		// Sets the location of the controls button.
		controlsButton.setLocation(controlsButton.getCenteredX(), 
				menuPanel.getHeight() / 2 - MENU_Y_MARGIN - 
				controlsButton.getHeight());

		// Set the text of the controls button.
		controlsButton.setText("Controls");

		// Sets the action to occur when the button is clicked.
		controlsButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				
				// Opens the controls panel.
				menuPanel.setVisible(false);
				controlsPanel.setVisible(true);
			}
		});
		
		// Create and add the how to play button.
		UIButton instructionsButton = new UIButton();
		menuPanel.add(instructionsButton);
		
		// Sets the location of the instructions button.
		instructionsButton.setLocation(instructionsButton.getCenteredX(),
				menuPanel.getHeight() / 2 -
				instructionsButton.getHeight() / 2);
		
		// Sets the text and font size of the instructions button.
		instructionsButton.setText("How to Play");
		instructionsButton.setFontSize(26);
		
		instructionsButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				
				// Opens the instructions (how to play) panel.
				menuPanel.setVisible(false);
				instructionsPanel.setVisible(true);
				
			}
			
		});
		
		// Create and add the leaderboard button.

		UIButton leaderboardButton = new UIButton();
		menuPanel.add(leaderboardButton);

		// Sets the location of the leaderboard button.
		leaderboardButton.setLocation(leaderboardButton.getCenteredX(), 
				menuPanel.getHeight() / 2 + MENU_Y_MARGIN);

		// Set the text of the leaderboard button.
		leaderboardButton.setText("Leaderboard");
		
		// Set the font size of the leaderboard button
		leaderboardButton.setFontSize(24);

		// Sets the action to occur when the button is clicked.
		leaderboardButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {

				// Opens the Leaderboard Panel.
				menuPanel.setVisible(false);
				leaderboardPanel.setVisible(true);
			}

		});
		
		// Create and add the exit button
		
		UIButton exitButton = new UIButton();
		menuPanel.add(exitButton);
		
		// Sets the location of the exit button.
		exitButton.setLocation(exitButton.getCenteredX(), menuPanel.getImage()
				.getHeight() - MENU_Y_MARGIN - exitButton.getHeight() * 2);
		
		// Sets the text of the exit button
		exitButton.setText("Exit Game");
		
		// Sets the action to occur when the button is clicked.
		
		exitButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Ends the program.
				System.exit(0);
				
			}
			
		});
		
		// --------------- Controls Panel ---------------

		// Sets the location of the controls panel.
		controlsPanel.setLocation(Game.WIDTH / 2 - controlsPanel.getWidth() / 2,
				Game.HEIGHT / 2 - controlsPanel.getHeight() / 2);

		// Hides the controls panel.
		controlsPanel.setVisible(false);

		// Creates the controls back button.

		UIButton controlsBackBtn = new UIButton();
		controlsBackBtn.setImage(prevBtnImage);
		controlsPanel.add(controlsBackBtn);

		controlsBackBtn.setLocation(12, 12);

		controlsBackBtn.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Returns to the menu.
				controlsPanel.setVisible(false);
				menuPanel.setVisible(true);
			}

		});

		// Creates the controls panel's title label.
		UILabel controlsTitle = new UILabel("Controls");
		controlsPanel.add(controlsTitle);

		controlsTitle.setLocation(controlsTitle.getCenteredX(), 12);
		
		// --------------- Instructions Panel ---------------
		
		// Sets the location of the instructions panel.
		instructionsPanel.setLocation(Game.WIDTH / 2 - instructionsPanel.getWidth() / 2,
				Game.HEIGHT / 2 - instructionsPanel.getHeight() / 2);
		
		// Hides the instructions panel.
		instructionsPanel.setVisible(false);
		
		// Create the back button.
		UIButton instructionsBackBtn = new UIButton();
		instructionsBackBtn.setImage(prevBtnImage);
		instructionsPanel.add(instructionsBackBtn);
		instructionsBackBtn.setLocation(12, 12);
		
		instructionsBackBtn.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Returns to the menu.
				instructionsPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		
		// Create the instructions title label.
		UILabel instructionsLabel = new UILabel("How to Play");
		instructionsLabel.setFontSize(20);
		instructionsPanel.add(instructionsLabel);
		
		// Sets the location of the label.
		instructionsLabel.setLocation(instructionsLabel.getCenteredX(), 12);
		

		// --------------- Leaderboard Panel ---------------
		
		// Sets the location of the leaderboard panel.
		leaderboardPanel.setLocation(Game.WIDTH / 2 - leaderboardPanel.getWidth() / 2,
				Game.HEIGHT / 2 - leaderboardPanel.getHeight() / 2);
		
		// Hides the leaderboard panel.
		leaderboardPanel.setVisible(false);
		
		// Create the leaderboard back button
		UIButton leaderboardBackBtn = new UIButton();
		leaderboardBackBtn.setImage(prevBtnImage);
		leaderboardPanel.add(leaderboardBackBtn);
		leaderboardBackBtn.setLocation(12, 12);
		
		leaderboardBackBtn.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Returns to the menu.
				leaderboardPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		
		// Create the leaderboard title label.
		UILabel leaderboardLabel = new UILabel("Leaderboard");
		leaderboardLabel.setFontSize(20);
		
		// Adds the label to the panel.
		leaderboardPanel.add(leaderboardLabel);
		
		// Sets the location of the label.
		leaderboardLabel.setLocation(leaderboardLabel.getCenteredX(), 
				12);
		
		// Create the leaderboard reset button.
		UIButton scoreResetButton = new UIButton();
		
		// Adds the reset button to the panel.
		leaderboardPanel.add(scoreResetButton);

		// Sets the location of the reset button.
		scoreResetButton.setLocation(scoreResetButton.getCenteredX(), 450);

		// Sets the text of the reset button.
		scoreResetButton.setText("Reset Scores");
		
		// Sets the font size of the reset button
		scoreResetButton.setFontSize(20);

		// Sets the action to occur when the button is clicked.

		scoreResetButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {

				// Resets the leaderboard.
				game.getLeaderboard().reset();
			}

		});
		
	}

	/**
	 * Handles key presses (esc and enter) to switch between menus.
	 */
	
	@Override
	public void update() {
		
		if (input.isKeyDown(KeyEvent.VK_ENTER) && !menuPanel.isVisible()) {
			
			// Goes to the menu panel.
			menuPanel.setVisible(true);
			titlePanel.setVisible(false);
		}
		else if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			if (menuPanel.isVisible()) {
				
				// Returns to the title screen.
				menuPanel.setVisible(false);
				titlePanel.setVisible(true);
			}
			
			else if (leaderboardPanel.isVisible() ||
					controlsPanel.isVisible() ||
					instructionsPanel.isVisible()) {
				
				// Returns to the menu screen.
				leaderboardPanel.setVisible(false);
				controlsPanel.setVisible(false);
				instructionsPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		}
	}

	/**
	 * Renders the components of the main menu state.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		
		/*
		 * Updates the current panel.
		 * This will render the panel that is currently visible.
		 * No more than one panel should be visible at a time.
		 */
		
		if (menuPanel.isVisible()) {
			// Renders the menu panel.
			menuPanel.render(graphics);
		}
		else if (titlePanel.isVisible()) {
			// Renders the title panel.
			titlePanel.render(graphics);
			
			// Draw the title logo
			graphics.drawImage(titleLogo, titleLogoX, titleLogoY, null);
		}
		else if (leaderboardPanel.isVisible()) {
			// Renders the leaderboard panel.
			leaderboardPanel.render(graphics);
		}
		else if (controlsPanel.isVisible()) {
			// Renders the controls panel.
			controlsPanel.render(graphics);
		}
		else if (instructionsPanel.isVisible()) {
			// Renders the instruction panel.
			instructionsPanel.render(graphics);
		}
		
	}

	/**
	 * Draws the background image of the Main Menu State.
	 */
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(bgImage, 0, 0, null);
	}

	/**
	 * Handles mouse clicks in the main menu state.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		/*
		 * Calls the mouseClicked method for the current panel.
		 * No more than one panel should be visible at a time.
		 * This will ensure that no methods are randomly called
		 * the mouse is clicked.
		 */
		
		if (menuPanel.isVisible()) {
			menuPanel.mouseClicked(e);
		}
		else if (titlePanel.isVisible()) {
			titlePanel.mouseClicked(e);
		}
		else if (leaderboardPanel.isVisible()) {
			leaderboardPanel.mouseClicked(e);
		}
		else if (controlsPanel.isVisible()) {
			controlsPanel.mouseClicked(e);
		}
		else if (instructionsPanel.isVisible()) {
			instructionsPanel.mouseClicked(e);
		}
	}
	
	/**
	 * Opens the leaderboard.
	 */

	public void openLeaderboard() {
		titlePanel.setVisible(false);
		menuPanel.setVisible(false);
		leaderboardPanel.setVisible(true);
	}
	
	/**
	 * Opens the navigation panel.
	 */
	
	public void openNavigationPanel() {
		titlePanel.setVisible(false);
		menuPanel.setVisible(true);
	}
	

}
