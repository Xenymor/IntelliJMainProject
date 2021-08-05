package Labyrinth;

import StandardClasses.Collider;
import StandardClasses.Vector2;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable {
    protected Vector2 pos;
    protected int size;



    protected GameObject(int size, Vector2 pos) {
        this.size = size;
        this.pos = pos;
    }

    void move(GameObject[] objects, Player player) {

    }

    void draw(Graphics g) {

    }

    boolean collides(Rectangle other) {
        Rectangle block = new Rectangle((int) pos.getX(), (int) pos.getY(), size, size);
        return Collider.isTouchingRectRect(block, other);
    }

    protected Vector2 getPos() {
        return new Vector2(this.pos);
    }
}
