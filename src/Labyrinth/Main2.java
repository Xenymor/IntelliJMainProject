package Labyrinth;

import java.io.*;

public class Main2 {
    public static void main(String[] args) throws IOException {
        World world = new World("Default");
        for (int i = 0; i <= 9; i++) {
            try {
                FileInputStream reader = new FileInputStream("out\\production\\Timon\\Labyrinth\\SaveFile" + i + ".txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(reader);
                GameObject[] gameObjects = (GameObject[]) objectInputStream.readObject();
                Player player = (Player) objectInputStream.readObject();
                if (player == null) {
                    System.out.println("Player == null");
                }
                Level level = new Level(player, gameObjects);
                world.addLevel(level);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Couldn't load level " + i);
                System.exit(-1111);
                e.printStackTrace();
            }
        }
        FileOutputStream writer = new FileOutputStream("out\\production\\Timon\\Labyrinth\\World_" + world.getName() + ".txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(writer);
        objectOutputStream.writeObject(world);
    }
}
