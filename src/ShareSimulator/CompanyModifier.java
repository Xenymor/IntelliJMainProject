package ShareSimulator;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class CompanyModifier {

    private static final String filePath = "src/ShareSimulator/Saved.timon";

    public static void main(String[] args) throws WrongException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("add")) {
                    System.out.println("Input name to add");
                    if (scanner.hasNextLine()) {
                        input = scanner.nextLine();
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
                            HashMap<String, Double> companies = (HashMap<String, Double>) objectInputStream.readObject();
                            companies.put(input, 1d);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
                            objectOutputStream.writeObject(companies);
                            objectOutputStream.close();
                        } catch (ClassNotFoundException e) {
                            System.out.println("Invalid File");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Finished");
                    }
                } else if (input.equalsIgnoreCase("remove")) {
                    System.out.println("Input name to remove");
                    if (scanner.hasNextLine()) {
                        input = scanner.nextLine();
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
                            HashMap<String, Double> companies = (HashMap<String, Double>) objectInputStream.readObject();
                            companies.remove(input);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
                            objectOutputStream.writeObject(companies);
                            objectOutputStream.close();
                        } catch (ClassNotFoundException e) {
                            System.out.println("Invalid File");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Finished");
                    }
                }
            }
        }
    }

    private static class WrongException extends Throwable {
    }
}
