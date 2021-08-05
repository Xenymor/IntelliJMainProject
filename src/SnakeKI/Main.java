package SnakeKI;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 2, 1, 1, 0.1);
        double[][] first = neuralNetwork.generateOutputs(new double[]{1});
        for (int i = 0; i < 1000; i++) {
            neuralNetwork.trainLayers(new double[]{1}, new double[]{0});
        }
        System.out.println(Arrays.toString(first[first.length - 1]));
    }
}