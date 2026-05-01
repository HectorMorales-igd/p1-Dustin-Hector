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
}