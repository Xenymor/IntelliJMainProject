package ShareSimulator;

import java.io.Serializable;

public class StringInteger implements Comparable<StringInteger>, Serializable {
    int number;
    String name;

    public StringInteger(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public int compareTo(StringInteger o) {
        return Integer.compare(number, o.number);
    }
}
