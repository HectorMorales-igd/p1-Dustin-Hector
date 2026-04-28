import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

	private static final String VIDEOGAMES_DB_NAME = "videogamesDB.dat";
	private static VideoGamesDB videoGamesDB;

	public static void main (String[] args) {
		try {
			videoGamesDB = new VideoGamesDB (VIDEOGAMES_DB_NAME);
		} catch (IOException ioe) {
			System.err.println ("Error obrint la base de dades!");
			System.exit (-1);
		}
		for (;;) {
			printMenu();
			int option = getOption();
			switch (option) {
				case 1:
					listTitles();
					break;
				case 2:
					infoFromOneVideoGame();
					break;
				case 3:
					addVideoGame();
					break;
				case 4:
					deleteVideoGame();
					break;
				case 5:
					quit();
					break;
			}
			System.out.println();
		}
	}

	private static void printMenu() {
		System.out.println ("Menú d'opcions:");
		System.out.println ("1 - Llista els títols dels videojocs.");
		System.out.println ("2 - Mostra la informació d'un videojoc.");
		System.out.println ("3 - Afegeix un videojoc.");
		System.out.println ("4 - Elimina un videojoc.");
		System.out.println ("5 - Sortir.");
	}

	private static int getOption() {
		for (;;) {
			try {
				BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
				System.out.println ("Escull una opció: ");
				String optionStr = in.readLine();
				int option = Integer.parseInt (optionStr);
				if (0 < option && option <= 5) {
					return option;
				}
			} catch (Exception e) {
				System.err.println ("Error llegint opció!");
			}
		}
	}

	private static void listTitles() {
		int numVideoGames = videoGamesDB.getNumVideoGames();
		System.out.println();
		try {
			for (int i = 0; i < numVideoGames; i++) {
				VideoGameInfo vgi = videoGamesDB.readVideoGameInfo (i);
				System.out.println (vgi.getTitle());
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void infoFromOneVideoGame() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Escriu títol o part del títol del videojoc: ");
		String str;
		try {
			str = in.readLine();
		} catch (IOException ioe) {
			System.err.println ("Error llegint el títol!");
			return;
		}
		try {
			int n = videoGamesDB.searchVideoGame (str);
			if (n >= 0) {
				VideoGameInfo vgi = videoGamesDB.readVideoGameInfo (n);
				System.out.println (vgi);
			} else {
				System.out.println ("Videojoc no trobat.");
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void addVideoGame() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		VideoGameInfo vgi;
		try {
			System.out.println ("Escriu el títol del videojoc a afegir: ");
			String title = in.readLine();
			while (title == null || title.trim().isEmpty()) {
				System.out.println ("El títol del videojoc no pot ser buit.");
				System.out.println ("Escriu el títol del videojoc a afegir: ");
				title = in.readLine();
			}
			title = title.trim();

			System.out.println ("Escriu la sèrie a la que pertany: ");
			String series = in.readLine();
			if (series == null) { series = ""; }
			else { series = series.trim(); }

			System.out.println ("Escriu l'editor del videojoc: ");
			String publisher = in.readLine();
			if (publisher == null) { publisher = ""; }
			else { publisher = publisher.trim(); }

			System.out.println ("Introdueix l'any de publicació: ");
			short year = -1;
			String yearStr = in.readLine();
			if (yearStr == null) { yearStr = ""; }
			else { yearStr = yearStr.trim(); }
			try {
				year = Short.parseShort (yearStr);
			} catch (NumberFormatException nfe) { /* Ignored */ }

			System.out.println ("Introdueix la quantitat de vendes: ");
			int sales = -1;
			String salesStr = in.readLine();
			if (salesStr == null) { salesStr = ""; }
			else { salesStr = salesStr.trim(); }
			try {
				sales = Integer.parseInt (salesStr);
			} catch (NumberFormatException nfe) { /* Ignored */ }

			vgi = new VideoGameInfo (title, series, publisher, year, sales);
		} catch (IOException ioe) {
			System.err.println ("Error llegint la informació del videojoc!");
			return;
		}
		try {
			boolean success = videoGamesDB.insertVideoGame (vgi);
			if (!success) {
				System.out.println ("Aquest videojoc ja estava a la base de dades.");
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void deleteVideoGame() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Escriu títol o part del títol del videojoc: ");
		String str;
		try {
			str = in.readLine();
		} catch (IOException ioe) {
			System.err.println ("Error llegint el títol!");
			return;
		}
		try {
			boolean success = videoGamesDB.deleteVideoGame (str);
			if (!success) {
				System.out.println ("Videojoc no trobat.");
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void quit() {
		try {
			videoGamesDB.close();
			System.exit (0);
		} catch (IOException ioe) {
			System.err.println ("Error tancant base de dades!");
			System.exit (-1);
		}
	}

}
