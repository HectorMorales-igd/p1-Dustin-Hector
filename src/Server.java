import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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

                Thread t = new Thread(new ClientHandler(clientSocket, videoGamesDB));
                t.start();
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

    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final VideoGamesDB videoGamesDB;

        ClientHandler(Socket socket, VideoGamesDB videoGamesDB) {
            this.socket = socket;
            this.videoGamesDB = videoGamesDB;
        }

        @Override
        public void run() {
            DataInputStream in = null;
            DataOutputStream out = null;

            try {
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                boolean fi = false;

                while (!fi) {
                    int option;
                    try {
                        option = in.readInt();
                    } catch (EOFException e) {
                        break;
                    }

                    System.out.println("Client " + socket.getRemoteSocketAddress() + " opció " + option);

                    switch (option) {
                        case 1:
                            handleListTitles(out);
                            break;
                        case 2:
                            handleInfoFromOneVideoGame(in, out);
                            break;
                        case 3:
                            handleAddVideoGame(in, out);
                            break;
                        case 4:
                            handleDeleteVideoGame(in, out);
                            break;
                        case 5:
                            fi = true;
                            break;
                        default:
                            fi = true;
                            break;
                    }
                }

            } catch (SocketException | EOFException e) {
                // client desconnectat o connexió tallada
            } catch (IOException e) {
                System.err.println("Error de comunicació amb el client " + socket.getRemoteSocketAddress());
            } finally {
                closeQuietly(in);
                closeQuietly(out);
                closeQuietly(socket);
                System.out.println("Client desconnectat: " + socket.getRemoteSocketAddress());
            }
        }

        private void handleListTitles(DataOutputStream out) throws IOException {
            int num;
            synchronized (videoGamesDB) {
                num = videoGamesDB.getNumVideoGames();
            }

            out.writeInt(num);

            for (int i = 0; i < num; i++) {
                VideoGameInfo vgi;
                synchronized (videoGamesDB) {
                    vgi = videoGamesDB.readVideoGameInfo(i);
                }
                out.writeUTF(vgi.getTitle());
            }

            out.flush();
        }

        private void handleInfoFromOneVideoGame(DataInputStream in, DataOutputStream out) throws IOException {
            String pattern = in.readUTF();

            int index;
            synchronized (videoGamesDB) {
                index = videoGamesDB.searchVideoGame(pattern);
            }

            if (index < 0) {
                out.writeBoolean(false);
            } else {
                VideoGameInfo vgi;
                synchronized (videoGamesDB) {
                    vgi = videoGamesDB.readVideoGameInfo(index);
                }
                byte[] record = vgi.toBytes();
                out.writeBoolean(true);
                out.write(record);
            }

            out.flush();
        }

        private void handleAddVideoGame(DataInputStream in, DataOutputStream out) throws IOException {
            byte[] record = new byte[VideoGameInfo.SIZE];
            in.readFully(record);

            VideoGameInfo vgi = VideoGameInfo.fromBytes(record);

            boolean success;
            synchronized (videoGamesDB) {
                success = videoGamesDB.insertVideoGame(vgi);
            }

            out.writeBoolean(success);
            out.flush();
        }

        private void handleDeleteVideoGame(DataInputStream in, DataOutputStream out) throws IOException {
            String pattern = in.readUTF();

            boolean success;
            synchronized (videoGamesDB) {
                success = videoGamesDB.deleteVideoGame(pattern);
            }

            out.writeBoolean(success);
            out.flush();
        }

        private void closeQuietly(AutoCloseable c) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
