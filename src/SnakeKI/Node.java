package SnakeKI;

import StandardClasses.MyMath;
import StandardClasses.Random;

public class Node {
    public Weight[] in;
    int nodeCountIn;

    public Node(int nodeCountIn) {
        this.nodeCountIn = nodeCountIn;
        in = new Weight[nodeCountIn];
        for (int i = 0; i < in.length; i++) {
            in[i] = new Weight(Random.randomDoubleInRange(-10, 10));
        }
    }

    public double generateOutput(double[] inputs) throws Exception {
        double result = 0;
        if (inputs.length != in.length) {
            throw new Exception("input length != in length");
        } else {
            for (int i = 0; i < inputs.length; i++) {
                result += inputs[i]*in[i].getWeight();
            }
        }
        result = MyMath.sigmoid(result);
        return result;
    }
}
