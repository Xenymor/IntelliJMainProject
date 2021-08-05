package PawnGame;

import StandardClasses.MyArrays;

import java.util.Arrays;

public class Position {
    PlayerType[][] field;
    int score;

    public Position(PlayerType[][] field, int score) {
        this.field = MyArrays.deepCloneEnum(field);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PlayerType[][] getField() {
        return field;
    }

    public void setField(PlayerType[][] field) {
        this.field = field;
    }
}
