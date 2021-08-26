package Pathfinding.AStar;

import java.util.ArrayList;
import java.util.List;

public class AStar {
    List<Field> open = new ArrayList<>();
    List<Field> closed = new ArrayList<>();

    public static void main(String[] args) {
        new AStar().run(10, 10);
    }

    public void run(int startX, int startY) {
        open.add(new Field(startX, startY, null));

        while (true) {
            Field current = getLowestGCost();

        }
    }
}
