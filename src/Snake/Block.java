package Snake;

import StandardClasses.Vector2;

import java.awt.*;

public class Block {
    private final Vector2 pos;
    private final float size;

    public Block(Vector2 pos, float size) {
        this.pos = pos;
        this.size = size;
    }

    public void paint(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect((int) pos.getX(), (int) pos.getY(), (int) size, (int) size);
    }

    public Vector2 getPos() {
        return pos;
    }
}
