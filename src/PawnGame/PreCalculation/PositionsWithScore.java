package PawnGame.PreCalculation;

import java.util.List;

public class PositionsWithScore {
    Integer score;
    List<Integer> previousPositions;

    public PositionsWithScore(Integer score, List<Integer> previousPositions) {
        this.score = score;
        this.previousPositions = previousPositions;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void add(int toAdd) {
        previousPositions.add(toAdd);
    }
}
