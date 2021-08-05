package SnakeKI;

public class Weight {
    private double weight;
    public double var;

    public Weight(double weight) {
        this.weight = weight;
        this.var = 0.5;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void addToWeight(double toAdd) {
        this.weight += weight + toAdd;
    }
}
