import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final String VIDEOGAMES_DB_NAME = "videogamesDB.dat";
    private static final int PORT = 12345;

    private final VideoGamesDB videoGamesDB;

    public Server(String dbFileName) throws IOException {
        this.videoGamesDB = new VideoGamesDB(dbFileName);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor escoltant al port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket.getRemoteSocketAddress());

                clientSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(VIDEOGAMES_DB_NAME);
            server.start();
        } catch (IOException ioe) {
            System.err.println("Error iniciant servidor / obrint base de dades!");
        }
    }
}
