
package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;

public class Player extends GameObject {
    private static final long serialVersionUID = 1L;
    private Vector2 lastPos;

    public Player(Vector2 pos, int size) {
        super(size, pos);
    }

    public void move(GameObject[] objects) {
        Vector2 nextPos = new Vector2(this.pos);
        if (Main.north) {
            nextPos.setY(this.pos.getY() - (double) this.size);
        }

        if (Main.south) {
            nextPos.setY(this.pos.getY() + (double) this.size);
        }

        if (Main.east) {
            nextPos.setX(this.pos.getX() + (double) this.size);
        }

        if (Main.west) {
            nextPos.setX(this.pos.getX() - (double) this.size);
        }

        if (nextPos.getX() + (double) this.size > 800.0D || nextPos.getX() < 0.0D) {
            nextPos.setX(this.pos.getX());
        }

        if (nextPos.getY() + (double) this.size > 600.0D || nextPos.getY() < 0.0D) {
            nextPos.setY(this.pos.getY());
        }

        Rectangle playerRect = new Rectangle((int) nextPos.getX(), (int) nextPos.getY(), this.size, this.size);

        for (GameObject object : objects) {
            if (object.collides(playerRect) && object != this) {
                if (object instanceof Finish) {
                    Main.nextLevel();
                }

                if (object instanceof Enemy) {
                    System.out.println("You lost");
                    System.exit(0);
                }

                return;
            }

            if (object.getPos().equals(this.pos)) {
                if (object instanceof Finish) {
                    Main.nextLevel();
                }

                if (object instanceof Enemy) {
                    System.out.println("You lost");
                    System.exit(0);
                }

                return;
            }
        }

        this.pos = nextPos;
        if (!this.pos.equals(this.lastPos)) {
            MyPanel.newPath(MyPanel.level.getObjects());
            this.lastPos = this.pos;
        }

    }

    public void draw(Graphics g) {
        g.setColor(Main.PLAYER_COLOR);
        g.fillRect((int) this.pos.getX(), (int) this.pos.getY(), this.size, this.size);
    }
}

