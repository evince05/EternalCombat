package gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import game.Game;
import gameobjects.GameObject;
import gameobjects.player.Player;
import gameobjects.powerups.Powerup;
import gfx.Camera;
import input.Input;
import input.PlayerController;
import levels.LevelManager;
import ui.HUD;
import ui.components.UIButton;
import ui.components.UILabel;
import ui.containers.UIPanel;
import ui.events.ButtonClickAction;

/**
 * The game is composed of only one level. <br>
 * Therefore, the PlayState is where the game <br>
 * is played.
 * 
 * @author Elliott Vince
 */

public class PlayState extends GameState {
	
	private static final int PAUSE_MENU_Y_OFFSET = 30;
	
	// Game Class
	private Game game;
	
	// Input
	private Input input;
	
	// GameObjects
	private List<GameObject> gameObjects;
	private List<GameObject> objectsToRemove;
	private List<Powerup> powerups;
	private List<Powerup> powerupsToRemove;
	
	// Background Image;
	private BufferedImage background;
	
	// Player
	private Player player;
	
	// HUD
	private HUD hud;
	
	// Camera
	private Camera camera;
	
	// Map Bounds
	private Rectangle mapBounds;
	
	// Camera Bound
	private Rectangle cameraBounds;
	
	// Level Manager
	private LevelManager levelManager;
	
	// Pause
	private boolean paused;
	
	// Pause Menu
	private UIPanel pauseMenu;
	
	/**
	 * Creates the PlayState.
	 * @param game An instance of the game class.
	 * @param input An instance of the input class.
	 */
	
	public PlayState(Game game, Input input) {
		this.game = game;
		this.input = input;
		init();
		
	}
	
	/**
	 * Handles mouse clicking in the play state.
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (paused) {
			pauseMenu.mouseClicked(e);
		}
		
	}

	/**
	 * Updates the play state.
	 */
	
	@Override
	public void update() {
		
		// Toggles pause if the player clicks escape.
		if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			paused = !paused;

			// Updates the LevelManager's spawn timer.
			levelManager.updateLastSpawnTime();
			
		}
		
		/*
		 * The powerups need to be updated regardless if the game is
		 * paused so their timers can update.
		 */
		
		for (int i = 0; i < powerups.size(); i++) {
			Powerup powerup = powerups.get(i);
			powerup.update();
			
			if (paused) {
				powerup.updateSpawnTime();
			}
			
			if (powerupsToRemove.contains(powerup)) {
				
				// Removes the powerup from the playstate.
				powerups.remove(i);
				powerupsToRemove.remove(powerup);
			}
		}
		
		// The game updates if it is not paused.
		
