package PawnGame;

import StandardClasses.Random;
import StandardClasses.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static PawnGame.PlayerType.*;
import static StandardClasses.MyArrays.deepCloneEnum;


public class Game {
    private static final Vector2 screenSize = new Vector2(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    private static final int fieldSizeX = 4;
    private static final int fieldSizeY = 4;
    private static final int depth = 10;
    private static final int stripSize = 20;
    private static final int squareSizeX = (screenSize.getXAsInt() / fieldSizeX);
    private static final int squareSizeY = (screenSize.getYAsInt() / fieldSizeY);
    private static final Color PLAYER_COLOR_1 = new Color(50, 20, 20);
    private static final Color PLAYER_COLOR_2 = new Color(240, 240, 240);
    private static final Color NEUTRAL_COLOR = new Color(127, 255, 212);
    private static final Color CHOSEN_COLOR = new Color(50, 200, 50);
    private static final Color POSSIBLE_FIELD_COLOR = new Color(173, 0, 0);
    private static Vector2 clicked = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Labyrinth");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(screenSize.getXAsInt(), screenSize.getYAsInt());
        JPanel panel;
        PlayerType[][] field = new PlayerType[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (y == 0 && x == 0) {
                    field[x][y] = NEUTRAL;
                } else {
                    if (y == 0) {
                        field[x][y] = PLAYER1;
                    } else if (x == 0) {
                        field[x][y] = PLAYER2;
                    } else {
                        field[x][y] = NEUTRAL;
                    }
                }
            }
        }
        panel = new MyPanel(field);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clicked = new Vector2(e.getX(), e.getY());
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        panel.setMinimumSize(new Dimension(screenSize.getXAsInt(), screenSize.getYAsInt()));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.add("Center", panel);
        frame.setVisible(true);
    }

    public static class MyPanel extends JPanel {
        PlayerType[][] field;
        static PlayerType lastMovedPlayer = PLAYER2;
        int clickedIndexX = -1;
        int clickedIndexY = -1;

        public MyPanel(PlayerType[][] field) {
            this.field = field;
        }

        @Override
        public void paint(Graphics g) {
            g.clearRect(0, 0, screenSize.getXAsInt(), screenSize.getYAsInt());
            g.setColor(NEUTRAL_COLOR);
            g.fillRect(0, 0, screenSize.getXAsInt(), screenSize.getYAsInt());
            g.setColor(Color.black);
            for (int x = 0; x < screenSize.getXAsInt(); x += squareSizeX) {
                g.fillRect(x, 0, stripSize, screenSize.getYAsInt());
            }
            for (int y = 0; y < screenSize.getYAsInt(); y += squareSizeY) {
                g.fillRect(0, y, screenSize.getXAsInt(), stripSize);
            }
            g.fillRect(screenSize.getXAsInt() - stripSize, 0, stripSize, screenSize.getYAsInt());
            g.fillRect(0, screenSize.getYAsInt() - stripSize, screenSize.getXAsInt(), stripSize);
            if (lastMovedPlayer == PLAYER1) {
                PlayerType[][] fieldClone = deepCloneEnum(field);
                System.out.println("Starting Calculation with depth: " + depth);
                long time = System.nanoTime();
                MoveWithScore bestMove = getBotMove(fieldClone, PLAYER2, depth, PLAYER1);
                long duration = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - time);
                System.out.println("Completed Calculation: " + duration + " | " + bestMove.getScore());
                Move move = bestMove.getMove();
                field[move.getxFrom()][move.getyFrom()] = NEUTRAL;
                try {
                    field[move.getxTo()][move.getyTo()] = PLAYER2;
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
                lastMovedPlayer = PLAYER2;
            }
            if (clicked != null) {
                if (clickedIndexX == -1) {
                    for (int x = 0; x < fieldSizeX; x++) {
                        for (int y = 0; y < fieldSizeY; y++) {
                            Rectangle rect = new Rectangle(x * squareSizeX, y * squareSizeY, squareSizeX, squareSizeY);
                            if (rect.contains(clicked.getX(), clicked.getY())) {
                                clickedIndexX = x;
                                clickedIndexY = y;
                                break;
                            }
                        }
                    }
                    if (clickedIndexX != -1 && clickedIndexY != -1) {
                        if (field[clickedIndexX][clickedIndexY] == NEUTRAL) {
                            clickedIndexX = -1;
                            clickedIndexY = -1;
                        }
                    }
                } else {
                    boolean calculate = true;
                    if (clicked.getX() >= screenSize.getX() - stripSize || clicked.getY() >= screenSize.getY() - stripSize) {
                        if (field[clickedIndexX][clickedIndexY] == PLAYER1 && lastMovedPlayer == PLAYER2) {
                            calculate = false;
                            if (clickedIndexY == fieldSizeY - 1 && clicked.getY() >= screenSize.getY() - stripSize) {
                                lastMovedPlayer = field[clickedIndexX][clickedIndexY];
                                field[clickedIndexX][clickedIndexY] = NEUTRAL;
                                clickedIndexX = -1;
                                clickedIndexY = -1;
                            }
                        } else if (field[clickedIndexX][clickedIndexY] == PLAYER2 && lastMovedPlayer == PLAYER1) {
                            calculate = false;
                            if (clickedIndexX == fieldSizeX - 1 && clicked.getX() >= screenSize.getX() - stripSize) {
                                lastMovedPlayer = field[clickedIndexX][clickedIndexY];
                                field[clickedIndexX][clickedIndexY] = NEUTRAL;
                                clickedIndexX = -1;
                                clickedIndexY = -1;
                            }
                        }
                    }
                    if (calculate) {
                        int chosenIndexX = -1;
                        int chosenIndexY = -1;
                        for (int x = 0; x < fieldSizeX; x++) {
                            for (int y = 0; y < fieldSizeY; y++) {
                                Rectangle rect = new Rectangle(x * squareSizeX, y * squareSizeY, squareSizeX, squareSizeY);
                                if (rect.contains(clicked.getX(), clicked.getY())) {
                                    chosenIndexX = x;
                                    chosenIndexY = y;
                                    break;
                                }
                            }
                        }
                        if (chosenIndexX != -1) {
                            if (field[chosenIndexX][chosenIndexY] != PLAYER1
                                    && field[chosenIndexX][chosenIndexY] != PLAYER2
                                    && moveAllowed(field[clickedIndexX][clickedIndexY], clickedIndexX, clickedIndexY, chosenIndexX, chosenIndexY, field)
                            ) {
                                lastMovedPlayer = field[clickedIndexX][clickedIndexY];
                                field[chosenIndexX][chosenIndexY] = lastMovedPlayer;
                                field[clickedIndexX][clickedIndexY] = NEUTRAL;
                            }
                        }
                        clickedIndexX = -1;
                        clickedIndexY = -1;
                    }
                }
                clicked = null;
            }
            if (clickedIndexX != -1 && clickedIndexY != -1) {
                boolean[][] allowedMoves = getAllAllowedMoves(field, clickedIndexX, clickedIndexY);
                for (int x = 0; x < fieldSizeX; x++) {
                    for (int y = 0; y < fieldSizeY; y++) {
                        if (allowedMoves[x][y]) {
                            g.setColor(POSSIBLE_FIELD_COLOR);
                            int roundRectSizeX = squareSizeX / 2;
                            int roundRectSizeY = squareSizeY / 2;
                            g.fillOval(x * squareSizeX + squareSizeX / 4, y * squareSizeY + squareSizeY / 4, roundRectSizeX, roundRectSizeY);
                        }
                    }
                }
                if (field[clickedIndexX][clickedIndexY] == PLAYER2 && lastMovedPlayer == PLAYER1) {
                    if (clickedIndexX == fieldSizeX - 1) {
                        g.setColor(POSSIBLE_FIELD_COLOR);
                        g.fillRect(screenSize.getXAsInt() - stripSize, 0, stripSize, screenSize.getYAsInt());
                    }
                } else if (field[clickedIndexX][clickedIndexY] == PLAYER1 && lastMovedPlayer == PLAYER2) {
                    if (clickedIndexY == fieldSizeY - 1) {
                        g.setColor(POSSIBLE_FIELD_COLOR);
                        g.fillRect(0, screenSize.getYAsInt() - stripSize, screenSize.getXAsInt(), stripSize);
                    }
                }
            }
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    PlayerType type = field[x][y];
                    if (type != NEUTRAL) {
                        if (type == PLAYER1) {
                            g.setColor(PLAYER_COLOR_1);
                        } else if (type == PLAYER2) {
                            g.setColor(PLAYER_COLOR_2);
                        }
                        if (x == clickedIndexX && y == clickedIndexY) {
                            g.setColor(CHOSEN_COLOR);
                        }
                        int roundRectSizeX = squareSizeX / 2;
                        int roundRectSizeY = squareSizeY / 2;
                        g.fillRoundRect(x * squareSizeX + squareSizeX / 4, y * squareSizeY + squareSizeY / 4, roundRectSizeX, roundRectSizeY, roundRectSizeX / 10, roundRectSizeY / 10);
                    }
                }
            }
            PlayerType type = hasWon(lastMovedPlayer, field);
            if (type == PLAYER1) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
                g.drawString("BROWN WON", screenSize.getXAsInt() / 3, screenSize.getYAsInt() / 2 - 10);
            } else if (type == PLAYER2) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
                g.drawString("WHITE WON", screenSize.getXAsInt() / 3, screenSize.getYAsInt() / 2 - 10);
            }
            repaint();
        }

        private static PlayerType hasWon(PlayerType lastMovedPlayer, PlayerType[][] field) {
            boolean Player1Exists = false;
            boolean Player2Exists = false;
            boolean hasMove = false;
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    switch (field[x][y]) {
                        case PLAYER1:
                            Player1Exists = true;
                            break;
                        case PLAYER2:
                            Player2Exists = true;
                            break;
                    }
                    if (field[x][y] == PLAYER2) {
                        if (getAllAllowedMoves(field, x, y, PLAYER1).size() > 0) {
                            hasMove = true;
                        }
                    }
                    if (field[x][y] == PLAYER1) {
                        if (getAllAllowedMoves(field, x, y, PLAYER1).size() > 0) {
                            hasMove = true;
                        }
                    }
                }
            }

            if (!Player1Exists) {
                return PLAYER1;
            }
            if (!Player2Exists) {
                return PLAYER2;
            }
            if (!hasMove) {
                return getEnemyType(lastMovedPlayer);
            }
            return NEUTRAL;
        }

        private static MoveWithScore getBotMove(PlayerType[][] fieldOriginal, PlayerType botType, int depth, PlayerType lastMovedPlayer) {
            if (depth >= 7) System.out.println("Calculation Depth: " + depth);
            MoveWithScore bestMove = new MoveWithScore(new Move(-1, -1, -1, -1), Integer.MIN_VALUE);
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    if (fieldOriginal[x][y] == botType) {
                        PlayerType[][] field = deepCloneEnum(fieldOriginal);
                        List<MoveWithScore> movesWithScores = getAllAllowedMovesWithScore(field, x, y, lastMovedPlayer);
                        for (MoveWithScore move : movesWithScores) {
                            PlayerType playerType = hasWon(lastMovedPlayer, deepCloneEnum(field));
                            if (playerType != NEUTRAL) {
                                if (playerType == botType) {
                                    move.setScore(Integer.MAX_VALUE);
                                } else if (playerType == lastMovedPlayer) {
                                    move.setScore(Integer.MIN_VALUE + 1);
                                }
                            } else {
                                if (depth > 0 && movesWithScores.size() > 1) {
                                    PlayerType[][] newField = deepCloneEnum(field);
                                    PlayerType type = newField[move.getMove().getxFrom()][move.getMove().getyFrom()];
                                    newField[move.getMove().getxFrom()][move.getMove().getyFrom()] = NEUTRAL;
                                    try {
                                        newField[move.getMove().getxTo()][move.getMove().getyTo()] = type;
                                    } catch (IndexOutOfBoundsException ignored) {

                                    }
                                    PlayerType newLastMovedPlayer = getEnemyType(lastMovedPlayer);
                                    MoveWithScore botMove = getBotMove(newField, getEnemyType(botType), depth - 1, newLastMovedPlayer);
                                    move.setScore(-botMove.getScore());
                                }
                            }
                            if (move.getScore() >= bestMove.getScore()) {
                                bestMove = move;
                            }
                        }
                    }
                }
            }
            if (depth >= 7)
            System.out.println(bestMove.getScore());
            return bestMove;
        }

        private static MoveWithScore getBestMove(int startX, int startY, int[][] scores) {
            int highest = Integer.MIN_VALUE;
            int indexX = -1;
            int indexY = -1;
            for (int x = 0; x < fieldSizeX + 1; x++) {
                for (int y = 0; y < fieldSizeY + 1; y++) {
                    if (scores[x][y] >= highest) {
                        highest = scores[x][y];
                        indexX = x;
                        indexY = y;
                    }
                }
            }
            return new MoveWithScore(new Move(startX, startY, indexX, indexY), highest);
        }

        private static List<MoveWithScore> getScores(List<Move> allowedMoves, PlayerType[][] field, PlayerType lastMovedPlayer) {
            return allowedMoves
                    .stream()
                    .map(move -> new MoveWithScore(move, getScore(move, field)))
                    .collect(Collectors.toList());
        }

        private static int getScore(Move move, PlayerType[][] field) {
            PlayerType type = field[move.getxFrom()][move.getyFrom()];
            int score = 0;
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    if (field[x][y] == PLAYER1) {
                        if (type == PLAYER1) {
                            score -= fieldSizeY - y;
                        } else {
                            score += fieldSizeY - y;
                        }
                    }
                    if (field[x][y] == PLAYER2) {
                        if (type == PLAYER2) {
                            score -= fieldSizeX - x;
                        } else {
                            score += fieldSizeX - x;
                        }
                    }
                }
            }
            return score;
        }

        private static boolean moveAllowed(Move move, PlayerType[][] field, PlayerType lastMovedPlayer) {
            return moveAllowed(field[move.getxFrom()][move.getyFrom()], move.getxFrom(), move.getyFrom(), move.getxTo(), move.getyTo(), field, lastMovedPlayer);
        }

        private static PlayerType getEnemyType(PlayerType alliedType) {
            switch (alliedType) {
                case PLAYER1:
                    return PLAYER2;
                case PLAYER2:
                    return PLAYER1;
                case NEUTRAL:
                    return NEUTRAL;
            }
            return null;
        }

        private Move botMoveRandom(PlayerType[][] field, PlayerType botPlayerType) {
            List<Vector2> figures = new ArrayList<>();
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    if (field[x][y] == botPlayerType) {
                        figures.add(new Vector2(x, y));
                    }
                }
            }
            int random;
            List<Vector2> possibleMovesList;
            do {
                random = Random.randomIntInRange(0, figures.size() - 1);
                boolean[][] possibleMoves = getAllAllowedMoves(field, figures.get(random).getXAsInt(), figures.get(random).getYAsInt());
                possibleMovesList = new ArrayList<>();
                for (int x = 0; x < fieldSizeX; x++) {
                    for (int y = 0; y < fieldSizeY; y++) {
                        if (possibleMoves[x][y]) {
                            possibleMovesList.add(new Vector2(x, y));
                        }
                    }
                }
            } while (possibleMovesList.size() <= 0);
            int randomMove = Random.randomIntInRange(0, possibleMovesList.size() - 1);
            return new Move(figures.get(random).getXAsInt(), figures.get(random).getYAsInt(), possibleMovesList.get(randomMove).getXAsInt(), possibleMovesList.get(randomMove).getYAsInt());
        }

        public static boolean[][] getAllAllowedMoves(PlayerType[][] field, int chosenFigureIndexX, int chosenFigureIndexY) {
            boolean[][] allowedMoves = new boolean[fieldSizeX][fieldSizeY];
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    allowedMoves[x][y] = moveAllowed(field[chosenFigureIndexX][chosenFigureIndexY], chosenFigureIndexX, chosenFigureIndexY, x, y, field);
                }
            }

            return allowedMoves;
        }

        public static List<Move> getAllAllowedMoves(PlayerType[][] field, int chosenX, int chosenY, PlayerType lastMovedPlayer) {
            ArrayList<Move> result = new ArrayList<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dy == 0 && dx == 0) {
                        continue;
                    }
                    final int x = chosenX + dx;
                    final int y = chosenY + dy;
                    if (moveAllowed(field[chosenX][chosenY], chosenX, chosenY, x, y, field, lastMovedPlayer)) {
                        result.add(new Move(chosenX, chosenY, x, y));
                    }
                }
            }
            if (field[chosenX][chosenY] == PLAYER2 && lastMovedPlayer == PLAYER1) {
                if (chosenX == fieldSizeX - 1) {
                    result.add(new Move(chosenX, chosenY, fieldSizeX, chosenY));
                }
            } else if (field[chosenX][chosenY] == PLAYER1 && lastMovedPlayer == PLAYER2) {
                if (chosenY == fieldSizeY - 1) {
                    result.add(new Move(chosenX, chosenY, chosenX, fieldSizeY));
                }
            }
            return result;
        }

        public static List<MoveWithScore> getAllAllowedMovesWithScore(PlayerType[][] field, int chosenX, int chosenY, PlayerType lastMovedPlayer) {
            ArrayList<MoveWithScore> result = new ArrayList<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dy == 0 && dx == 0) {
                        continue;
                    }
                    final int x = chosenX + dx;
                    final int y = chosenY + dy;
                    if (moveAllowed(field[chosenX][chosenY], chosenX, chosenY, x, y, field, lastMovedPlayer)) {
                        Move move = new Move(chosenX, chosenY, x, y);
                        result.add(new MoveWithScore(move, getScore(move, field)));
                    }
                }
            }
            if (field[chosenX][chosenY] == PLAYER2 && lastMovedPlayer == PLAYER1) {
                if (chosenX == fieldSizeX - 1) {
                    Move move = new Move(chosenX, chosenY, fieldSizeX, chosenY);
                    result.add(new MoveWithScore(move, getScore(move, field)));
                }
            } else if (field[chosenX][chosenY] == PLAYER1 && lastMovedPlayer == PLAYER2) {
                if (chosenY == fieldSizeY - 1) {
                    Move move = new Move(chosenX, chosenY, chosenX, fieldSizeY);
                    result.add(new MoveWithScore(move, getScore(move, field)));
                }
            }
            return result;
        }

        private static boolean moveAllowed(PlayerType type, int xFrom, int yFrom, int xTo, int yTo, PlayerType[][] field) {
            final int travelDistance = Math.abs(xFrom - xTo) + Math.abs(yFrom - yTo);
            if (type == lastMovedPlayer) {
                return false;
            }
            if (field[xTo][yTo] != NEUTRAL) {
                return false;
            }
            switch (type) {
                case NEUTRAL:
                    return false;
                case PLAYER2:
                    return xTo >= xFrom
                            && travelDistance == 1;
                case PLAYER1:
                    return yTo >= yFrom
                            && travelDistance == 1;
                default:
                    throw new IllegalArgumentException("Unkown type: " + type);
            }
        }

        private static boolean moveAllowed(PlayerType type, int xFrom, int yFrom, int xTo, int yTo, PlayerType[][] field, PlayerType lastMovedPlayer) {
            if (xTo < 0 || yTo < 0) {
                return false;
            }
            try {
                final int travelDistance = Math.abs(xFrom - xTo) + Math.abs(yFrom - yTo);
                if (type == lastMovedPlayer) {
                    return false;
                }
                if (field[xTo][yTo] != NEUTRAL) {
                    return false;
                }
                switch (type) {
                    case NEUTRAL:
                        return false;
                    case PLAYER2:
                        return xTo >= xFrom
                                && travelDistance == 1;
                    case PLAYER1:
                        return yTo >= yFrom
                                && travelDistance == 1;
                    default:
                        throw new IllegalArgumentException("Unkown type: " + type);
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }

        static class MoveWithScore {
            private Move move;
            private int score;

            public MoveWithScore(Move move, int score) {
                this.move = move;
                this.score = score;
            }

            public Move getMove() {
                return move;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public void setMove(Move move) {
                this.move = move;
            }
        }
    }
}
