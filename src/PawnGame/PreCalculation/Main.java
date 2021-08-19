package PawnGame.PreCalculation;

import PawnGame.Move;
import PawnGame.PlayerType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static PawnGame.PlayerType.*;

public class Main {
    private final int fieldSize = 2;
    HashMap<Long, List<Long>> knownPositions = new HashMap<>();
    private HashMap<Long, Integer> scores;
    private final String pathToSaveFile = "scores2x2.obj";

    public static void main(String[] args) throws IllegalMoveException, IOException, ClassNotFoundException, WhyAreYouEditingMyCodeException {
        new Main().run();
        new Main().printStartingScore();
    }

    private void printStartingScore() throws IOException, ClassNotFoundException, WhyAreYouEditingMyCodeException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathToSaveFile));
        HashMap<Long, Long> scores = (HashMap<Long, Long>) objectInputStream.readObject();
        long startingPositionAsInt = getStartingPositionAsLong();
        System.out.println(startingPositionAsInt + " = " + scores.get(startingPositionAsInt));
    }

    private long getStartingPositionAsLong() throws WhyAreYouEditingMyCodeException {
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
        return posToLong(figures, PLAYER1);
    }

    private void run() throws IllegalMoveException, WhyAreYouEditingMyCodeException, IOException {
        long field = getStartingPositionAsLong();
        knownPositions.put(field, new ArrayList<>());
        addAllMoves(field);
        scores = new HashMap<>();
        Long[] keys = knownPositions.keySet().toArray(new Long[1]);
        for (Long aLong : keys) {
            PlayerType wonPlayerType = whoHasWon(longToPos(aLong, fieldSize));
            if (wonPlayerType != NEUTRAL) {
                if (wonPlayerType == PLAYER1) {
                    setScores(aLong, 1);
                } else if (wonPlayerType == PLAYER2) {
                    setScores(aLong, -1);
                } else {
                    throw new WhyAreYouEditingMyCodeException();
                }
            }
        }
        for (Long key : keys) {
            if (!scores.containsKey(key)) {
                scores.put(key, 0);
            }
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathToSaveFile));
        objectOutputStream.writeObject(scores);
        objectOutputStream.close();
        System.out.println("Finished");
        System.out.println(scores);
    }

    private void setScores(Long position, int scoreToSet) throws WhyAreYouEditingMyCodeException {
        if (scores.containsKey(position)) {
            Position pos = longToPos(position, fieldSize);
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
        List<Long> intList = knownPositions.get(position);
        if (intList == null)
            return;
        List<Long> prev = intList.subList(0, intList.size());
        for (Long aLong : prev) {
            setScores(aLong, scoreToSet);
        }
    }

    private void addAllMoves(long field) throws IllegalMoveException, WhyAreYouEditingMyCodeException {
        try {
            Position pos = longToPos(field, fieldSize);
            PlayerType playerToMove = pos.getPlayerToMove();
            PlayerType wonPlayer = whoHasWon(pos);
            if (wonPlayer != NEUTRAL) {
                return;
            }
            List<Move> allMoves = getAllAllowedMoves(pos.getFigures(), playerToMove);
            for (Move allMove : allMoves) {
                Position potentialPos = doMove(allMove, pos);
                potentialPos.setPlayerToMove(getEnemy(potentialPos.getPlayerToMove()));
                long potentialPosInt = posToLong(potentialPos.getFigures(), potentialPos.getPlayerToMove());
                if (!addField(potentialPosInt, field)) {
                    continue;
                }
                addAllMoves(potentialPosInt);
            }
        } catch (StackOverflowError e) {
            System.out.println(Long.toBinaryString(field));
            throw e;
        }
    }

    private PlayerType whoHasWon(Position pos) throws WhyAreYouEditingMyCodeException {
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
                    if (move.getxTo() == fieldSize || move.getyTo() == fieldSize) {
                        figures.remove(figure);
                        pos.setFigures(figures);
                    } else {
                        figure.setX(move.getxTo());
                        figure.setY(move.getyTo());
                    }
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

    private boolean addField(long field, long previous) {
        boolean wasAdded = false;
        if (!knownPositions.containsKey(field)) {
            knownPositions.put(field, new ArrayList<>());
            wasAdded = true;
        }
        knownPositions.get(field).add(previous);
        return wasAdded;
    }

    private List<Move> getAllAllowedMoves(List<Figure> figures, PlayerType playerToMove) throws WhyAreYouEditingMyCodeException {
        final List<Move> allowedMoves = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            Figure figure = figures.get(i);
            if (figure.getPlayerType() == playerToMove) {
                allowedMoves.addAll(getAllAllowedMoves(figure, figures, playerToMove));
            }
        }
        return allowedMoves;
    }

    private List<Move> getAllAllowedMoves(Figure figure, List<Figure> figures, PlayerType playerToMove) throws WhyAreYouEditingMyCodeException {
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

    private boolean isAllowed(Move move, List<Figure> figures, PlayerType playerToMove, Figure figureToMove) throws WhyAreYouEditingMyCodeException {
        //check figure type
        if (figureToMove.getPlayerType() != playerToMove) {
            return false;
        }
        //check distance
        if ((move.getxFrom() - move.getxTo()) * (move.getxFrom() - move.getxTo()) + (move.getyFrom() - move.getyTo()) * (move.getyFrom() - move.getyTo()) != 1) {
            return false;
        }
        //check if target location is blocked
        for (Figure figure : figures) {
            if (figure.getX() == move.getxTo() && figure.getY() == move.getyTo()) {
                return false;
            }
        }
        //check if figure exists
        if (!figures.contains(figureToMove)) {
            return false;
        }
        //check if too small
        if (move.getxTo() < 0 || move.getyTo() < 0 || move.getxFrom() < 0 || move.getyFrom() < 0) {
            return false;
        }
        //check if start too big
        if (move.getxFrom() > fieldSize - 1 || move.getyFrom() > fieldSize - 1) {
            return false;
        }
        //don't allow backwards movement
        if (playerToMove == PLAYER1) {
            if (move.getxFrom() > move.getxTo()) {
                return false;
            }
            //check if out of bounds
            if (move.getyTo() > fieldSize - 1) {
                return false;
            }
            if (move.getxTo() > fieldSize) {
                return false;
            }
        } else if (playerToMove == PLAYER2) {
            if (move.getyFrom() > move.getyTo()) {
                return false;
            }
            //check if out of bounds
            if (move.getxTo() > fieldSize - 1) {
                return false;
            }
            if (move.getyTo() > fieldSize) {
                return false;
            }
        } else {
            throw new WhyAreYouEditingMyCodeException();
        }
        //allow move
        return true;
    }

    public static long posToLong(List<Figure> figures, PlayerType playerToMove) throws WhyAreYouEditingMyCodeException {
        long result = 0;
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

    public static Position longToPos(Long number, long fieldSize) {
        List<Figure> figures = new ArrayList<>();
        long i = (number) & 1;
        PlayerType playerToMove = i == 0 ? PLAYER1 : PLAYER2;
        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                long firstBit = (number >> (x * 2 + 8 * y) + 1) & 1;
                long secondBit = (number >> (x * 2 + 8 * y) + 2) & 1;
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
