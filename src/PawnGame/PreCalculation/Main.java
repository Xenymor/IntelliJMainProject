package PawnGame.PreCalculation;

import PawnGame.Move;
import PawnGame.PlayerType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static PawnGame.PlayerType.*;

public class Main {
    private final int fieldSize = 4;
    HashMap<Integer, List<Integer>> knownPositions = new HashMap<>();
    private HashMap<Integer, Integer> scores;

    public static void main(String[] args) throws IllegalMoveException, InterruptedException, IOException, ClassNotFoundException, WhyAreYouEditingMyCodeException {
        new Main().run();
    }

    private void printStartingScore() throws IOException, ClassNotFoundException, WhyAreYouEditingMyCodeException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("scores.txt"));
        HashMap<Integer, Integer> scores = (HashMap<Integer, Integer>) objectInputStream.readObject();
        int startingPositionAsInt = getStartingPositionAsInt();
        System.out.println(startingPositionAsInt + " = " + scores.get(startingPositionAsInt));
    }

    private int getStartingPositionAsInt() throws WhyAreYouEditingMyCodeException {
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
        return posToInt(figures, PLAYER1);
    }

    private void run() throws IllegalMoveException, WhyAreYouEditingMyCodeException, IOException {
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
        scores = new HashMap<>();
        Integer[] keys = knownPositions.keySet().toArray(new Integer[1]);
        for (Integer aInteger : keys) {
            PlayerType wonPlayerType = whoHasWon(intToPos(aInteger, fieldSize));
            if (wonPlayerType != NEUTRAL) {
                if (wonPlayerType == PLAYER1) {
                    setScores(aInteger, 1);
                } else if (wonPlayerType == PLAYER2) {
                    setScores(aInteger, -1);
                } else {
                    throw new WhyAreYouEditingMyCodeException();
                }
            }
        }
        for (Integer key : keys) {
            if (!scores.containsKey(key)) {
                scores.put(key, 0);
            }
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("scores.txt"));
        objectOutputStream.writeObject(scores);
        objectOutputStream.close();
        System.out.println("Finished");
        System.out.println(scores);
    }

    private void setScores(Integer position, int scoreToSet) throws WhyAreYouEditingMyCodeException {
        if (scores.containsKey(position)) {
            Position pos = intToPos(position, fieldSize);
            if (pos.getPlayerToMove() == PLAYER1) {
                if (scoreToSet < scores.get(position)) {
                    scores.put(position, scoreToSet);
                }
            } else if (pos.getPlayerToMove() == PLAYER2) {
                if (scoreToSet > scores.get(position)) {
                    scores.put(position, scoreToSet);
                }
            } else {
                throw new WhyAreYouEditingMyCodeException();
            }
        } else {
            scores.put(position, scoreToSet);
        }
        List<Integer> intList = knownPositions.get(position);
        if (intList == null)
            return;
        List<Integer> prev = intList.subList(0, intList.size());
        knownPositions.remove(position);
        for (Integer aInteger : prev) {
            setScores(aInteger, -scoreToSet);
        }
    }

    private void addAllMoves(int field) throws IllegalMoveException, WhyAreYouEditingMyCodeException {
        Position pos = intToPos(field, fieldSize);
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
        for (Figure figure : figures) {
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

    public static int posToInt(List<Figure> figures, PlayerType playerToMove) throws WhyAreYouEditingMyCodeException {
        int result = 0;
        result |= playerToMove == PLAYER1 ? 0 : playerToMove == PLAYER2 ? 1 : -1;
        for (Figure figure : figures) {
            PlayerType type = figure.getPlayerType();
            if (type == PLAYER1) {
                result |= 1 << (figure.getX() * 2 + figure.getY() * 8) + 2;
            } else if (type == PLAYER2) {
                result |= 1 << (figure.getX() * 2 + figure.getY() * 8) + 1;
            } else {
                throw new WhyAreYouEditingMyCodeException();
            }
        }
        return result;
    }

    public static Position intToPos(Integer number, int fieldSize) {
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

    private static class WhyAreYouEditingMyCodeException extends Throwable {
    }
}
