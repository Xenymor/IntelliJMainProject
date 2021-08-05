package Snake;

import StandardClasses.Vector2;

import java.awt.*;

public class Apple {
    Vector2 pos;
    int blockSize;

    public Apple(int blockSize) {
        this.blockSize = blockSize;
        wasEaten();
    }

    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.fillOval((int) pos.getX(), (int) pos.getY(), blockSize, blockSize);
    }

    public void wasEaten() {
        this.pos = Vector2.randomVector2InRange(new Vector2(0, 0), new Vector2((Main.windowSize.getX() / blockSize) - 1, (Main.windowSize.getY() / blockSize) - 1));
        this.pos.setX((long) Math.floor(this.pos.getX()));
        this.pos.setY((long) Math.floor(this.pos.getY()));
        this.pos.mult(blockSize);
        this.pos.setX(this.pos.getX());
        this.pos.setY(this.pos.getY());
    }

    public boolean isTouching(Vector2 pos) {
        Rectangle rect1 = new Rectangle((int) this.pos.getX(), (int) this.pos.getY(), blockSize, blockSize);
        Rectangle rect2 = new Rectangle((int) pos.getX(), (int) pos.getY(), blockSize, blockSize);
        Rectangle intersection = rect1.intersection(rect2);
        return intersection.height > 0 && intersection.width > 0;
    }
}
