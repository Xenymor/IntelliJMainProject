package Labyrinth;

import Labyrinth.Networking.Client;
import Labyrinth.Networking.DownloadWorldButton;
import Labyrinth.Networking.UploadWorldButton;
import StandardClasses.PathFinder;
import StandardClasses.PathFinder.Position;
import StandardClasses.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static Labyrinth.Main.BLOCK_SIZE;
import static Labyrinth.Main.isBuildModeOn;
import static Labyrinth.MyPanel.player;

public class Main {
    public static final Color PLAYER_COLOR = new Color(120, 255, 120);
    public static final Color BLOCK_COLOR = new Color(50, 0, 0);
    public static final Color ENEMY_COLOR = new Color(200, 0, 0);
    public static final Color FINISH_COLOR = new Color(200, 0, 200);
    private static final int grayScale = 80;
    public static final Color BOX_COLOR = new Color(255 - grayScale, 255 - grayScale, 255 - grayScale);
    private static final int grayScale1 = 160;
    public static final Color CHOSEN_BOX_COLOR = new Color(255 - grayScale1, 255 - grayScale1, 255 - grayScale1);
    public static final int BLOCK_SIZE = 20;
    public static final int BLOCK_COUNT_X = 40;
    public static final int BLOCK_COUNT_Y = 30;
    public static final int WINDOW_WIDTH = BLOCK_COUNT_X * BLOCK_SIZE;
    public static final int WINDOW_HEIGHT = BLOCK_COUNT_Y * BLOCK_SIZE;
    public static boolean north = false;
    public static boolean south = false;
    public static boolean east = false;
    public static boolean west = false;
    public static boolean clicked = false;
    public static Vector2 point;
    public static Symbols chosen = Symbols.BLOCK;
    static Level level;
    public static int mouseX;
    public static int mouseY;
    private static boolean buildModeOn = false;
    public static int levelNumber = 0;
    public static boolean rightClicked;
    public static String name;
    private static JPanel panel;

    public static List<File> getEveryFileInDirectory() {
        File folder = new File("out/production/Timon/Labyrinth/Worlds");
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> objects = new ArrayList<>();
        Collections.addAll(objects, listOfFiles);
        return objects;
    }

    public static void nextLevel() {
        load(levelNumber, name);
        levelNumber++;
        if (levelNumber > world.size()) {
            System.out.println("You won");
            System.exit(0);
        }
    }

    public static boolean isBuildModeOn() {
        return buildModeOn;
    }

    public static void main(String[] args) throws AWTException {
        chooseNameOfWorld();
        nextLevel();
        createFrame(level);
        Thread gameLoop = new MyThread();
        gameLoop.start();
    }

