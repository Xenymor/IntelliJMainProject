package PawnGame.PreCalculation;

import PawnGame.Move;
import PawnGame.PlayerType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static PawnGame.PlayerType.*;

public class Main {
    private final int fieldSize = 4;
    HashMap<Integer, List<Integer>> knownPositions = new HashMap<>();
    String pathname = "positions1.txt";

    public static void main(String[] args) throws IllegalMoveException, InterruptedException, IOException, ClassNotFoundException, WTFIsWrongWithYouException {
        new Main().test();
    }

    private void test() throws IOException, ClassNotFoundException {
        List<Figure> figures = new ArrayList<>();
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                PlayerType type = NEUTRAL;
                if (x == 0) {
                    type = PLAYER1;
                } else if (y == 0) {
                    type = PLAYER2;
                }
                if (type != NEUTRAL && !(x == 0 && y == 0)) {
                    figures.add(new Figure(x, y, type));
                }
            }
        }
        int field = posToInt(figures, PLAYER1);
        System.out.println(field);
    }

    private void run() throws IllegalMoveException, WTFIsWrongWithYouException {
        List<Figure> figures = new ArrayList<>();
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                PlayerType type = NEUTRAL;
                if (x == 0) {
                    type = PLAYER1;
                } else if (y == 0) {
                    type = PLAYER2;
                }
                if (type != NEUTRAL && !(x == 0 && y == 0)) {
                    figures.add(new Figure(x, y, type));
                }
            }
        }
        int field = posToInt(figures, PLAYER1);
        knownPositions.put(field, new ArrayList<>());
        addAllMoves(field);
        HashMap<Integer, Float> scores = new HashMap<>();
        for (int position = 0; position < Integer.MAX_VALUE; position++) {
            if (knownPositions.containsKey(position)) {
                PlayerType wonPlayerType = whoHasWon(intToPosition(position));
                if (wonPlayerType != NEUTRAL) {
                    if (wonPlayerType == PLAYER1) {

                    } else if (wonPlayerType == PLAYER2) {

                    } else {
                        throw new WTFIsWrongWithYouException();
                    }
                }
            }
        }
    }


    private void addAllMoves(int field) throws IllegalMoveException {
        Position pos = intToPosition(field);
        PlayerType playerToMove = pos.getPlayerToMove();
        PlayerType wonPlayer = whoHasWon(pos);
        if (wonPlayer != NEUTRAL) {
            return;
        }
        List<Move> allMoves = getAllAllowedMoves(pos.getFigures(), playerToMove);
        for (Move allMove : allMoves) {
            Position potentialPos = doMove(allMove, pos);
            potentialPos.setPlayerToMove(getEnemy(potentialPos.getPlayerToMove()));
            int potentialPosInt = posToInt(potentialPos.getFigures(), potentialPos.getPlayerToMove());
            if (!addField(potentialPosInt, field)) {
                continue;
            }
            addAllMoves(potentialPosInt);
        }
    }

    private PlayerType whoHasWon(Position pos) {
        if (getAllAllowedMoves(pos.getFigures(), pos.getPlayerToMove()).size() == 0) {
            return pos.getPlayerToMove();
        }
        return NEUTRAL;
    }

    private PlayerType getEnemy(PlayerType playerToMove) {
        return playerToMove == PLAYER1 ? PLAYER2 : playerToMove == PLAYER2 ? PLAYER1 : NEUTRAL;
    }

    private Position doMove(Move move, Position pos) throws IllegalMoveException {
        pos = pos.clone();
        List<Figure> figures = pos.getFigures();
        boolean worked = false;
        for (int i = 0; i < figures.size(); i++) {
            Figure figure = figures.get(i);
            if (figure.getX() == move.getxFrom() && figure.getY() == move.getyFrom()) {
                if (figure.getPlayerType() == pos.getPlayerToMove()) {
                    figure.setX(move.getxTo());
                    figure.setY(move.getyTo());
                    worked = true;
                    break;
                }
            }
        }
        if (!worked) {
            throw new IllegalMoveException();
        }
        return pos;
    }

    private boolean addField(int field, int previous) {
        boolean wasAdded = false;
        if (!knownPositions.containsKey(field)) {
            knownPositions.put(field, new ArrayList<>());
            wasAdded = true;
        }
        knownPositions.get(field).add(previous);
        return wasAdded;
    }

    private List<Move> getAllAllowedMoves(List<Figure> figures, PlayerType playerToMove) {
        final List<Move> allowedMoves = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            Figure figure = figures.get(i);
            if (figure.getPlayerType() == playerToMove) {
                allowedMoves.addAll(getAllAllowedMoves(figure, figures, playerToMove));
            }
        }
        return allowedMoves;
    }

    private List<Move> getAllAllowedMoves(Figure figure, List<Figure> figures, PlayerType playerToMove) {
        final List<Move> allowedMoves = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int figureX = figure.getX();
                int figureY = figure.getY();
                Move move = new Move(figureX, figureY, figureX + x, figureY + y);
                if (isAllowed(move, figures, playerToMove, figure)) {
                    allowedMoves.add(move);
                }
            }
        }
        return allowedMoves;
    }

    private boolean isAllowed(Move move, List<Figure> figures, PlayerType playerToMove, Figure figureToMove) {
        if (figureToMove.getPlayerType() != playerToMove) {
            return false;
        }
        if (Math.sqrt((move.getxFrom() - move.getxTo()) * (move.getxFrom() - move.getxTo()) + (move.getyFrom() - move.getyTo()) * (move.getyFrom() - move.getyTo())) != 1) {
            return false;
        }
        for (Figure figure : figures) {
            if (figure.getX() == move.getxTo() && figure.getY() == move.getyTo()) {
                return false;
            }
        }
        if (move.getxTo() > fieldSize + 1 || move.getyTo() > fieldSize + 1) {
            return false;
        }
        if (move.getxTo() < 0 || move.getyTo() < 0) {
            return false;
        }
        return true;
    }

    private int posToInt(List<Figure> figures, PlayerType playerToMove) {
        int result = 0;
        result |= playerToMove == PLAYER1 ? 0 : playerToMove == PLAYER2 ? 1 : -1;
        for (Figure figure : figures) {
            PlayerType type = figure.getPlayerType();
            if (type == PLAYER1) {
                result |= 0 << (figure.getX() * 2 + figure.getY() * 8) + 1;
                result |= 1 << (figure.getX() * 2 + figure.getY() * 8) + 2;
            } else if (type == PLAYER2) {
                result |= 1 << (figure.getX() * 2 + figure.getY() * 8) + 1;
                result |= 0 << (figure.getX() * 2 + figure.getY() * 8) + 2;
            }
        }
        return result;
    }

    private Position intToPosition(int number) {
        List<Figure> figures = new ArrayList<>();
        int i = (number) & 1;
        PlayerType playerToMove = i == 0 ? PLAYER1 : PLAYER2;
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                int firstBit = (number >> (x * 2 + 8 * y) + 1) & 1;
                int secondBit = (number >> (x * 2 + 8 * y) + 2) & 1;
                if (firstBit == 0 && secondBit == 1) {
                    figures.add(new Figure(x, y, PLAYER1));
                } else if (firstBit == 1 && secondBit == 0) {
                    figures.add(new Figure(x, y, PLAYER2));
                }
            }
        }
        return new Position(figures, playerToMove);
    }

    private static class IllegalMoveException extends Throwable {
    }

    private static class WTFIsWrongWithYouException extends Throwable {
    }
}
