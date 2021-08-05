package Labyrinth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World implements Serializable {
    private final List<Level> levels;
    public static final long serialVersionUID = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public World(String name) {
        name = name.replace(".", "");
        name = name.replace("/", "");
        name = name.replace("\\", "");
        this.name = name;
        levels = new ArrayList<>();
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public void addLevels(Level... levels) {
        this.levels.addAll(Arrays.asList(levels));
    }

    public Level getLevel(int index) {
        return levels.get(index).clone();
    }

    public void delete(int index) {
        levels.remove(index);
    }

    public void deleteAll() {
        levels.removeAll(levels);
    }

    public void setLevel(int index, Level level) {
        levels.set(index, level);
    }

    public int size() {
        return levels.size();
    }
}
