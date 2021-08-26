package ShareSimulator;

import java.io.IOException;
import java.util.*;

import static ShareSimulator.Main.loadFile;
import static ShareSimulator.Main.saveFile;

public class ShareMarket {
    private final String filePathMainSave;
    HashMap<String, List<StringInteger>> companies = null;
    HashMap<String, Double> money = null;
    private String filePathMoney = "src/ShareSimulator/SavedMoney.timon";
    private String filePathCompanies = "src/ShareSimulator/SavedOwners.timon";

    public ShareMarket() {
        filePathMainSave = "src/ShareSimulator/Saved.timon";
        try {
            this.companies = (HashMap<String, List<StringInteger>>) loadFile(filePathCompanies);
        } catch (IOException ignored) {

        }
        try {
            this.money = (HashMap<String, Double>) loadFile(filePathMoney);
        } catch (IOException ignored) {

        }
        HashMap<String, Double> mainCompanies = null;
        try {
            mainCompanies = (HashMap<String, Double>) loadFile(filePathMainSave);
            if (mainCompanies.size() != this.companies.size()) {
                Set<String> keySet = mainCompanies.keySet();
                for (String key : keySet) {
                    if (!companies.containsKey(key)) {
                        companies.put(key, new ArrayList<>());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (companies == null) {
            try {
                String[] names = mainCompanies.keySet().toArray(new String[0]);
                companies = new HashMap<>();
                for (int i = 0; i < names.length; i++) {
                    companies.put(names[i], new ArrayList<>());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (companies == null) {
                companies = new HashMap<>();
            }
        }
        if (money == null) {
            money = new HashMap<>();
        }
    }


    public static void main(String[] args) throws IOException {
        new ShareMarket().run();
    }

    private void run() throws IOException {
        printEverything();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input an Action");
        while (true) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("add")) {
                    System.out.println("Input your name");
                    String name = scanner.nextLine();
                    if (money.containsKey(name)) {
                        System.out.println("Name already exists");
                    } else {
                        money.put(name, 100d);
                    }
                    saveFile(filePathMoney, money);
                    printEverything();
                    System.out.println("Input an Action");
                } else if (input.equalsIgnoreCase("buy")) {
                    System.out.println("Input your name");
                    String name = scanner.nextLine();
                    if (money.containsKey(name)) {
                        System.out.println("Input the name of the company");
                        String company = scanner.nextLine();
                        if (companies.containsKey(company)) {
                            System.out.println("Input in digits from 0 to 100 how many percent you want to buy");
                            int shareInt = scanner.nextInt();
                            double share = (double) shareInt / 100d;
                            if (share <= 0) {
                                System.out.println("Share to small");
                            } else {
                                if (getAllInAll(companies.get(company)) / 100 + share * 100 <= 100) {
                                    double prize = getPrize(company, share);
                                    if (prize <= money.get(name)) {
                                        List<StringInteger> value = companies.get(company);
                                        boolean found = false;
                                        for (int i = 0; i < value.size(); i++) {
                                            StringInteger s = value.get(i);
                                            if (s.name.equals(name)) {
                                                s.number += shareInt;
                                                found = true;
                                                value.set(i, s);
                                            }
                                        }
                                        if (!found) {
                                            value.add(new StringInteger(name, shareInt));
                                        }
                                        companies.put(company, value);
                                        money.put(name, money.get(name) - prize);
                                    }
                                }
                            }
                        } else {
                            System.out.println("This company doesn't exist");
                        }
                    } else {
                        System.out.println("You don't exist");
                    }
                    saveFile(filePathMoney, money);
                    saveFile(filePathCompanies, companies);
                    printEverything();
                    System.out.println("Input an Action");
                } else if (input.equalsIgnoreCase("sell")) {
                    System.out.println("Input your name");
                    String name = scanner.nextLine();
                    if (money.containsKey(name)) {
                        System.out.println("Input the company you want to sell shares from");
                        String company = scanner.nextLine();
                        if (companies.containsKey(company)) {
                            System.out.println("Input how many percent you want to sell");
                            int count = scanner.nextInt();
                            double percentage = (double) count / 100;
                            List<StringInteger> names = companies.get(company);
                            StringInteger possession = null;
                            for (int i = 0; i < names.size(); i++) {
                                StringInteger o = names.get(i);
                                if (o.name.equalsIgnoreCase(name)) {
                                    possession = o;
                                    break;
                                }
                            }
                            if (possession == null) {
                                System.out.println("You don't own anything in this company");
                            } else {
                                int v = possession.number - count;
                                if (v >= 0) {
                                    names.remove(possession);
                                    if (v != 0) {
                                        names.add(new StringInteger(possession.name, v));
                                    }
                                    companies.put(company, names);
                                    money.put(name, money.get(name) + getPrize(company, percentage));
                                } else {
                                    System.out.println("You just own " + possession.number * 100 + " and not " + percentage * 100);
                                }
                            }
                        } else {
                            System.out.println("The company doesn't exist");
                        }
                    } else {
                        System.out.println("You don't exist");
                    }
                    saveFile(filePathMoney, money);
                    saveFile(filePathCompanies, companies);
                    printEverything();
                    System.out.println("Input an Action");
                }
            }
        }
    }

    private double getPrize(String company, double share) throws IOException {
        HashMap<String, Double> prices = (HashMap<String, Double>) loadFile(filePathMainSave);
        if (!prices.containsKey(company)) {
            return -1;
        }
        return prices.get(company) * 100 * share;
    }

    private void printEverything() throws IOException {
        String[] names = companies.keySet().toArray(new String[0]);
        for (String name : names) {
            List<StringInteger> owners = companies.get(name);
            System.out.println(name + ":");
            double allInAll = 0;
            for (StringInteger owner : owners) {
                int v = owner.number;
                allInAll += v;
                System.out.println("    " + owner.name + " : " + v + " | worth : " + getPrize(name, v / 100d));
            }
            System.out.println("All in All: " + allInAll);
            System.out.println("---------------");
        }
        System.out.println("------------------------");
        names = money.keySet().toArray(new String[0]);
        for (String name : names) {
            Double currentMoney = money.get(name);
            System.out.println(name + ":" + currentMoney);
        }
        System.out.println("------------------------");
    }

    private double getAllInAll(List<StringInteger> owners) {
        double allInAll = 0;
        for (StringInteger owner : owners) {
            double v = owner.number * 100;
            allInAll += v;
        }
        return allInAll;
    }
}
