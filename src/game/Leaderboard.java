package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * The leaderboard stores a list of the 10 best scores
 * that have been achieved in the game.
 * 
 * @author Elliott Vince
 */

public class Leaderboard {
	
	// The path of the leaderboard file.
	private static final String FILE_PATH =
			"resources/data/leaderboard.txt";
	
	// How many scores are stored in the leaderboard.
	public static final int NUM_SCORES = 10;
	
	private File dataFile;
	
	private Scanner scanner;
	
	private String[] usernames;
	private int[] scores;
	
	/**
	 * Creates a new leaderboard.
	 */
	
	public Leaderboard() {
		// Initializes the leaderboard.
		init();
	}
	
	/**
	 * Initializes any of the required variables and objects
	 * needed for this class.
	 */
	
	private void init() {
		// Gets and initializes the leaderboard file.
		dataFile = new File(FILE_PATH);
		
		// Initializes the arrays
		usernames = new String[NUM_SCORES];
		scores = new int[NUM_SCORES];
		
		// Creates the scanner for the text file.
		try {
			scanner = new Scanner(dataFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Updates and sorts the entries.
		updateScores();
		sortEntries();
	}
	
	/**
	 * Gets the name at the specified index.
	 * @param index The index of the name.
	 */
	
	public String getName(int index) {
		
		if (index >= 0 && index < NUM_SCORES) {
			// Gets the name at the specified index if it is valid.
			return usernames[index];
		}
		return null;
	}
	
	/**
	 * Gets the score at the specified index.
	 * @param index The index of the score.
	 */
	
	public int getScore(int index) {
		
		if (index >= 0 && index < NUM_SCORES) {
			// Gets the score at the specified index if it is valid.
			return scores[index];
		}
		return 0;
	}
	
	/**
	 * Resets the leaderboard.
	 */
	
	public void reset() {
		// Resets the contents of the file.
		try {
			new PrintWriter(dataFile).close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Updates the leaderboard's arrays.
		updateScores();
	}
	
	/**
	 * Compares the specified entry to the entries of the <br>
	 * leaderboard and sorts the array using the bubble sort algorithm.
	 * 
	 * @param username The username associated with the entry.
	 * @param score The score associated with the entry.
	 * 
	 * @return True if the entry was added to the leaderboard,
	 * false if it was not.
	 *
	 */
	
	public boolean sortNewEntry(String username, int score) {
		
		boolean addedToLeaderboard = false;
		
		for (int i = NUM_SCORES - 1; i >= 0; i--) {
			if (score > scores[i]) {
				/*
				 * This method will return true if the entry's score
				 * is greater than any of the scores in the leaderboard. 
				 */
				
				scores[i] = score;
				usernames[i] = username;
				addedToLeaderboard = true;
				break;
				
			}
		}
		
		// Sorts the list of entries.
		sortEntries();
		
		return addedToLeaderboard;
	}
	
	/**
	 * Sorts the entries of the leaderboard in order from
	 * greatest to smallest using the bubble sort algorithm.
	 */
	
	public void sortEntries() {
		
		for (int pass = 0; pass < NUM_SCORES - 1; pass++) {

			boolean isChange = false;
			
			for (int index = NUM_SCORES - 1; index > 0; index--) {

				/*
				 * Swaps the entries if the score of the entry 
				 * is greater than the score of the next entry.
				 */
				
				if (scores[index] > scores[index - 1]) {

					// Creates temporarily variables so the entries can swap.
					String tempName = usernames[index - 1];
					int tempScore = scores[index - 1];

					// Swaps the entries.
					usernames[index - 1] = usernames[index];
					scores[index - 1] = scores[index];

					usernames[index] = tempName;
					scores[index] = tempScore;

					isChange = true;

				}
			}

			if (!isChange) {
				/*
				 * No changes have occured throughout the pass, therefore
				 * the loop will short-circuit.
				 */
				break;
			}

		}
	}
	
	
	/**
	 * Updates the scores and the leaderboard's arrays 
	 * from the file.
	 */
	
	private void updateScores() {
		// Fills the array from the file's data.
		
		int index = 0;
		boolean isChange = false;
		while (scanner.hasNextLine()) {
			
			String entry = scanner.nextLine();
			
			if (!entry.equals("")) {
				/**
				 * Splits the string by the regex ": ".
				 * The scores are formatted in the data like so:
				 * Username: Score
				 * 
				 * This means that index 0 is the username and index 1
				 * is their score.
				 */

				String[] args = entry.split(": ");
				// Gets the name and score of the entry.
				String name = args[0];
				int score = Integer.parseInt(args[1]);

				// Assigns the name and score to the current index.
				usernames[index] = name;
				scores[index] = score;
				
				index++;
				
				isChange = true;
			}
		}
		
		if (!isChange) {
			
			// Sets all entries to null if the file is empty.
			
			for (int i = 0; i < NUM_SCORES; i++) {
				usernames[i] = null;
				scores[i] = 0;
			}
		}
		else {
			
		}

				
	}
	
	/**
	 * Saves the leaderboard to the file.
	 */
	
	public void saveToFile() {
		// Resets the contents of the file.
		
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(dataFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Writes each entry to the file.
		for (int index = 0; index < NUM_SCORES; index++) {
			writer.write(usernames[index] + ": " + scores[index] + "\n");
		}
		
		writer.close();
		
		
	}

}
