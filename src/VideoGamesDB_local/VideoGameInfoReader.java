import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class VideoGameInfoReader {

	public static VideoGameInfo readVideoGameFile (String dirName, String fileName) throws IOException {
		File file = new File (dirName, fileName);
		BufferedReader input = new BufferedReader (new FileReader (file));

		String title = input.readLine();
		if (title == null) { title = ""; }
		else { title = title.trim(); }
		if (title.isEmpty()) {
			System.err.println ("Error: Títol buit al fitxer " + fileName);
			throw new IOException ("Títol buit al fitxer " + fileName);
		}

		String series = input.readLine();
		if (series == null) { series = ""; }
		else { series = series.trim(); }

		String publisher = input.readLine();
		if (publisher == null) { publisher = ""; }
		else { publisher = publisher.trim(); }

		short year = -1;
		String yearStr = input.readLine();
		if (yearStr == null) { yearStr = ""; }
		else { yearStr = yearStr.trim(); }
		try {
			year = Short.parseShort (yearStr);
		} catch (NumberFormatException ex) {}

		int sales = -1;
		String salesStr = input.readLine();
		if (salesStr == null) { salesStr = ""; }
		else { salesStr = salesStr.trim(); }
		try {
			sales = Integer.parseInt (salesStr);
		} catch (NumberFormatException ex) {}

		input.close();

		return new VideoGameInfo (title, series, publisher, year, sales);
	}

}
