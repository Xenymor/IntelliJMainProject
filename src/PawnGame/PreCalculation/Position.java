package PawnGame.PreCalculation;

import PawnGame.PlayerType;

import java.io.Serializable;
import java.util.List;

public class Position implements Serializable {
    private List<Figure> figures;
    private PlayerType playerToMove;

    public Position(List<Figure> figures, PlayerType playerToMove) {
        this.figures = figures;
        this.playerToMove = playerToMove;
    }

    public List<Figure> getFigures() {
        return figures;
    }

    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    public PlayerType getPlayerToMove() {
        return playerToMove;
    }

    @Override
    public String toString() {
        return "Position{" +
                "figures=" + figures +
                ", playerToMove=" + playerToMove +
                '}';
    }

    @Override
    public Position clone() {
        return new Position(Figure.deepClone(figures), playerToMove);
    }

    public void setPlayerToMove(PlayerType playerToMove) {
        this.playerToMove = playerToMove;
    }
}
