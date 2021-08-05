package Labyrinth.Networking;

import Labyrinth.World;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {
    public static World load(String name) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 2007);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(0);
        new ObjectOutputStream(outputStream).writeObject(name);
        InputStream inputStream = socket.getInputStream();
        return (World)new ObjectInputStream(inputStream).readObject();
    }

    public static boolean save(World world) {
        try {
            Socket socket = new Socket("localhost", 2007);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(1);
            new ObjectOutputStream(outputStream).writeObject(world);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static List<File> getEveryFileInDirectory() {
        try {
            Socket socket = new Socket("localhost", 2007);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(2);
            return (List<File>) new ObjectInputStream(socket.getInputStream()).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
