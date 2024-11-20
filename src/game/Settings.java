package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * The settings class stores and retrieves a list of settings from
 * a specific file.
 * @author Elliott Vince
 */

public class Settings {
	
	private static final String FILE_PATH = 
			"resources/data/settings.properties";
	
	private Game game;
	
	// Settings variables
	private boolean playMusic;
	private boolean playSounds;
	
	/**
	 * Creates a new settings object.
	 * @param game An instance of the game class.
	 */
	
	public Settings(Game game) {
		
		// Initializes the game class.
		this.game = game;
		
		// Loads the settings file.
		load();
		
	}
	
	/**
	 * Loads the settings file.
	 */
	
	private void load() {
		try {
			// Loads the file.
			InputStream inputStream = new FileInputStream(FILE_PATH);
			Properties properties = new Properties();
			properties.load(inputStream);
			
			// Loads the properties from the file.
			if (properties.getProperty("music").equalsIgnoreCase("on")) {
				playMusic = true;
			}
			else {
				playMusic = false;
			}
			
			if (properties.getProperty("sounds").equalsIgnoreCase("on")) {
				playSounds = true;
			}
			else {
				playSounds = false;
			}
		}
		catch (IOException e) {
			// If an error occurs, a new default settings file is created.
			createSettings();
		}
	}
	
	/**
	 * Saves the settings file.
	 */
	
	private void save() {
		
		try {
			OutputStream outputStream = new FileOutputStream(FILE_PATH);
			Properties properties = new Properties();
			
			if (playMusic) {
				properties.setProperty("music", "on");
			}
			else {
				properties.setProperty("music", "off");
			}
			
			if (playSounds) {
				properties.setProperty("sounds", "on");
			}
			else {
				properties.setProperty("sounds", "off");
			}
			
			properties.store(outputStream, null);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new settings file with the default settings.
	 */
	
	private void createSettings() {
		
		try {
			// Creates a new file at the specified location.
			File file = new File(FILE_PATH);
			file.createNewFile();
			
			OutputStream outputStream = new FileOutputStream(FILE_PATH);
			
			// Create a new properties object and stores the settings.
			Properties properties = new Properties();
			properties.put("music", "on");
			properties.put("sounds", "on");
			
			// Saves the properties file to the output stream.
			properties.store(outputStream, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determines whether music should play.
	 * @return True if music should play, otherwise false.
	 */
	
	public boolean isMusicEnabled() {
		return playMusic;
	}
	
	/**
	 * Determines whether sound effects should play.
	 * @return True if sounds should play, otherwise false.
	 */
	
	public boolean areSoundsEnabled() {
		return playSounds;
	}
	
	/**
	 * Sets whether music should play.
	 * @param playMusic True if music should play, otherwise false.
	 */
	
	public void toggleMusic(boolean playMusic) {
		this.playMusic = playMusic;
		// Saves the settings file.
		save();
		
		if (!playMusic) {
			// Stops the soundtrack player if it is playing.
			if (game.getSoundtrackPlayer().isPlaying()) {
				game.getSoundtrackPlayer().stop();
			}
		}
		else {
			// Restarts the soundtrack player if it is not playing.
			if (!game.getSoundtrackPlayer().isPlaying()) {
				game.getSoundtrackPlayer().playNextSong();
			}
		}
		
	}
	
	/**
	 * Sets whether sound effects should play.
	 * @param playSounds True if sound effects should play, otherwise false.
	 */
	
	public void toggleSounds(boolean playSounds) {
		this.playSounds = playSounds;
		
		// Saves the settings file.
		save();
	}
}
