package OnlineGame;

import StandardClasses.Vector2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<Socket> sockets;
    private static List<Vector2> positions;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        sockets = new ArrayList<>();
        new Thread(() -> {
            while (true) {
                try {
                    gameLoop();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        while (true) {
            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            introduceSocket(socket, positions);
        }
    }

    static void introduceSocket(Socket socket, List<Vector2> positions) throws IOException, ClassNotFoundException {
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        outputStream.write(positions.size());
        objectOutputStream.writeObject(positions);
        positions.add((Vector2) new ObjectInputStream(socket.getInputStream()).readObject());
    }

    static void gameLoop() throws IOException, ClassNotFoundException {
        for (int i = 0; i < sockets.size(); i++) {
            Socket socket = sockets.get(i);
            InputStream inputStream = socket.getInputStream();
            positions.set(i, (Vector2) new ObjectInputStream(inputStream).readObject());
        }
        for (Socket socket : sockets) {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(positions);
        }
    }
}
