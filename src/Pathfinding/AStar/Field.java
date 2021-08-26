package Pathfinding.AStar;

public class Field {
    int x;
    int y;
    Field parent;

    public Field(int x, int y, Field parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    public void setParent(Field parent) {
        this.parent = parent;
    }

    public Field getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
