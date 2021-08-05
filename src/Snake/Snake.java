package Snake;

import StandardClasses.Collider;
import StandardClasses.Vector2;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Snake {
    List<Vector2> positions;
    float blockSize;
    int length;
    direction lastDirection;
    Vector2 startPosition;

    enum direction {
        up, down, right, left, nothing
    }

    public Snake(Vector2 position, float blockSize) {
        positions = new ArrayList<>();
        positions.add(position);
        this.blockSize = blockSize;
        this.length = 1;
        lastDirection = direction.nothing;
        this.startPosition = position;
    }

    public void paint(Graphics g) {
        Color head = new Color(50, 255, 55);
        Color tail = new Color(161, 255, 0);
        Color after5 = new Color(30, 255, 247);
        Color after10 = new Color(40, 76, 255);
        Color after20 = new Color(16, 0, 255);
        for (int i = 0; i < positions.size(); i++) {
            Vector2 pos = positions.get(i);
            Block block = new Block(pos, blockSize);
            if (i == 0) {
                block.paint(g, head);
            } else if (i < 5) {
                block.paint(g, tail);
            } else if (i < 10) {
                block.paint(g, after5);
            } else if (i < 20) {
                block.paint(g, after10);
            } else {
                block.paint(g, after20);
            }
        }
    }

    public void move() {
        if (Main.wDown) {
            lastDirection = direction.up;
            move(direction.up);
        } else {
            if (Main.aDown) {
                lastDirection = direction.left;
                move(direction.left);
            } else {
                if (Main.sDown) {
                    lastDirection = direction.down;
                    move(direction.down);
                } else {
                    if (Main.dDown) {
                        lastDirection = direction.right;
                        move(direction.right);
                    } else {
                        move(lastDirection);
                    }
                }
            }
        }
    }

    private void move(direction dir) {
        float x = 0;
        float y = 0;
        if (dir == direction.up) {
            y = -1;
        }
        if (dir == direction.down) {
            y = 1;
        }
        if (dir == direction.left) {
            x = -1;
        }
        if (dir == direction.right) {
            x = 1;
        }
        Vector2[] oldPositions = new Vector2[positions.size()];
        positions.toArray(oldPositions);
        positions = new ArrayList<>();
        positions.add(new Vector2((long) (oldPositions[0].getX() + x * blockSize), (long) (oldPositions[0].getY() + y * blockSize)));
        Collections.addAll(positions, oldPositions);

        if (Main.apple.isTouching(positions.get(0))) {
            Main.apple.wasEaten();
            eatApple();
        }
        positions = positions.subList(0, length);
        if (positions.get(0).getX() < 0 || positions.get(0).getX() + blockSize > Main.windowSize.getX() || positions.get(0).getY() < 0 || positions.get(0).getY() + blockSize > Main.windowSize.getY()) {
            respawn();
        }
        Rectangle rect = new Rectangle((int) positions.get(0).getX(), (int) positions.get(0).getY(), (int) blockSize, (int) blockSize);
        for (int i = 1; i < positions.size(); i++) {
            Rectangle rect1 = new Rectangle((int) positions.get(i).getX(), (int) positions.get(i).getY(), (int) blockSize, (int) blockSize);
            if (Collider.isTouchingRectRect(rect, rect1)) {
                respawn();
            }
        }
    }

    public void eatApple() {
        length++;
    }

    void respawn() {
        positions = new ArrayList<>();
        Main.wDown = false;
        Main.aDown = false;
        Main.sDown = false;
        Main.dDown = false;
        lastDirection = direction.nothing;
        positions.add(startPosition);
        try {
            FileReader fr = new FileReader(Main.name + ".txt");
            BufferedReader reader = new BufferedReader(fr);
            String highscoreString = reader.readLine();
            reader.close();
            double highscore = Double.parseDouble(highscoreString);
            if (this.length * Main.tickDiv > highscore) {
                FileWriter fw = new FileWriter(Main.name + ".txt");
                BufferedWriter writer = new BufferedWriter(fw);
                writer.write(Double.toString(this.length * Main.tickDiv));
                writer.close();
            }
        } catch (IOException e) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(Main.name + ".txt");
                BufferedWriter writer = new BufferedWriter(fw);
                writer.write(Double.toString(this.length * Main.tickDiv));
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        this.length = 1;
    }
}
