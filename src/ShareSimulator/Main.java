package ShareSimulator;

import StandardClasses.Random;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Main {
    private final File file;
    HashMap<String, Double> companies;
    String filePath;
    long lastModified;

    public Main(String filePath) throws IOException {
        this.companies = new HashMap<>();
        this.filePath = filePath;
        file = new File(filePath);
        if (file.exists()) {
            companies = (HashMap<String, Double>) loadFile(filePath);
        } else {
            file.createNewFile();
        }
        lastModified = file.lastModified();
//        String[] keys = companies.keySet().toArray(new String[0]);
//        for (int i = 0; i < keys.length; i++) {
//            companies.put(keys[i], 10d);
//        }
//        saveFile(filePath);
//        System.exit(0);
    }

    public static Object loadFile(String filePath) throws IOException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
            return objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid File");
        } catch (IOException e) {
            throw e;
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        new Main("src/ShareSimulator/Saved.timon").run();
    }

    private void run() throws IOException {
        long lastNanoTime = System.nanoTime() - TimeUnit.MINUTES.toNanos(3);
        printAllShares();
        saveFile(filePath, companies);
        while (true) {
            if (TimeUnit.NANOSECONDS.toMinutes(System.nanoTime() - lastNanoTime) >= 1) {
                if (lastModified != file.lastModified()) {
                    companies = (HashMap<String, Double>) loadFile(filePath);
                }
                lastNanoTime = System.nanoTime();
                String[] keys = new String[0];
                keys = companies.keySet().toArray(keys);
                for (String key : keys) {
                    double current = companies.get(key);
                    double random = Random.randomIntInRangeNumbersGetMoreUnlikely(-99, 99) / 100;
                    current += random * current;
                    current += 0.25;
                    current *= 100;
                    current = Math.round(current);
                    current /= 100;
                    if (current <= 0) {
                        companies.put(key, 0.1);
                    } else {
                        companies.put(key, current);
                    }
                }
                saveFile(filePath, companies);
                lastModified = file.lastModified();
                printAllShares();
            }
        }
    }

    private void printAllShares() {
        String[] keys = companies.keySet().toArray(new String[0]);
        StringDouble[] stringDoubles = new StringDouble[keys.length];
        for (int i = 0; i < keys.length; i++) {
            stringDoubles[i] = new StringDouble(keys[i], companies.get(keys[i]));
        }
        Arrays.sort(stringDoubles);
        for (StringDouble stringDouble : stringDoubles) {
            System.out.println(stringDouble.toString());
        }
        System.out.println("-----------------------");
    }

    static void saveFile(String filePath, Object toSave) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        objectOutputStream.writeObject(toSave);
        objectOutputStream.close();
    }
}
