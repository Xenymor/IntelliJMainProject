package OnlineGame;

import StandardClasses.Vector2;

import java.awt.*;

public class Player {
    Vector2 pos;
    int size;
    private Color color;
    int maxHealth;
    int actualLife;

    public Player(Vector2 pos, int size, Color color, int maxHealth) {
        this.pos = pos;
        this.size = size;
        this.color = color;
        this.maxHealth = maxHealth;
        this.actualLife = maxHealth;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int) pos.getX(), (int) pos.getY(), size, size);
    }

    public boolean isHit(Rectangle bullet) {
        Rectangle player = new Rectangle((int) pos.getX(), (int) pos.getY(), size, size);
        return player.intersects(bullet);
    }

    public Rectangle getRect() {
        return new Rectangle(pos.getXAsInt(), pos.getYAsInt(), size, size);
    }

    public void hit(int dmg) {
        actualLife -= dmg;
        if (actualLife <= 0) {
            die();
        }
    }

    private void die() {

    }
}
