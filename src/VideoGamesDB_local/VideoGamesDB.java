import java.io.RandomAccessFile;
import java.io.IOException;

public class VideoGamesDB {

	private RandomAccessFile videoGamesDB;
	private int numVideoGames;

	public VideoGamesDB (String fileName) throws IOException {
		videoGamesDB = new RandomAccessFile (fileName, "rw");
		numVideoGames = (int)videoGamesDB.length() / VideoGameInfo.SIZE;
	}

	public int getNumVideoGames() {
		return numVideoGames;
	}

	public void close() throws IOException {
		videoGamesDB.close();
	}

	public void reset() throws IOException {
		videoGamesDB.setLength (0);
		numVideoGames = 0;
	}

	public VideoGameInfo readVideoGameInfo (int n) throws IOException {
		videoGamesDB.seek (n * VideoGameInfo.SIZE);
		byte[] record = new byte[VideoGameInfo.SIZE];
		videoGamesDB.read (record);
		return VideoGameInfo.fromBytes (record);
	}

	public int searchVideoGame (String str) throws IOException {
		int titleIndex = -1;
		int titlePartIndex = -1;

		str = str.trim().toLowerCase();
		for (int i = numVideoGames - 1; i >= 0; i--) {
			VideoGameInfo vgi = readVideoGameInfo (i);
			String title = vgi.getTitle().toLowerCase();
			if (title.equals   (str)) { titleIndex     = i; }
			if (title.contains (str)) { titlePartIndex = i; }
		}
		if (titleIndex     >= 0) { return titleIndex;     }
		if (titlePartIndex >= 0) { return titlePartIndex; }
		return -1; // Not found
	}

	public void writeVideoGameInfo (int n, VideoGameInfo vgi) throws IOException {
		videoGamesDB.seek (n * VideoGameInfo.SIZE);
		byte[] record = vgi.toBytes();
		videoGamesDB.write (record);
	}

	public void appendVideoGameInfo (VideoGameInfo vgi) throws IOException {
		writeVideoGameInfo (numVideoGames, vgi);
		numVideoGames++;
	}

	public boolean insertVideoGame (VideoGameInfo vgi) throws IOException {
		int n = searchVideoGame (vgi.getTitle());
		if (n == -1) {
			appendVideoGameInfo (vgi);
			return true;
		}
		return false; // The videogame was already in the file
	}

	public boolean deleteVideoGame (String str) throws IOException {
		int n = searchVideoGame (str);
		if (n >= 0) {
			deleteVideoGame (n);
			return true;
		}
		return false; // Not found
	}

	private void deleteVideoGame (int n) throws IOException {
		VideoGameInfo lastVideoGame = readVideoGameInfo (numVideoGames - 1);
		writeVideoGameInfo (n, lastVideoGame);
		videoGamesDB.setLength ((numVideoGames - 1) * VideoGameInfo.SIZE);
		numVideoGames--;
	}

}
