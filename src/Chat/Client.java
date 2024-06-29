package Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    private static String ipAddress = "192.168.86.219";

    public static void println(String text, JTextArea textArea) {
        textArea.append(text + "\n");
        textArea.scrollRectToVisible(new Rectangle(0, textArea.getHeight(), 1, 1));
    }

    public static void main(String... args) throws IOException, ClassNotFoundException {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        jFrame.add("Center", scrollPane);
        JTextField textField = new JTextField();
        final String[] userName = {""};
        Socket socket = getSocket();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setSize(screenSize.width / 3, screenSize.height / 3 * 2);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        System.out.println("Enter IP-Address");
        while (true) {
            try {
                ipAddress = new Scanner(System.in).nextLine();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        println("Enter Username", jTextArea);
        textField.addActionListener(new ActionListener() {
            boolean didGetCalled = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (didGetCalled) {
//                    println(textField.getText(), jTextArea);
                    send(jTextArea, textField.getText(), userName[0], socket, objectOutputStream);
                    textField.setText("");
                } else {
                    userName[0] = textField.getText();
                    try {
                        objectOutputStream.writeObject(userName[0]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    textField.setText("");
                    didGetCalled = true;
                }
            }
        });
        jTextArea.setFont(Font.getFont(Font.SERIF));
        textField.setFont(Font.getFont(Font.SERIF));
        jFrame.add("South", textField);
        jFrame.setVisible(true);
        List<Message> chat = (List<Message>) objectInputStream.readObject();
        for (Message msg : chat) {
            println(msg.getUserName() + " : " + msg.getMessage(), jTextArea);
        }
        new Thread(() -> {
            while (true) {
                if (socket.isClosed()) {
                    println("Connection Closed", jTextArea);
                    return;
                }
                try {
                    if (inputStream.available() > 0) {
                        Message msg = (Message) objectInputStream.readObject();
                        println(msg.getUserName() + " : " + msg.getMessage(), jTextArea);
                    }
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Socket getSocket() throws IOException {
//        String ip = (String) JOptionPane.showInputDialog(
//                null,
//                "What ip does the server have?",
//                "IP",
//                JOptionPane.PLAIN_MESSAGE,
//                null,
//                null,
//                "");
        Socket socket;
        try {
//            if (ip.length() == 0) {
//                socket = new Socket("192.168.86.30", 2002);
//            } else {
//                socket = new Socket(ip, 2002);
//            }
            socket = new Socket(ipAddress, 2002);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            socket = getSocket();
        }
        return socket;
    }

    private static void send(JTextArea jTextArea, String message, String userName, Socket socket, ObjectOutputStream objectOutputStream) {
        try {
            Message msg = new Message(message, userName);
            println(msg.getUserName() + " : " + msg.getMessage(), jTextArea);
            objectOutputStream.writeObject(msg);
        } catch (IOException e) {
            try {
                socket.close();
                main(new String[1]);
            } catch (IOException | ClassNotFoundException ioException) {
                System.exit(-12345);
            }
        }
    }
}
