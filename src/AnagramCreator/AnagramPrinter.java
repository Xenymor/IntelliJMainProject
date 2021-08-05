package AnagramCreator;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class AnagramPrinter {
    private static final HashSet<String> words = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("This program creates all german anagrams of the letters, with the length you give.");
        System.out.println("There are double characters.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("\033[0;32mEnter the letters\033[0m (e.g.:erto)");
        String inputChars = scanner.nextLine();
        String noDuplicates = Arrays.stream(inputChars.split(""))
                .distinct()
                .collect(Collectors.joining());
        char[] chars = noDuplicates.toCharArray();
        System.out.println("\033[0;32mEnter the length of the outcome\033[0m (e.g.:3)");
        String input = scanner.nextLine();
        int length = 3;
        while (true) {
            try {
                length = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                System.out.println("\033[0;31mThat is no legal number\033[0m");
            }
        }
        iterate(chars, length, new char[length], 0);
        System.out.println("\033[0;32mGenerated all possible combinations\033[0m");
        System.out.println("\033[0;32mReading dictionary and comparing words\033[0m");
        List<String> realWords = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(new File("out\\production\\Timon\\AnagramCreator\\Dictionary.txt"));
            Scanner sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (words.contains(line.toLowerCase())) {
                    realWords.add(line);
                }
            }
        } catch (Exception ignored) {

        }
        System.out.println("\033[0;32mFound all words\033[0m");
        System.out.println("\033[0;32mRemoving doubled words\033[0m");
        realWords = new ArrayList<>(new HashSet<>(realWords));
        System.out.println(Arrays.toString(realWords.toArray()));
    }

    public static void iterate(char[] chars, int length, char[] build, int pos) {
        if (pos == length) {
            String word = new String(build);
            words.add(word.toLowerCase());
            return;
        }

        for (int i = 0; i < chars.length; i++) {
            build[pos] = chars[i];
            iterate(chars, length, build, pos + 1);
        }
    }

}