package PawnGame.PreCalculation;

import PawnGame.PlayerType;

import java.util.ArrayList;
import java.util.List;

public class Figure {
    private int x;
    private int y;
    private PlayerType playerType;

    public Figure(int x, int y, PlayerType playerType) {
        this.x = x;
        this.y = y;
        this.playerType = playerType;
    }

    public static List<Figure> deepClone(List<Figure> figures) {
        List<Figure> newFigures = new ArrayList<>();
        for (Figure figure : figures) {
            newFigures.add(figure.clone());
        }
        return newFigures;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    @Override
    public String toString() {
        return "Figure{" +
                "x=" + x +
                ", y=" + y +
                ", playerType=" + playerType +
                '}';
    }

    @Override
    public Figure clone() {
        return new Figure(x, y, playerType);
    }
}
