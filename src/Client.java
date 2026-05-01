import java.io.IOException;
import java.net.Socket;

public class Client {
        public static void main(String[] args) {
                String host = "localhost";
                int port = 12345;

                try {
                        Socket socket = new Socket(host, port);
                        System.out.println("Connectat al servidor.");
                        socket.close();
                } catch (IOException e) {
                        System.out.println("Impossible connectar amb el servidor.");
                }
        }
}