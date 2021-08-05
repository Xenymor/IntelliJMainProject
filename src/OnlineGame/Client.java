package OnlineGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    static final Color enemyColor = new Color(255, 50, 50);
    static final Color playerColor = new Color(50, 50, 255);
    static final Color allyColor = new Color(50, 240, 50);

    public static void main(String[] args) {
        players = new ArrayList<>();
        JPanel jPanel = new MyPanel();
        JFrame jFrame = new JFrame();
        jFrame.add(jPanel);
    }

    private static List<Player> players;

    private static class MyPanel extends JPanel {
        @Override
        public void paint(Graphics g) {

        }
    }
}
