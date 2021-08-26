package ShareSimulator;

import java.io.Serializable;

public class StringDouble implements Comparable<StringDouble>, Serializable {
    String name;
    Double number;

    public StringDouble(String name, Double number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public int compareTo(StringDouble o) {
        return number.compareTo(o.number);
    }

    @Override
    public String toString() {
        return name + " : " + number;
    }
}
