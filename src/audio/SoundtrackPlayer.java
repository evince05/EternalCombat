package audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.advanced.*;

/**
 * This class is used to open and play MP3 files.
 * A few modifications were made to the original class
 * (made by Casey Devet) so it could be implemented into
 * a soundtrack and queue system.
 * 
 * @author Elliott Vince (original file: Casey Devet
 * (casey.devet@sccdsb.net))
 */
public class SoundtrackPlayer
{
	
	private Soundtrack soundtrack;
	
	private Song currentSong;
	
	private String filename;
	
	private AdvancedPlayer player;

	private boolean openFlag = false;
	private boolean playing = false;

	/**
	 * Creates a soundtrack player.
	 * @param soundtrack An instance of the soundtrack class.
	 */
	public SoundtrackPlayer (Soundtrack soundtrack)
	{
		this.soundtrack = soundtrack;
		soundtrack.shuffle();
	}
	
	/**
	 * Loads and plays the next song in the soundtrack.
	 */
	
	public void playNextSong() {
		currentSong = soundtrack.getCurrentSong();
		
		// Loads the song at the current position.
		load(currentSong.getPath());
		player.setPlayBackListener(new FinishedHandler());
		play();
		
	}

	/**
	 * Loads an audio file.
	 * @param filename The audio file to be loaded.
	 */
	
	public void load(String filename)
	{
		this.filename = filename;
		
		try {
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new AdvancedPlayer(bis);
		}
		catch (Exception e) {
			System.out.println("Problem loading file " + filename);
			System.out.println(e);
		}
		openFlag = true;
	}

	/**
	 * Plays the MP3 file to the sound card.  If the file is not open, this method
	 * will open it first.
	 */
	public void play()
	{
		if (!openFlag)
		{
			load(filename);
		}
		// run in new thread to play in background
		new Thread() {
			public void run() {
				try { player.play(); }
				catch (Exception e) { System.out.println(e); }
			}
		}.start();
		playing = true;
	}

	/**
	 * Stops the playback.  The MP3 file is closed and must be opened to be
	 * played again.  Note that this does not pause the sound file, but stops it.
	 */
	public void stop ()
	{
		if (player != null)
		{
			player.close();
			openFlag = false;
		}
		playing = false;
	}

	/**
	 * Returns the state of the playback.
	 * @return true if the audio is playing and false otherwise.
	 */
	public boolean isPlaying ()
	{
		return playing;
	}

	private class FinishedHandler extends PlaybackListener
	{
		public void playbackFinished (PlaybackEvent evt)
		{
			
			player.close();

			// Plays the next song if the queue size is greater than 0.
			if (soundtrack.hasNextSong()) {
				soundtrack.nextSong();
				playNextSong();
			}
			else {
				// The queue has no remaining songs. Therefore, it is reset.
				soundtrack.reset();
				playNextSong();
			}
			playing = false;
		}
	}
}