    public static void loadFromServer(String name) {
        try {
            Main.world = Client.load(name);
            Main.name = Main.world.getName();
            Main.level = Main.world.getLevel(0);
            MyPanel.level = Main.world.getLevel(0);
            player = MyPanel.level.getPlayer();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveToServer(World world) {
        String name;
        name = (String) JOptionPane.showInputDialog(
                null,
                "How do you want to name your world?",
                "World Upload",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                Main.name);

        if ((name == null) || (name.length() <= 0)) {
            JOptionPane.showConfirmDialog(
                    null,
                    "No name entered",
                    "Warning",
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!Objects.equals(world.getName(), Main.name)) {
            world.setName(Main.name);
        }
        return Client.save(world);
    }

    public static void chooseNameOfWorld() {
        while (true) {
            List<File> files = getEveryFileInDirectory();
            if (files.size() == 0) {
                String s = (String) JOptionPane.showInputDialog(
                        null,
                        "Which world do you want to load?",
                        "World Choice",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "Default");

                if ((s != null)) {
                    Main.name = s;
                    break;
                }
            } else {
                String[] possibilities = new String[files.size()];
                for (int i = 0; i < files.size(); i++) {
                    String name = files.get(i).getName();
                    name = name.replace("World_", "");
                    name = name.replace(".txt", "");
                    possibilities[i] = name;
                }
                String s = (String) JOptionPane.showInputDialog(
                        null,
                        "Which world do you want to load?",
                        "World Choice",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        "Default");

                if ((s != null)) {
                    Main.name = s;
                    break;
                }
            }
        }
    }

    private static void createFrame(Level level) throws AWTException {
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    Main.keyPressed(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Main.keyReleased(e);
            }
        };
        JFrame frame = new JFrame("Labyrinth");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = new MyPanel(level, new Player(new Vector2(BLOCK_SIZE * (WINDOW_WIDTH / BLOCK_SIZE) / 2, BLOCK_SIZE * (WINDOW_HEIGHT / BLOCK_SIZE) / 2), BLOCK_SIZE));
        panel.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.addKeyListener(keyListener);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    clicked = true;
                    point = new Vector2(e.getX(), e.getY());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClicked = true;
                    point = new Vector2(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        frame.setLayout(new BorderLayout());
        frame.add("Center", panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void keyPressed(KeyEvent e) throws IOException {
        if (Character.toLowerCase(e.getKeyChar()) == 'w') {
            north = true;
        } else if (Character.toLowerCase(e.getKeyChar()) == 'b') {
            buildModeOn = !buildModeOn;
        } else if (Character.toLowerCase(e.getKeyChar()) == 'a') {
            west = true;
        } else if (Character.toLowerCase(e.getKeyChar()) == 's') {
            south = true;
        } else if (Character.toLowerCase(e.getKeyChar()) == 'd') {
            east = true;
//        } else if (e.isControlDown()) {
//            System.out.println(e.getKeyChar());
//            for (int i = 0; i < 9; i++) {
//                if (Character.toString(e.getKeyChar()).equals(Integer.toString(i))) {
//                    System.out.println(i + " is down");
//                    save(level.getObjects(), player, i);
//                    System.out.println("Saved");
//                    break;
//                }
//                if (i == 6) {
//                    if (e.getKeyCode() == 54) {
//                        System.out.println(i + " is down");
//                        save(level.getObjects(), player, i);
//                        System.out.println("Saved");
//                        break;
//                    }
//                }
//            }
//        } else if (e.isAltDown()) {
//            for (int i = 0; i <= 9; i++) {
//                if (Character.toString(e.getKeyChar()).equals(Integer.toString(i))) {
//                    MyPanel.player = load(level, i);
//                    System.out.println("Loaded");
//                    break;
//                }
//            }
//        }
        }
    }

    private static void keyReleased(KeyEvent e) {
        if (Character.toLowerCase(e.getKeyChar()) == 'w') {
            north = false;
        } else if (Character.toLowerCase(e.getKeyChar()) == 'a') {
            west = false;
        } else if (Character.toLowerCase(e.getKeyChar()) == 's') {
            south = false;
        } else if (Character.toLowerCase(e.getKeyChar()) == 'd') {
            east = false;
        }
    }

    public static void saveLevel(GameObject[] objects, Player player, int number) {
        if (world.size() <= number) {
            world.addLevel(new Level(player, objects));
        } else {
            world.setLevel(number, new Level(player, objects));
        }
    }

    public static void saveWorld() {
        String name;
        name = (String) JOptionPane.showInputDialog(
                null,
                "How do you want to name your world?",
                "World Saving",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                Main.name);

        if ((name == null) || (name.length() <= 0)) {
            JOptionPane.showConfirmDialog(
                    null,
                    "No name entered",
                    "Warning",
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("out/production/Timon/Labyrinth/Worlds/World_" + name + ".txt");
            if (file.exists()) {
                int n = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to overwrite the world?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE);
                if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
                    System.out.println("No is pressed");
                    return;
                }
            }
            FileOutputStream w = new FileOutputStream("out/production/Timon/Labyrinth/Worlds/World_" + name + ".txt");
            ObjectOutputStream writer = new ObjectOutputStream(w);
            world.setName(name);
            Main.name = name;
            writer.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static World world = null;
    private static boolean shouldRepaint = false;

    public static void load(int number, String name) {
        if (world == null || !Objects.equals(name, world.getName())) {
            number = 0;
            getOrCreateWorld(name);
        }
        try {
            Main.level = world.getLevel(number);
            MyPanel.level = world.getLevel(number);
            player = Main.level.getPlayer();
            System.out.println("loaded: " + "out/production/Timon/Labyrinth/Worlds/World_" + name + ".txt" + ", " + number);
        } catch (IndexOutOfBoundsException ignored) {

        }
    }

    public static void getOrCreateWorld(String name) {
        if (new File("out/production/Timon/Labyrinth/Worlds/World_" + name + ".txt").exists()) {
            getWorld(name);
        } else {
            createWorld(name);
        }
    }

    public static void createWorld(String name) {
        world = new World(name);
        Player player = new Player(new Vector2(Main.WINDOW_WIDTH / 2, Main.WINDOW_HEIGHT / 2), Main.BLOCK_SIZE);
        world.addLevel(new Level(player));
    }

    public static void getWorld(String name) {
        try (FileInputStream reader = new FileInputStream("out/production/Timon/Labyrinth/Worlds/World_" + name + ".txt")) {
            ObjectInputStream objectReader = new ObjectInputStream(reader);
            world = (World) objectReader.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.exit(-999999999);
        }
    }

    public static List<Position> getPath(Vector2 start, Vector2 target, Level level) {
        Set<Position> blockedPos = new HashSet<>();
        for (GameObject object :
                level.getObjects()) {
            if (object instanceof Block || object instanceof Enemy) {
                blockedPos.add(object.getPos().toPosition(BLOCK_SIZE));
            }
        }
        return PathFinder.findShortestPath(start.toPosition(BLOCK_SIZE), target.toPosition(BLOCK_SIZE), blockedPos, BLOCK_COUNT_X, BLOCK_COUNT_Y);
    }

    public static void chooseNameOfServerWorld() {
        while (true) {
            List<File> files = Client.getEveryFileInDirectory();
            if (files.size() == 0) {
                String s = (String) JOptionPane.showInputDialog(
                        null,
                        "Which world do you want to load?",
                        "World Choice",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "Default");

                if ((s != null)) {
                    Main.name = s;
                    break;
                }
            } else {
                String[] possibilities = new String[files.size()];
                for (int i = 0; i < files.size(); i++) {
                    String name = files.get(i).getName();
                    name = name.replace("World_", "");
                    name = name.replace(".txt", "");
                    possibilities[i] = name;
                }
                String s = (String) JOptionPane.showInputDialog(
                        null,
                        "Which world do you want to load?",
                        "World Choice",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        "Default");

                if ((s != null)) {
                    Main.name = s;
                    break;
                }
            }
        }
    }
}

class MyPanel extends JPanel {
    public static Level level;
    Robot robot;
    public static Player player;
    private static GameObject[] lastObjects;
    private static final Symbol[] symbols = {
            new Symbol(Symbols.FINISH, 0, 100, 50, 50),
            new Symbol(Symbols.BLOCK, 0, 200, 50, 50),
            new Symbol(Symbols.ENEMY, 0, 300, 50, 50),
    };
    private static List<Button> buttons;

    public MyPanel(Level level, Player player) throws HeadlessException, AWTException {
        super();
        MyPanel.level = level;
        lastObjects = level.getObjects();
        this.robot = new Robot();
        MyPanel.player = player;
        buttons = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            buttons.add(new SaveButton(new Vector2(60 * i, 0), new Vector2(50, 50), i + 1));
            buttons.add(new LoadButton(new Vector2(Main.WINDOW_WIDTH - 50, 60 * i), new Vector2(50, 50), i + 1));
        }
        buttons.add(new SaveAllButton(new Vector2(600, 0), new Vector2(50, 50)));
        buttons.add(new LoadWorldButton(new Vector2(660, 0), new Vector2(50, 50)));
        buttons.add(new UploadWorldButton(new Vector2(0, Main.WINDOW_HEIGHT - 50), new Vector2(50, 50)));
        buttons.add(new DownloadWorldButton(new Vector2(60, Main.WINDOW_HEIGHT - 50), new Vector2(50, 50)));
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("Paint:" + System.identityHashCode(this));
        boolean newChosenSymbol = false;
        if (Main.clicked) {
            int index = -1;
            for (int i = 0; i < symbols.length; i++) {
                Rectangle rectangle = symbols[i].getRect();
                if (rectangle.contains(new Point(Main.mouseX, Main.mouseY))) {
                    symbols[i].clicked();
                    newChosenSymbol = true;
                    index = i;
                }
            }
            if (newChosenSymbol) {
                Main.clicked = false;
                for (int i = 0; i < symbols.length; i++) {
                    if (i != index) {
                        symbols[i].unChosen();
                    }
                }
            }
        }
        if (Main.clicked) {
            for (Button ui : buttons) {
                if (ui.isClicked(new Vector2(Main.mouseX, Main.mouseY))) {
                    ui.clicked();
                    Main.clicked = false;
                }
            }
        }
        g.clearRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        for (Button button :
                buttons) {
            if (Main.isBuildModeOn()) {
                button.paint(g);
            }
        }
        GameObject[] objects = level.getObjects();
        if (!newChosenSymbol) {
            if (Main.clicked && isBuildModeOn()) {
                if (Main.chosen == Symbols.BLOCK) {
                    Vector2 blockPos = new Vector2(
                            Math.floor(Main.point.getX() / BLOCK_SIZE) * BLOCK_SIZE,
                            Math.floor(Main.point.getY() / BLOCK_SIZE) * BLOCK_SIZE
                    );
                    level.add(new Block(blockPos, BLOCK_SIZE));
                }
                if (Main.chosen == Symbols.ENEMY) {
                    Vector2 enemyPos = new Vector2(
                            Math.floor(Main.point.getX() / BLOCK_SIZE) * BLOCK_SIZE,
                            Math.floor(Main.point.getY() / BLOCK_SIZE) * BLOCK_SIZE
                    );
                    Enemy toAdd = new Enemy(enemyPos, BLOCK_SIZE);
                    toAdd.newPath();
                    level.add(toAdd);
                }
                if (Main.chosen == Symbols.FINISH) {
                    Vector2 finishPos = new Vector2(
                            Math.floor(Main.mouseX / BLOCK_SIZE) * BLOCK_SIZE,
                            Math.floor(Main.mouseY / BLOCK_SIZE) * BLOCK_SIZE
                    );
                    Finish toAdd = new Finish(finishPos, BLOCK_SIZE);
                    level.add(toAdd);
                }
                Main.clicked = false;
            }
        }
        if (Main.rightClicked && isBuildModeOn()) {
            Vector2 blockPos = new Vector2(
                    Math.floor(Main.point.getX() / BLOCK_SIZE) * BLOCK_SIZE,
                    Math.floor(Main.point.getY() / BLOCK_SIZE) * BLOCK_SIZE
            );
            for (GameObject object :
                    objects) {
                if (object.getPos().getX() == blockPos.getX() && object.getPos().getY() == blockPos.getY()) {
                    level.delete(object);
                }
            }
            Main.rightClicked = false;
        }
        newPath(objects);
        for (GameObject object :
                objects) {
            object.draw(g);
        }
        player.draw(g);
        for (Symbol symbol :
                symbols) {
            symbol.paint(g);
        }
        EventQueue.invokeLater(this::repaint);
    }

    public static void newPath(GameObject[] objects) {
        if (!Arrays.equals(objects, lastObjects)) {
            for (GameObject object :
                    objects) {
                if (object instanceof Enemy) {
                    ((Enemy) object).newPath();
                }
            }
            lastObjects = objects;
        }
    }
}

class MyThread extends Thread {
    boolean shouldMove = false;
    long lastFrame = System.nanoTime();

    public void run() {
        while (true) {
//            System.out.println("Frame: " + (System.nanoTime()-lastFrame));
            lastFrame = System.nanoTime();
            GameObject[] objects = MyPanel.level.getObjects();
            for (GameObject object :
                    objects) {
                if (object instanceof Enemy) {
                    if (shouldMove) {
                        object.move(objects, player);
                    }
                } else {
                    object.move(objects, player);
                }
            }
            Vector2 lastPos = player.getPos();
            player.move(objects);
            if (!player.getPos().equals(lastPos)) {
                for (GameObject object :
                        objects) {
                    if (object instanceof Enemy) {
                        ((Enemy) object).newPath();
                    }
                }
            }
            shouldMove = !shouldMove;
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
