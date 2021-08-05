package PawnGame;

public class Move {
    private final int xFrom;
    private final int yFrom;
    private final int xTo;
    private final int yTo;

    public Move(int xFrom, int yFrom, int xTo, int yTo) {
        this.xFrom = xFrom;
        this.yFrom = yFrom;
        this.xTo = xTo;
        this.yTo = yTo;
    }

    public int getxFrom() {
        return xFrom;
    }

    public int getyFrom() {
        return yFrom;
    }

    public int getxTo() {
        return xTo;
    }

    public int getyTo() {
        return yTo;
    }
}
