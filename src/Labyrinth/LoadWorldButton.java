package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class LoadWorldButton implements Button {
    private final Vector2 pos;
    private final Vector2 size;
    private Color color;

    public LoadWorldButton(Vector2 pos, Vector2 size) {
        this.pos = pos;
        this.size = size;
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
        g.drawString("Load", (int)(pos.getX()+size.getX()/5), (int)(pos.getY()+size.getY()/3));
        g.drawString("World", (int)(pos.getX()+size.getX()/5), (int)(pos.getY()+size.getY()/3*2));
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle((int)pos.getX(), (int)pos.getY(), (int)size.getX(), (int)size.getY());
    }

    @Override
    public void clicked() {
        color = Main.CHOSEN_BOX_COLOR;
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                color = Main.BOX_COLOR;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Main.chooseNameOfWorld();
        Main.nextLevel();
    }
}
