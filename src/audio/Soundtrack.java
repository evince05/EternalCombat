package audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles and manages the soundtrack of the game.
 * The soundtrack can be disabled in the game's
 * settings menu.
 * 
 * @author Elliott Vince
 */

public class Soundtrack {
	
	private List<Song> queue;
	private int remainingSongs;
	
	/**
	 * Creates a new soundtrack object.
	 */
	
	public Soundtrack() {
		// Initializes the queue of songs.
		queue = new ArrayList<Song>();
		// Resets the soundtrack.
		reset();
	}
	
	/**
	 * Resets the soundtrack.
	 * This refills the queue and automatically shuffles it.
	 */
	
	public void reset() {
		// Adds each song to the queue.
		for (int index = 0; index < Song.values().length; index++) {
			queue.add(Song.values()[index]);
		}

		// Shuffles the queue.
		remainingSongs = queue.size();
		shuffle();
	}
	
	/**
	 * Gets the current song.
	 */
	
	public Song getCurrentSong() {
		return queue.get(0);
	}
	
	/**
	 * Shuffles the queue.
	 */
	
	public void shuffle() {
		Collections.shuffle(queue);
	}
	
	/**
	 * Moves to the next song in the queue.
	 */
	
	public void nextSong() {
		
		if (hasNextSong()) {
			
			// Removes the last played song.
			queue.remove(0);
			remainingSongs--;
		}
	}
	
	/**
	 * Determines if the queue has any remaining songs.
	 * @return true If the queue has any remaining songs, otherwise false.
	 */
	
	public boolean hasNextSong() {
		return remainingSongs - 1 > 0;
	}

}
