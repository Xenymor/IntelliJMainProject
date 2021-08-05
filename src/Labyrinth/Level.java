package Labyrinth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Level implements Serializable {
    public static final long serialVersionUID = 1;
    private Player player;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    public Level(Player player, GameObject... objects) {
        this.gameObjects.addAll(Arrays.asList(objects));
        this.player = player;
    }

    public GameObject[] getObjects() {
        GameObject[] a = new GameObject[gameObjects.size()];
        return gameObjects.toArray(a);
    }

    public Level clone() {
        return new Level(player, getObjects());
    }

    public void add(GameObject toAdd) {
        gameObjects.add(toAdd);
    }

    public void add(GameObject... toAdd) {
        gameObjects.addAll(Arrays.asList(toAdd));
    }

    public void delete(int index) {
        gameObjects.remove(index);
    }

    public void deleteAll() {
        gameObjects.removeAll(gameObjects);
    }

    public void delete(GameObject toDelete) {
        gameObjects.remove(toDelete);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
