package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import audio.Soundtrack;
import audio.SoundtrackPlayer;
import gamestates.GameState;
import gamestates.MainMenuState;
import input.Input;
import util.Methods;

/**
 * The game class is essentialy the main class of the program,
 * but it does not contain the main method. An instance of this
 * class is created in the GameLauncher class, and this class starts
 * and manages the game.
 * 
 * @author Elliott Vince
 */

public class Game implements Runnable {
	
	// Frame and Canvas
	private JFrame frame;
	private Canvas canvas;
	
	// Window Size
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	// Game Loop Variables
	private boolean running;
	private static final double UPDATES_PER_SECOND = 1.0 / 60.0;
	private int fps;
	
	// Input
	private Input input;
	
	// The current state of the game.
	private GameState currentState;
	
	// A List of Icons.
	private List<Image> icons;
	
	// App Icons.
	private Image icon_16x16;
	private Image icon_32x32;
	private Image icon_48x48;
	private Image icon_64x64;
	
	// Leaderboard
	private Leaderboard leaderboard;
	
	// Soundtraack
	private SoundtrackPlayer soundtrackPlayer;
	
	// Settings
	private Settings settings;
	
	/**
	 * Creates a new game. To launch the game, call the {@link #run}
	 * method.
	 */
	
	public Game() {
		
		/**
		 * Initializes the frame and creates a new canvas.
		 */
		
		frame = new JFrame();
		frame.setTitle("Eternal Combat");
		canvas = new Canvas();
		canvas.setSize(new Dimension(WIDTH, HEIGHT));
		frame.add(canvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
		frame.setLocationRelativeTo(null);
		
		// Creates a buffer strategy. This prevents animation flickering
		canvas.createBufferStrategy(2);
		
		// Focuses the keyboard and mouse input on the canvas.
		canvas.setFocusable(true);
		canvas.requestFocus();
		
		init();
		
		frame.setIconImages(icons);
	}
	
	/**
	 * Initializes any variables that still haven't been intialized.
	 */
	
	private void init() {
		/*
		 * Initializes the icons. The default icon is 32px by 32px,
		 * and the other icons are scaled to fit other dimensions.
		 */
		
		icon_32x32 = new ImageIcon("resources/icons/icon.png").getImage();
			
		icon_16x16 = Methods.scaleImage(icon_32x32, 16, 16);
		
		icon_48x48 = Methods.scaleImage(icon_32x32, 48, 48);
			
		icon_64x64 = Methods.scaleImage(icon_32x32, 64, 64);
		
		icons = new ArrayList<Image>();
		icons.add(icon_16x16);
		icons.add(icon_32x32);
		icons.add(icon_48x48);
		icons.add(icon_64x64);
		
		// Initializes the leaderboard
		leaderboard = new Leaderboard();
		
		running = true;
		
		// Creates the input and adds the listeners.
		input = new Input();
		canvas.addKeyListener(input);
		canvas.addMouseListener(input);
		
		// Loads the game's settings.
		settings = new Settings(this);
		
		// Initializes the soundtrack player.
		
		soundtrackPlayer = new SoundtrackPlayer(new Soundtrack());
		if (settings.isMusicEnabled()) {
			
			// Creates and starts the soundtrack
			soundtrackPlayer.playNextSong();
		}
		
		
		// Sets the current state to the main menu.
		currentState = new MainMenuState(this, input);
		input.setGameState(currentState);
		
		
	}
	
	/**
	 * Updates the current state of the game and the input
	 * class.
	 */
	
	public void update() {
		currentState.update();
		input.update();
	}
	
	/**
	 * Renders, draws, and updates the game approximately
	 * 60 times per second. This can drop depending on the
	 * computer's specs and strain on system resources.
	 */

	@Override
	public void run() {

		/*
		 * Note: This game loop uses Majoolwip's game loop.
		 * https://www.youtube.com/watch?v=4iPEjFUZNsw&list=PL7dwpoQd3a8j6C9p5LqHzYFSkii6iWPZF
		 */

		double firstTime = 0;
		// converts to seconds because System.nanoTime is extremely accurate.
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;

		double frameTime = 0;
		int frames = 0;
		fps = 0;

		boolean render = false;

		// Continuously repeats this loop until the game is stopped.
		while (running) {
			render = false;
			firstTime = System.nanoTime() / 1000000000.0; // converts to seconds.

			// Gets how many milliseconds have passed between now and the last time.
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			// Adds the passed time to the unprocessed time.
			unprocessedTime += passedTime;
			frameTime += passedTime;

			while (unprocessedTime >= UPDATES_PER_SECOND) {
				unprocessedTime -= UPDATES_PER_SECOND;
				// Sets render to true. This makes the game render only when it updates
				render = true;

				// Updates the FPS count.
				if (frameTime >= 1.0) {
					frameTime = 0;
					fps = frames;
					frames = 0;
				}
				// Updates the game.
				update();
			}

			if (render) {
				// Renders the game.
				frames++;
				render();
			}
			else {

				/*
				 * If the game doesn't need to render or update,
				 * the GameLoop thread sleeps for 1 millisecond,
				 * causing a decrease in CPU usage.
				 */

				try {
					Thread.sleep(1);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Draws the FPS at the bottom of the screen.
	 * @param graphics The draw graphics of the canvas.
	 */
	
	private void drawFPS(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.PLAIN, 16));
		graphics.drawString("FPS: " + fps, 10, HEIGHT - 10);
	}
		
	/**
	 * Renders all graphic components to the screen.
	 */

	public void render() {

		/*
		 * Gets the buffer strategy of the canvas and gets its 
		 * graphics object. This will be used to render components
		 * to the screen.
		 */
		
		BufferStrategy bufferStrategy = canvas.getBufferStrategy();
		Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Draws the current state.
		currentState.draw(graphics);
		
		// Draws the FPS count to the screen.
		drawFPS(graphics);
		
		// Renders each game object to the screen.
		currentState.render(graphics);

		/*
		 * Disposes of the graphics object once every 
		 * component has been rendered.
		 */
		
		graphics.dispose();
		
		// Displays the next buffer.
		bufferStrategy.show();
	}
	
	/**
	 * Gets the game's JFrame.
	 */
	
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Gets the game's canvas.
	 */

	public Canvas getCanvas() {
		return canvas;
	}
	
	/**
	 * Sets the current state of the game. <br>
	 * This can be used to navigate between the main menu and <br>
	 * the game itself.
	 * 
	 * @param state The new state of the game.
	 */
	
	public void setCurrentState(GameState state) {
		this.currentState = state;
		
		// Sets the input's focused state.
		input.setGameState(state);
	}
	
	/**
	 * Gets an instance of the leaderboard class.
	 */
	
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
	
	/**
	 * Gets an instance of the input class.
	 */
	
	public Input getInput() {
		return input;
	}
	
	/**
	 * Gets the game's soundtrack player.
	 */
	
	public SoundtrackPlayer getSoundtrackPlayer() {
		return soundtrackPlayer;
	}
	
	/**
	 * Gets the game's settings.
	 */
	
	public Settings getSettings() {
		return settings;
	}
	 
}
