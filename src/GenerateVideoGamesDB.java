import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class GenerateVideoGamesDB {

	private static final String VIDEOGAMES_DB_NAME = "videogamesDB.dat";
	private static final String VIDEOGAMES_FILES = "videogames.txt";
	private static final String VIDEOGAMES_DIR = "VideoGames";
	private static VideoGamesDB videogamesDB;

	public static void main (String[] args) {
		try {
			videogamesDB = new VideoGamesDB (VIDEOGAMES_DB_NAME);
			loadFromFiles();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println ("Error generant base de dades!");
		}
	}

	private static void loadFromFiles() throws IOException {
		videogamesDB.reset();
		BufferedReader input = new BufferedReader (new FileReader (VIDEOGAMES_FILES));
		String fileName = input.readLine();
		while (fileName != null) {
			System.out.println ("Llegint " + fileName);
			VideoGameInfo vgi = VideoGameInfoReader.readVideoGameFile (VIDEOGAMES_DIR, fileName);
			videogamesDB.appendVideoGameInfo (vgi);
			fileName = input.readLine();
		}
		input.close();
		System.out.println ("Fet!");
	}

}
