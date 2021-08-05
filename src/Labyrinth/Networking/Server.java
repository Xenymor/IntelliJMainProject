package Labyrinth.Networking;

import Labyrinth.World;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2007);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleSocket(socket)).start();
        }
    }

    private static void handleSocket(Socket socket) {
        InputStream inputStream;
        OutputStream outputStream;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int input = inputStream.read();
            if (input == 0) {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                String name = (String) objectInputStream.readObject();
                File file = new File("Worlds/" + name + ".txt");
                if (file.exists()) {
                    objectOutputStream.writeObject(new ObjectInputStream(new FileInputStream(file)).readObject());
                    System.out.println("Sent world " + name);
                } else {
                    throw new FileNotFoundException();
                }
            } else if (input == 1) {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                World world = (World) objectInputStream.readObject();
                new ObjectOutputStream(new FileOutputStream("Worlds/" + world.getName() + ".txt")).writeObject(world);
                System.out.println("Saved world " + world.getName());
            } else if (input == 2) {
                File folder = new File("Worlds");
                File[] listOfFiles = folder.listFiles();
                ArrayList<File> objects = new ArrayList<>();
                Collections.addAll(objects, listOfFiles);
                new ObjectOutputStream(outputStream).writeObject(objects);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {

            }
        }
    }
}
