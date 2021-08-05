package SnakeKI;

import java.util.Arrays;

public class NeuralNetwork {
    Node[][] nodes;
    int outputs;
    int inputs;
    int nodesPerLayer;
    double learningRate;

    public NeuralNetwork(int hiddenLayers, int nodesPerLayer, int inputs, int outputs, double learningRate) {
        this.learningRate = learningRate;
        this.outputs = outputs;
        this.inputs = inputs;
        this.nodesPerLayer = nodesPerLayer;
        nodes = new Node[hiddenLayers + 1][nodesPerLayer];
        nodes[hiddenLayers] = new Node[outputs];
        for (int i = 0; i < hiddenLayers; i++) {
            for (int j = 0; j < nodesPerLayer; j++) {
                if (j == 0) {
                    nodes[i][j] = new FixedNode();
                } else if (i == 0) {
                    nodes[i][j] = new Node(inputs);
                } else {
                    nodes[i][j] = new Node(nodesPerLayer);
                }
            }
        }
        for (int i = 0; i < outputs; i++) {
            nodes[hiddenLayers][i] = new Node(nodesPerLayer);
        }
    }

    public double[] generateOutputsForLayer(double[] inputs, int layerIndex) throws Exception {
        double[] outputs = new double[nodes[layerIndex].length];
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = nodes[layerIndex][i].generateOutput(inputs);
        }
        return outputs;
    }

    public double[][] generateOutputs(double[] inputs) throws Exception {
        double[][] actualInputs = new double[this.nodes.length + 1][inputs.length];
        actualInputs[0] = inputs;
        for (int i = 0; i < nodes.length; i++) {
            actualInputs[i + 1] = generateOutputsForLayer(actualInputs[i], i);
        }
        return actualInputs;
    }

    public void trainLayers(double[] inputs, double[] targets) throws Exception {
        double[][] outputs = generateOutputs(inputs);
        System.out.println(Arrays.toString(outputs[outputs.length - 1]));
        double[] outputErrors = new double[this.outputs];
        Weight[][] weights = new Weight[nodes[nodes.length - 1].length][this.nodesPerLayer];
        Node[] node1 = nodes[nodes.length - 1];
        for (int i = 0; i < node1.length; i++) {
            Node node = node1[i];
            weights[i] = node.in;
        }
        //Calculate outputErrors
        for (int i = 0; i < outputs[outputs.length - 1].length; i++) {
            double target = targets[i];
            double guess = outputs[outputs.length - 1][i];
            outputErrors[i] = target - guess;
        }
        //System.out.println(Arrays.toString(outputErrors));
        //Calculate OutputGradients
        double[] outputGradients = new double[outputs[outputs.length - 1].length];
        for (int i = 0; i < outputGradients.length; i++) {
            double number = outputGradients[i];
            outputGradients[i] = number * (1 - number);
        }

        for (int i = 0; i < outputGradients.length; i++) {
            double number = outputGradients[i];
            outputGradients[i] = number * outputErrors[i];
        }

        for (int i = 0; i < outputGradients.length; i++) {
            double number = outputGradients[i];
            outputGradients[i] = number * learningRate;
        }

        //Calculate deltas
        double[] hidden = outputs[outputs.length - 2];
        double[] deltas = new double[hidden.length];
        for (int j = 0; j < outputGradients.length; j++) {
            for (int i = 0; i < deltas.length; i++) {
                deltas[i] = outputGradients[j] * hidden[i];
            }
        }

        //Add deltas
        for (int i = 0; i < weights[weights.length - 1].length; i++) {
            weights[weights.length - 1][i].addToWeight(deltas[i]);
        }


        for (int k = 1; k < this.nodes.length-1; k++) {
            //Calculate hiddenErrors
            double[][] hiddenErrors = new double[this.outputs][nodes[nodes.length - k].length];
            for (int h = 0; h < this.outputs; h++) {
                for (int i = 0; i < weights.length; i++) {
                    Weight[] nodeWeights = weights[i];
                    double sum = 0;
                    for (int j = 0; j < nodeWeights.length; j++) {
                        Weight weight = nodeWeights[i];
                        sum += weight.getWeight() * outputErrors[h];
                    }
                    hiddenErrors[h][i] = sum;
                }
            }
            //Print.println2DArray(hiddenErrors);
            //calculate gradients
            double[] hiddenGradients = new double[outputs[outputs.length - 1].length];
            for (int i = 0; i < hiddenGradients.length; i++) {
                double number = hiddenGradients[i];
                hiddenGradients[i] = number * (1 - number);
            }

            for (int i = 0; i < hiddenGradients.length; i++) {
                double number = hiddenGradients[i];
                hiddenGradients[i] = number * outputErrors[i];
            }

            for (int i = 0; i < hiddenGradients.length; i++) {
                double number = hiddenGradients[i];
                hiddenGradients[i] = number * learningRate;
            }
            //Calculate deltas
            hidden = outputs[outputs.length - k-1];
            deltas = new double[hidden.length];
            for (int j = 0; j < hiddenGradients.length; j++) {
                for (int i = 0; i < deltas.length; i++) {
                    deltas[i] = hiddenGradients[j] * hidden[i];
                }
            }
            //Add deltas
            for (int i = 0; i < weights[weights.length - k].length; i++) {
                weights[weights.length - k][i].addToWeight(deltas[i]);
            }
        }

//        System.out.println("Targets:");
//        System.out.println(Arrays.toString(targets));
//        System.out.println("Outputs:");
//        System.out.println(Arrays.toString(outputs));
//        System.out.println("OutputErrors:");
//        System.out.println(Arrays.toString(outputErrors));
    }
}