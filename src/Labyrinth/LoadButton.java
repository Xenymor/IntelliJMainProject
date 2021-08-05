package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class LoadButton implements Button {
    private final Vector2 pos;
    private final Vector2 size;
    private final int number;
    private Color color;

    public LoadButton(Vector2 pos, Vector2 size, int number) {
        this.pos = pos;
        this.size = size;
        this.number = number;
        color = Main.BOX_COLOR;
    }

    @Override
    public boolean isClicked(Vector2 mousePos) {
        return getRect().contains(mousePos.toPoint());
    }
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect((int)pos.getX(), (int)pos.getY(), (int)size.getX(), (int)size.getY());
        g.setColor(new Color(255, 255, 255));
        g.drawString(Integer.toString(number), (int)(pos.getX()+size.getX()/4*3), (int)(pos.getY()+size.getY()/4));
        g.drawString("Load", (int)(pos.getX()+size.getX()/5), (int)(pos.getY()+size.getY()/4+size.getY()/2.5));
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle((int)pos.getX(), (int)pos.getY(), (int)size.getX(), (int)size.getY());
    }

    @Override
    public void clicked() {
        color = Main.CHOSEN_BOX_COLOR;
        Main.load(number-1, Main.name);
        MyPanel.level = Main.level;
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                color = Main.BOX_COLOR;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
