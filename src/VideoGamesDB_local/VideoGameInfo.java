public class VideoGameInfo {

	private String title;
	private String series;
	private String publisher;
	private  short year;
	private    int sales;

	private static final int     TITLE_LIMIT = 24;
	private static final int    SERIES_LIMIT = 19;
	private static final int PUBLISHER_LIMIT = 18;

	public  static final int SIZE =
		2 * (TITLE_LIMIT + SERIES_LIMIT + PUBLISHER_LIMIT) + 2 + 4;

	public VideoGameInfo (String title, String series, String publisher,
	                      short year, int sales) {
		this.title     = title;
		this.series    = series;
		this.publisher = publisher;
		this.year      = year;
		this.sales     = sales;
	}

	// Getters
	public String getTitle     () { return title;     }
	public String getSeries    () { return series;    }
	public String getPublisher () { return publisher; }
	public  short getYear      () { return year;      }
	public    int getSales     () { return sales;     }

	public byte[] toBytes() {
		byte[] record = new byte[SIZE];
		int offset = 0;
		// Title
		PackUtils.packString (title, TITLE_LIMIT, record, offset);
		offset += 2 * TITLE_LIMIT;
		// Series
		PackUtils.packString (series, SERIES_LIMIT, record, offset);
		offset += 2 * SERIES_LIMIT;
		// Publisher
		PackUtils.packString (publisher, PUBLISHER_LIMIT, record, offset);
		offset += 2 * PUBLISHER_LIMIT;
		// Year
		PackUtils.packShort (year, record, offset);
		offset += 2;
		// Sales
		PackUtils.packInt (sales, record, offset);
		// offset += 4;
		return record;
	}

	public static VideoGameInfo fromBytes (byte[] record) {
		int offset = 0;
		// Title
		String title = PackUtils.unpackString (TITLE_LIMIT, record, offset);
		offset += 2 * TITLE_LIMIT;
		// Series
		String series = PackUtils.unpackString (SERIES_LIMIT, record, offset);
		offset += 2 * SERIES_LIMIT;
		// Publisher
		String publisher = PackUtils.unpackString (PUBLISHER_LIMIT, record, offset);
		offset += 2 * PUBLISHER_LIMIT;
		// Year
		short year = PackUtils.unpackShort (record, offset);
		offset += 2;
		// Sales
		int sales = PackUtils.unpackInt (record, offset);
		// offset += 4;
		return new VideoGameInfo (title, series, publisher, year, sales);
	}

	public String toString() {
		String ls = System.lineSeparator();
		String result = title;
		if (!series.isEmpty()) {
			result += " (sèrie " + series + ")";
		}
		if (!publisher.isEmpty() || year >= 0) {
			result += ls + "Publicat";
			if (!publisher.isEmpty()) {
				result += " per " + publisher;
			}
			if (year >= 0) {
				result += " en l'any " + year;
			}
			result += ".";
		}
		if (sales >= 0) {
			result += ls + "Ha venut aproximadament " + sales + " còpies.";
		}
		return result;
	}

}
