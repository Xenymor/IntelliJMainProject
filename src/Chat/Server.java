package Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    private static List<Message> chat;
    private static List<String> names;

    public static void main(String[] args) throws IOException {
        chat = new ArrayList<>();
        names = new ArrayList<>();
//        chat.add(new Message("Test", "Master"));
//        chat.add(new Message("Test2", "Master"));
//        chat.add(new Message("Test3", "Master"));
        ServerSocket serverSocket = new ServerSocket(2002);
        while (true) {
            Socket socket = serverSocket.accept();
            MyThread myThread = new MyThread(socket, chat);
            myThread.start();
        }
    }

    static class MyThread extends Thread {
        Socket socket;
        List<Message> chat;

        public MyThread(Socket socket, List<Message> chat) {
            this.socket = socket;
            this.chat = chat;
        }

        private boolean shouldShowLeaveMessage = false;

        private void handleSocket(Socket socket) {
            String name = "";
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                objectOutputStream.writeObject(Server.chat);
                this.chat = new ArrayList<>();
                name = (String) objectInputStream.readObject();
                for (String toCompare : names) {
                    if (Objects.equals(toCompare, name)) {
                        socket.close();
                        return;
                    }
                }
                names.add(name);
                Server.chat.add(new Message(name + " joined", "James"));
                shouldShowLeaveMessage = true;
                chat.addAll(Server.chat);
                while (true) {
                    if (socket.isClosed()) {
                        return;
                    } else {
                        if (chat.size() != Server.chat.size()) {
                            final int newMsgIndex = chat.size();
                            objectOutputStream.writeObject(Server.chat.get(newMsgIndex));
                            chat.add(Server.chat.get(newMsgIndex));
                            System.out.println("Sent for " + System.identityHashCode(this));
                        }
                        if (inputStream.available() > 0) {
                            Message message = (Message) objectInputStream.readObject();
                            Server.chat.add(message);
                            chat.add(message);
                            System.out.println("new Message of " + message.getUserName());
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (shouldShowLeaveMessage) {
                    Server.chat.add(new Message(name + " left", "James"));
                    System.out.println(name + " disconnected");
                }
                names.remove(name);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            System.out.println("User connected : " + socket.getLocalPort());
            handleSocket(socket);
        }
    }
}
