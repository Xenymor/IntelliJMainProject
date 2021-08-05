package Labyrinth.Networking;

import Labyrinth.Enemy;
import Labyrinth.Level;
import Labyrinth.Player;
import Labyrinth.World;
import StandardClasses.Vector2;

import java.io.IOException;

public class ClientTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Level level = new Level(new Player(new Vector2(0, 0), 20), new Enemy(new Vector2(60, 60), 20));
        World test = new World("Test");
        test.addLevel(level);
        System.out.println("Saving");
        Client.save(test);
        System.out.println("Saved");
        System.out.println("Loading");
        System.out.println(Client.load("Test"));
    }
}
