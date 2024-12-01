package machine_learning;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

import static config.Config.TESTING_DATA_EXPERIENCE_LEVEL;
import static config.Config.TRAINING_DATA;

public class decisionTreePrediction {
    private Classifier tree;

    // Method for loading the instences from the dataset path's

    public void predict() {
        try {
            // Load training data
            BufferedReader reader = new BufferedReader(new FileReader(TRAINING_DATA));
            ArffReader arff = new ArffReader(reader);
            Instances dataTrain = arff.getData();

            // Load testing data
            BufferedReader reader1 = new BufferedReader(new FileReader(TESTING_DATA_EXPERIENCE_LEVEL));
            ArffReader arff1 = new ArffReader(reader1);
            Instances dataTest = arff1.getData();

            // Set the target class (Experience Level) for both datasets
            dataTrain.setClassIndex(dataTrain.numAttributes() - 2);
            dataTest.setClassIndex(dataTrain.numAttributes() - 2);

            // Create and train the REPTree model
            weka.classifiers.trees.REPTree regressionTree = new weka.classifiers.trees.REPTree();
            regressionTree.buildClassifier(dataTrain);

            // Evaluate the model
            System.out.println("Evaluation with Train Data:");
            Evaluation eval = new Evaluation(dataTrain);
            eval.crossValidateModel(regressionTree, dataTrain, 10, new Random(1));
            System.out.println(eval.toSummaryString());

            // Make predictions on the test dataset
            System.out.println("Predictions:");
            for (int i = 0; i < dataTest.numInstances(); i++) {
                Instance instance = dataTest.instance(i);
                double predictionV = regressionTree.classifyInstance(instance);
                System.out.println("Function: " + instance.stringValue(0) +
                        ", Study Level: " + instance.stringValue(1) +
                        ", Experience Level (prediction): " + (int) predictionV +
                        ", Skills: " + instance.stringValue(3));
            }
        } catch (Exception ex) {
            System.out.println("Error during prediction: " + ex.getMessage());
        }
    }

}
