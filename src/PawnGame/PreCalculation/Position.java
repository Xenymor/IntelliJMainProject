package PawnGame.PreCalculation;

import PawnGame.PlayerType;

import java.util.List;

public class Position {
    private List<Figure> figures;
    private PlayerType playerToMove;

    public Position(List<Figure> figures, PlayerType playerToMove) {
        this.figures = figures;
        this.playerToMove = playerToMove;
    }

    public List<Figure> getFigures() {
        return figures;
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

    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    public void setPlayerToMove(PlayerType playerToMove) {
        this.playerToMove = playerToMove;
    }
}
