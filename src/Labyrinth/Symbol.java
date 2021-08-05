package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;

public class Symbol implements Button {
    final Symbols symbol;
    final int X_SIZE;
    final int Y_SIZE;
    int X_POS;
    int Y_POS;
    private Color color;

    public Symbol(Symbols symbol, int X_POS, int Y_POS, int X_SIZE, int Y_SIZE) {
        this.X_SIZE = X_SIZE;
        this.Y_SIZE = Y_SIZE;
        this.X_POS = X_POS;
        this.Y_POS = Y_POS;
        this.symbol = symbol;
        color = Main.BOX_COLOR;
    }

    public void paint(Graphics g) {
        if (Main.isBuildModeOn()) {
            g.setColor(color);
            g.fillRect(X_POS, Y_POS, X_SIZE, Y_SIZE);
            if (symbol == Symbols.BLOCK) {
                g.setColor(Color.black);
                g.drawString("Block", X_POS+X_SIZE/6, Y_POS+10);
                g.setColor(Main.BLOCK_COLOR);
                g.fillRect(X_POS+X_SIZE/4, Y_POS+Y_SIZE/4, X_SIZE/2, Y_SIZE/2);
            } else if (symbol == Symbols.ENEMY) {
                g.setColor(Color.black);
                g.drawString("Enemy", X_POS+X_SIZE/10, Y_POS+10);
                g.setColor(Main.ENEMY_COLOR);
                g.fillRect(X_POS+X_SIZE/4, Y_POS+Y_SIZE/4, X_SIZE/2, Y_SIZE/2);
            } else if (symbol == Symbols.FINISH) {
                g.setColor(Color.black);
                g.drawString("Finish", X_POS+X_SIZE/6, Y_POS+10);
                g.setColor(Main.FINISH_COLOR);
                g.fillRect(X_POS+X_SIZE/4, Y_POS+Y_SIZE/4, X_SIZE/2, Y_SIZE/2);
            }
        }
    }

    public Rectangle getRect() {
        return new Rectangle(X_POS, Y_POS, X_SIZE, Y_SIZE);
    }

    @Override
    public boolean isClicked(Vector2 mousePos) {
        return false;
    }

    @Override
    public void clicked() {
        color = Main.CHOSEN_BOX_COLOR;
        Main.chosen = symbol;
    }

    public void unChosen() {
        color = Main.BOX_COLOR;
    }
}
