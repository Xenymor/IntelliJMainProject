package SnakeKI;

public class FixedNode extends Node {
    double value = 1;

    public FixedNode() {
        super(0);
    }

    @Override
    public double generateOutput(double[] inputs) throws Exception {
        return value;
    }
}
