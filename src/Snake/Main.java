package Snake;

import StandardClasses.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class Main {
    public final static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final static StandardClasses.Vector2 windowSize = new StandardClasses.Vector2(600, 600);
    public final static int blockSize = 20;
    public static Snake snake = new Snake(new Vector2((int) (windowSize.getX() / 2), (int) (windowSize.getY() / 2)), blockSize);
    public static Apple apple = new Apple(blockSize);
    public static boolean wDown = false;
    public static boolean aDown = false;
    public static boolean sDown = false;
    public static boolean dDown = false;
    public static double tickTime = 500;
    public static double tickDiv = 1;
    public static String name = "Unbekannt";
    static KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'w') {
                wDown = true;
                aDown = false;
                sDown = false;
                dDown = false;
            }
            if (e.getKeyChar() == 'a') {
                wDown = false;
                aDown = true;
                sDown = false;
                dDown = false;
            }
            if (e.getKeyChar() == 's') {
                wDown = false;
                aDown = false;
                sDown = true;
                dDown = false;
            }
            if (e.getKeyChar() == 'd') {
                wDown = false;
                aDown = false;
                sDown = false;
                dDown = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'w') {
                wDown = false;
            }
            if (e.getKeyChar() == 'a') {
                aDown = false;
            }
            if (e.getKeyChar() == 's') {
                sDown = false;
            }
            if (e.getKeyChar() == 'd') {
                dDown = false;
            }
        }
    };

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Object[] possibilities;
        possibilities = new Object[100];
        for (float i = 0; i < possibilities.length; i++) {
            possibilities[(int) i] = i / 2 + 0.5;
        }
        Icon icon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {

            }

            @Override
            public int getIconWidth() {
                return 0;
            }

            @Override
            public int getIconHeight() {
                return 0;
            }
        };
        name = (String) JOptionPane.showInputDialog(
                frame,
                "Wie heißt du?",
                "Name",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null,
                "Unbekannt");
        Double d = (Double) JOptionPane.showInputDialog(
                frame,
                "Schwierigkeit (höher = schwieriger)",
                "Schwierigkeitsstufe",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                possibilities,
                "1");
        Main.tickTime = tickTime / d;
        Main.tickDiv = d;
        openWindow();
        while (true) {
            playWav("src\\0108. Winter - AShamaluevMusic (online-audio-converter.com).wav");
            TimeUnit.SECONDS.sleep(185);
        }
    }

    public static void playWav(String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    static void openWindow() {
        MyFrame frame = new MyFrame();
        MyCanvas canvas = new MyCanvas();
        frame.addKeyListener(keyListener);
        canvas.setBounds(0, 0, (int) windowSize.getX(), (int) windowSize.getY());
        frame.add(canvas);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLocation((int) (screenSize.getWidth() / 2 - windowSize.getX() / 2), (int) (screenSize.getHeight() / 2 - windowSize.getY() / 2));
        frame.pack();
        frame.setVisible(true);
    }
}

class MyFrame extends JFrame {

}

class MyCanvas extends Canvas {
    BufferedImage buffer;
    Graphics g;

    public MyCanvas() {
        buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        g = buffer.getGraphics();
    }

    @Override
    public void paint(Graphics graphics) {
        if (buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            g = buffer.getGraphics();
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int) Main.windowSize.getX(), (int) Main.windowSize.getY());
        // Move Stuff
        Main.snake.move();
        // Draw Stuff
        Main.snake.paint(g);
        Main.apple.paint(g);
        graphics.drawImage(buffer, 0, 0, null);
        // wait
        try {
            TimeUnit.MILLISECONDS.sleep((long) Main.tickTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }
}
