package Labyrinth;

import StandardClasses.Collider;
import StandardClasses.PathFinder;
import StandardClasses.Vector2;

import java.awt.*;
import java.util.List;

public class Enemy extends GameObject {
    private static final long serialVersionUID = 1L;
    private List<PathFinder.Position> path;
    private int index;
    private boolean shouldMove = true;
    private final Color color;

    public Enemy(Vector2 pos, int size) {
        super(size, pos);
        this.color = createColor();
    }

    private Color createColor() {
        float red = Main.ENEMY_COLOR.getRed() / 255f * getFactor();
        float green = Main.ENEMY_COLOR.getGreen() / 255f * getFactor();
        float blue = Main.ENEMY_COLOR.getBlue() / 255f * getFactor();
        return new Color(red, green, blue);
    }

    private float getFactor() {
        return (float) ((Math.random() + 1.5) / 2f);
    }

    @Override
    public void move(GameObject[] objects, Player player) {
        if (!Main.isBuildModeOn()) {
            if (index < path.size()) {
                Vector2 nextPos = path.get(index).toVector2();
                nextPos.mult(size);
                for (GameObject object : objects) {
                    if (object.getPos().equals(nextPos)) {
                        newPath();
                    }
                }
                if (shouldMove) {
                    nextPos = path.get(index).toVector2();
                    this.pos = nextPos;
                    this.pos.setX(this.pos.getX() * size);
                    this.pos.setY(this.pos.getY() * size);
                    index++;
                }
            }
        }
    }

    public void newPath() {
        path = Main.getPath(pos, MyPanel.player.getPos(), MyPanel.level);
        shouldMove = path.size() > 1;
        index = 1;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect((int) pos.getX(), (int) pos.getY(), size, size);
    }

    @Override
    public boolean collides(Rectangle other) {
        Rectangle block = new Rectangle((int) pos.getX(), (int) pos.getY(), size, size);
        return Collider.isTouchingRectRect(block, other);
    }

    @Override
    public Vector2 getPos() {
        return new Vector2(pos);
    }
}
