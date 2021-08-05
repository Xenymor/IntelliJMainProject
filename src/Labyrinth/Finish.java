package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;

public class Finish extends GameObject {
    private static final long serialVersionUID = 1L;

    public Finish(Vector2 pos, int size) {
        super(size, pos);
    }

    @Override
    public void move(GameObject[] objects, Player player) {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Main.FINISH_COLOR);
        g.fillRect((int)pos.getX(), (int)pos.getY(), this.size, this.size);
    }
}
