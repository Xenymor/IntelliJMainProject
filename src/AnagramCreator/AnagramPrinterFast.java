package AnagramCreator;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class AnagramPrinterFast {
    public static void main(String[] args) throws IOException {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the letters you want the german/english anagrams of");
            String input = scanner.nextLine();
            input = input.toLowerCase();
            char[] chars = input.toCharArray();
            Arrays.sort(chars);
            String sortedInput = new String(chars);
            HashSet<String> results = new HashSet<>();
            try {
                FileInputStream inputStream = new FileInputStream(new File("out\\production\\Timon\\AnagramCreator\\Dictionary.txt"));
                Scanner sc = new Scanner(inputStream, "UTF-8");
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String lowerLine = line.toLowerCase();
                    char[] lineChars = lowerLine.toCharArray();
                    Arrays.sort(lineChars);
                    String sortedLine = new String(lineChars);
                    if (sortedLine.equalsIgnoreCase(sortedInput)) {
                        results.add(line);
                    }
                }
            } catch (FileNotFoundException ignored) {
                System.out.println("File wasn't found");
                System.exit(-2);
            }
            if (!results.isEmpty()) {
                System.out.println(Arrays.toString(results.toArray()));
            } else {
                System.out.println("There were no anagrams found and the word is unknown.");
                System.out.println("Do you want to add it? (Y/N)");
                String saveYN = scanner.nextLine();
                if (saveYN.equalsIgnoreCase("Y") || saveYN.equalsIgnoreCase("Yes")) {
                    Scanner sc = new Scanner(new File("out\\production\\Timon\\AnagramCreator\\Dictionary.txt"));
                    StringBuilder file = new StringBuilder();
                    while (sc.hasNextLine()) {
                        file.append(sc.nextLine()).append("\n");
                    }
                    String stringFile = file.toString();
                    BufferedWriter writer = new BufferedWriter(new FileWriter("out\\production\\Timon\\AnagramCreator\\Dictionary.txt"));
                    writer.write(stringFile + input);
                    writer.close();
                    System.out.println("The word was added");
                } else {
                    System.out.println("The word wasn't added");
                }
            }
        }
    }
}
