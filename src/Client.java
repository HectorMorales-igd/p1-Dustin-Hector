import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
        public static void main(String[] args) {
                String host = "localhost";
                int port = 12345;

                Client client = new Client();
                client.run(host, port);
        }

        private void run(String host, int port) {
                try (Socket socket = new Socket(host, port);
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                     BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

                        printMenu();

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
}