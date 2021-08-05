package Labyrinth;

import StandardClasses.Vector2;

import java.awt.*;

public interface Button {
    void paint(Graphics g);
    Rectangle getRect();
    boolean isClicked(Vector2 mousePos);
    void clicked();
}
