package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;

public class Block extends GameObject {
    private static final long serialVersionUID = 1L;

    public Block(Vector2 pos, int size) {
        super(size, pos);
    }

    @Override
    public void move(GameObject[] objects, Player player) {

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Main.BLOCK_COLOR);
        g.fillRect((int)pos.getX(), (int)pos.getY(), size, size);
    }
}