		if (!paused) {
			
			// Updates each GameObject and removes it if necessary.
			for (int index = 0; index < gameObjects.size(); index++) {
				GameObject gameObject = gameObjects.get(index);
				gameObject.update();
				
				if (objectsToRemove.contains(gameObject)) {
					// Removes the object from the playstate.
					gameObjects.remove(index);
					objectsToRemove.remove(gameObject);
				}
				
				// Update the camera.
				camera.update();
				
				// Update the level manager
				levelManager.update();
				
				// Update the HUD.
				hud.update();
			}
		}
	}

	/**
	 * Draws all of the necessary components to the screen.
	 */
	
	@Override
	public void render(Graphics2D graphics) {
		for (int index = 0; index < gameObjects.size(); index++) {
			gameObjects.get(index).draw(graphics);
		}
		
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).draw(graphics);
		}
		
		/*
		 * Draws the HUD to the screen. This will be visible
		 * throughout the entire game, even when it is paused. 
		 */
		
		hud.render(graphics);
		
		if (paused) {
			// Draws a semi-opaque black background.
			graphics.setColor(new Color(0, 0, 0, 0.75f));
			graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			
			// Draws the pause menu
			pauseMenu.render(graphics);
		}
	}

	/**
	 * Draws the map image to the screen.
	 */
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(background, 0 - camera.getPosX(), 
				0 - camera.getPosY(), null);
	}

	/**
	 * Initializes the play state.
	 */
	
	@Override
	public void init() {
		
		// Initialize the background image
		try {
			background = ImageIO.read(new File("resources/backgrounds/gamebg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Initializes the bounds of the map and the camera.
		mapBounds = new Rectangle(191, 191, 1953, 1152);
		
		// The bounds of the camera are the dimensions of the image.
		cameraBounds = new Rectangle(background.getMinX(), background.getMinY(),
				background.getWidth(), background.getHeight());
		
		// Initialize the GameObject lists
		gameObjects = new ArrayList<GameObject>();
		objectsToRemove = new ArrayList<GameObject>();
		powerups = new ArrayList<Powerup>();
		powerupsToRemove = new ArrayList<Powerup>();
		
		// Create the player
		player = new Player(this, new PlayerController(input), 600, 600, 100, 100);
		gameObjects.add(player);
		
		camera = new Camera(this, Game.WIDTH, Game.HEIGHT);
		camera.setFocusedObject(player);
		
		// Initializes the LevelManager
		levelManager = new LevelManager(this);
		
		// Initializes the HUD.
		hud = new HUD(this);
		
		// Sets paused to false
		paused = false;
		
		// Initializes the pause menu.
		pauseMenu = new UIPanel();
		pauseMenu.setLocation(Game.WIDTH / 2 - pauseMenu.getImage().getWidth() / 2,
				Game.HEIGHT / 2 - pauseMenu.getImage().getHeight() / 2);
		
		// Initializes the pause menu label.
		UILabel pauseLabel = new UILabel("Pause Menu");
		pauseLabel.setFontSize(20);
		
		pauseMenu.add(pauseLabel);
		pauseLabel.setLocation(pauseLabel.getCenteredX(), 12);
		
		// Initializes the resume button.
		UIButton resumeButton = new UIButton();
		pauseMenu.add(resumeButton);
		resumeButton.setText("Resume");

		// Sets the location of the resume button.
		resumeButton.setLocation(resumeButton.getCenteredX(), pauseMenu.getHeight() / 2 - 
			resumeButton.getHeight() / 2 - PAUSE_MENU_Y_OFFSET * 2 - 
			resumeButton.getHeight() * 2);

		// Adds an action to the button.
		resumeButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Resumes the game
				paused = false;
				levelManager.updateLastSpawnTime();
			}

		});
		
		// Initializes the toggle sounds button.
		UIButton toggleMusicButton = new UIButton();
		pauseMenu.add(toggleMusicButton);
		toggleMusicButton.setText("Music: " + 
			(game.getSettings().isMusicEnabled() == true ? "On" : "Off"));

		// Set the location of the toggle music button.
		toggleMusicButton.setLocation(toggleMusicButton.getCenteredX(), 
			pauseMenu.getHeight() / 2 - toggleMusicButton.getHeight() / 2 - 
			PAUSE_MENU_Y_OFFSET - toggleMusicButton.getHeight());

		// Adds an action to the button
		toggleMusicButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Turns off the music.
				game.getSettings().toggleMusic(!game.getSettings().isMusicEnabled());
				
				// Updates the text.
				toggleMusicButton.setText("Music: " + 
					(game.getSettings().isMusicEnabled() == true ? "On" : "Off"));
			}

		});

		// Initializes the toggle sounds button.
		UIButton toggleSoundsButton = new UIButton();
		pauseMenu.add(toggleSoundsButton);
		toggleSoundsButton.setText("Sounds: " + 
				(game.getSettings().areSoundsEnabled() == true ? "On" : "Off"));

		// Set the location of the toggle sounds button.
		toggleSoundsButton.setLocation(toggleSoundsButton.getCenteredX(), 
				pauseMenu.getHeight() / 2 - toggleSoundsButton.getHeight() / 2);

		// Adds an action to the button
		toggleSoundsButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Turns off the sounds.
				game.getSettings().toggleSounds(!game.getSettings().areSoundsEnabled());

				// Updates the text.
				toggleSoundsButton.setText("Sounds: " + 
					(game.getSettings().areSoundsEnabled() == true ? "On" : "Off"));
			}

		});

		// Initializes the main menu button.
		UIButton mainMenuButton = new UIButton();
		pauseMenu.add(mainMenuButton);
		mainMenuButton.setText("Main Menu");
		
		// Set the location of the main menu button.
		mainMenuButton.setLocation(mainMenuButton.getCenteredX(), pauseMenu.getHeight() / 2
			+ mainMenuButton.getHeight() / 2  + PAUSE_MENU_Y_OFFSET);
		
		// Adds an action to the button
		mainMenuButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				// Ends the game and returns to the main menu.
				game.setCurrentState(new MainMenuState(game, input));
			}
			
		});
		
		// Initializes the exit button.
		UIButton exitButton = new UIButton();
		pauseMenu.add(exitButton);
		exitButton.setText("Exit Game");
		
		// Sets the location of the exit button.
		exitButton.setLocation(exitButton.getCenteredX(), pauseMenu.getHeight() / 2
			+ mainMenuButton.getHeight() / 2  + PAUSE_MENU_Y_OFFSET * 2 + 
			exitButton.getHeight());
		
		// Adds an action to the button.
		exitButton.setActionOnClick(new ButtonClickAction() {

			@Override
			public void run() {
				System.exit(0);
				
			}
			
		});
		
	}
	
	/**
	 * Gets the player.
	 */
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the list of game objects that are currently
	 * in the playstate.
	 */
	
	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	/**
	 * Gets the list of game objects to be removed from the
	 * playstate.
	 */
	
	public List<GameObject> getObjectsToRemove() {
		return objectsToRemove;
	}
	
	/**
	 * Adds the specified gameobject to the playstate.
	 * @param gameObject The object to be added.
	 */
	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}
	
	/**
	 * Removes the specified gameobject from the playstate.
	 * @param gameObject The gameobject to be removed.
	 */
	public void removeGameObject(GameObject gameObject) {
		objectsToRemove.add(gameObject);
	}
	
	/**
	 * Adds the specified powerup to the playstate.
	 * @param powerup The powerup to be added.
	 */
	public void addPowerup(Powerup powerup) {
		powerups.add(powerup);
	}
	
	/**
	 * Removes the specified powerup from the playstate.
	 * @param powerup The powerup to be removed.
	 */
	
	public void removePowerup(Powerup powerup) {
		powerupsToRemove.add(powerup);
	}
	
	/**
	 * Gets the camera. This method will be useful when 
	 * rendering objects to the screen.
	 */
	
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Gets the bounds of the map. <br>
	 * This will be used to ensure that
	 * gameobjects do not walk off of the
	 * screen.
	 */
	
	public Rectangle getBounds() {
		return mapBounds;
	}
	
	/**
	 * Gets the bounds of the camera. <br>
	 * This will be used to ensure that
	 * the camera will not move if it shows <br>
	 * any blank parts of the screen.
	 */
	
	public Rectangle getCameraBounds() {
		return cameraBounds;
	}
	
	/**
	 * Gets an instance of the game class.
	 */
	
	public Game getGame() {
		return game;
	}
	
	/**
	 * Gets an instance of the LevelManager class.
	 */
	
	public LevelManager getLevelManager() {
		return levelManager;
	}

}
