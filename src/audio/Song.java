package audio;

/**
 * A simple enum of songs and their file paths.
 * @author Elliott Vince
 */

public enum Song {
	
	TRACK_1("Conquerors.mp3"),
	TRACK_2("EpicOrchestralFantasy.mp3"),
	TRACK_3("ForTheKing.mp3"),
	TRACK_4("LandOfFearless.mp3"),
	TRACK_5("NewSunrise.mp3"),
	TRACK_6("NoMoreMagic.mp3"),
	TRACK_7("oga_blaze.mp3"),
	TRACK_8("RegularBattle.mp3"),
	TRACK_9("SpiritOfValley.mp3"),
	TRACK_10("BossBattleMetal.mp3");
	
	private String path;
	
	/**
	 * A simple song object. <br>
	 * <b>Note:</b> The song must be located in the resources/audio/music folder.
	 * 
	 * @param The song's filename.
	 */
	
	private Song(String path) {
		// Initializes the song's filepath.
		this.path = "resources/audio/music/" + path;
	}
	
	/**
	 * Gets the path of the song.
	 */
	
	public String getPath() {
		return path;
	}
}
