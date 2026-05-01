import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
        private static final String DEFAULT_HOST = "localhost";
        private static final int DEFAULT_PORT = 12345;

        public static void main(String[] args) {
                String host = DEFAULT_HOST;
                int port = DEFAULT_PORT;

                if (args.length >= 1) {
                        host = args[0];
                }

                if (args.length >= 2) {
                        try {
                                port = Integer.parseInt(args[1]);
                        } catch (NumberFormatException nfe) {
                                System.err.println("Port no vàlid, s'utilitza " + DEFAULT_PORT);
                                port = DEFAULT_PORT;
                        }
                }

                Client client = new Client();
                client.run(host, port);
        }

        private void run(String host, int port) {
                try (Socket socket = new Socket(host, port);
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                     BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

                        boolean exit = false;

                        while (!exit) {
                                printMenu();
                                int option = readMenuOption(console);

                                switch (option) {
                                        case 1:
                                                System.out.println("Has triat l'opció 1.");
                                                break;
                                        case 2:
                                                System.out.println("Has triat l'opció 2.");
                                                break;
                                        case 3:
                                                System.out.println("Has triat l'opció 3.");
                                                break;
                                        case 4:
                                                System.out.println("Has triat l'opció 4.");
                                                break;
                                        case 5:
                                                exit = true;
                                                break;
                                        default:
                                                System.out.println("Opció no vàlida.");
                                                break;
                                }

                                System.out.println();
                        }

                } catch (IOException e) {
                        System.out.println("Impossible connectar amb el servidor.");
                }
        }

        private void printMenu() {
                System.out.println("Menú d'opcions:");
                System.out.println("1 - Llista els títols dels videojocs.");
                System.out.println("2 - Mostra la informació d'un videojoc.");
                System.out.println("3 - Afegeix un videojoc.");
                System.out.println("4 - Elimina un videojoc.");
                System.out.println("5 - Sortir.");
        }

        private int readMenuOption(BufferedReader console) throws IOException {
                for (;;) {
                        System.out.println("Escull una opció: ");
                        String line = console.readLine();

                        if (line == null) {
                                return 5;
                        }

                        line = line.trim();

                        if (line.isEmpty()) {
                                System.out.println("Opció buida, torna-ho a provar.");
                                continue;
                        }

                        try {
                                int option = Integer.parseInt(line);
                                if (option >= 1 && option <= 5) {
                                        return option;
                                } else {
                                        System.out.println("Opció no vàlida. Introdueix un número del 1 al 5.");
                                }
                        } catch (NumberFormatException e) {
                                System.out.println("Opció no vàlida. Introdueix un número del 1 al 5.");
                        }
                }
        }

        private String readNonEmptyLine(BufferedReader console, String prompt) throws IOException {
                for (;;) {
                        System.out.println(prompt);
                        String line = console.readLine();

                        if (line == null) {
                                return "";
                        }

                        line = line.trim();

                        if (!line.isEmpty()) {
                                return line;
                        }

                        System.out.println("El camp no pot ser buit.");
                }
        }
        private void handleListTitles(DataOutputStream out, DataInputStream in) throws IOException {
                out.writeInt(1);
                out.flush();

                int numVideoGames = in.readInt();

                if (numVideoGames <= 0) {
                        System.out.println();
                        System.out.println("No hi ha videojocs a la base de dades.");
                        return;
                }

                System.out.println();
                System.out.println("Llista de títols:");

                for (int i = 0; i < numVideoGames; i++) {
                        String title = in.readUTF();
                        System.out.println(title);
                }
        }

        private void handleShowGameInfo(BufferedReader console,
                                        DataOutputStream out,
                                        DataInputStream in) throws IOException {
                String query = readNonEmptyLine(console,
                        "Escriu títol o part del títol del videojoc: ");

                out.writeInt(2);
                out.writeUTF(query);
                out.flush();

                boolean found = in.readBoolean();

                if (!found) {
                        System.out.println("Videojoc no trobat.");
                        return;
                }

                byte[] record = new byte[VideoGameInfo.SIZE];
                in.readFully(record);

                VideoGameInfo vgi = VideoGameInfo.fromBytes(record);
                System.out.println(vgi.toString());
        }

        private void handleAddVideoGame(BufferedReader console,
                                        DataOutputStream out,
                                        DataInputStream in) throws IOException {
                System.out.println("Escriu el títol del videojoc a afegir: ");
                String title = console.readLine();

                while (title == null || title.trim().isEmpty()) {
                        System.out.println("El títol del videojoc no pot ser buit.");
                        System.out.println("Escriu el títol del videojoc a afegir: ");
                        title = console.readLine();
                }

                title = title.trim();

                System.out.println("Escriu la sèrie a la que pertany: ");
                String series = console.readLine();
                if (series == null) {
                        series = "";
                } else {
                        series = series.trim();
                }

                System.out.println("Escriu l'editor del videojoc: ");
                String publisher = console.readLine();
                if (publisher == null) {
                        publisher = "";
                } else {
                        publisher = publisher.trim();
                }

                System.out.println("Introdueix l'any de publicació: ");
                short year = -1;
                String yearStr = console.readLine();
                if (yearStr == null) {
                        yearStr = "";
                } else {
                        yearStr = yearStr.trim();
                }
                try {
                        year = Short.parseShort(yearStr);
                } catch (NumberFormatException nfe) {
                }

                System.out.println("Introdueix la quantitat de vendes: ");
                int sales = -1;
                String salesStr = console.readLine();
                if (salesStr == null) {
                        salesStr = "";
                } else {
                        salesStr = salesStr.trim();
                }
                try {
                        sales = Integer.parseInt(salesStr);
                } catch (NumberFormatException nfe) {
                }

                VideoGameInfo vgi = new VideoGameInfo(title, series, publisher, year, sales);
                byte[] record = vgi.toBytes();

                out.writeInt(3);
                out.write(record);
                out.flush();

                boolean success = in.readBoolean();

                if (!success) {
                        System.out.println("Aquest videojoc ja estava a la base de dades.");
                } else {
                        System.out.println("Videojoc afegit correctament.");
                }
        }

        private void handleDeleteVideoGame(BufferedReader console,
                                           DataOutputStream out,
                                           DataInputStream in) throws IOException {
                String query = readNonEmptyLine(console,
                        "Escriu títol o part del títol del videojoc: ");

                out.writeInt(4);
                out.writeUTF(query);
                out.flush();

                boolean success = in.readBoolean();

                if (!success) {
                        System.out.println("Videojoc no trobat.");
                } else {
                        System.out.println("Videojoc eliminat correctament.");
                }
        }
}