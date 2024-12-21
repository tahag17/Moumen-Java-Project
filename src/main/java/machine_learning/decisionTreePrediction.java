//package machine_learning;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Random;
//import java.util.Scanner;
//
//import weka.classifiers.Classifier;
//import weka.classifiers.Evaluation;
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.Instance;
//import weka.core.Instances;
//import weka.core.converters.ArffLoader.ArffReader;
//
//import static config.Config.TRAINING_DATA;
//
//public class decisionTreePrediction {
//    private Classifier tree;
//
//    // Method for loading the instences from the dataset path's
//
//    public void predict() {
//        try {
//            // Load training data
//            System.out.println("Loading training data...");
//            BufferedReader reader = new BufferedReader(new FileReader(TRAINING_DATA)); // Replace with the actual path
//            ArffReader arff = new ArffReader(reader);
//            Instances dataTrain = arff.getData();
//
//            // Set the class index (Experience level as the target class)
//            dataTrain.setClassIndex(dataTrain.numAttributes() - 2);
//
//            // Train the REPTree model
//            System.out.println("Training the REPTree model...");
//            weka.classifiers.trees.REPTree regressionTree = new weka.classifiers.trees.REPTree();
//            regressionTree.buildClassifier(dataTrain);
//
//            // Evaluate the model using cross-validation
//            System.out.println("Evaluating the model...");
//            Evaluation eval = new Evaluation(dataTrain);
//            eval.crossValidateModel(regressionTree, dataTrain, 10, new Random(1));
//            System.out.println(eval.toSummaryString());
//
//            // Get user input for testing
//            System.out.println("Testing the model with user input...");
//            Scanner scanner = new Scanner(System.in);
//
//            System.out.print("Enter function: ");
//            String function = scanner.nextLine();
//
//            System.out.print("Enter study level: ");
//            String studyLevel = scanner.nextLine();
//
//            System.out.print("Enter skills: ");
//            String skills = scanner.nextLine();
//
//            // Create a new instance with user input
//            Attribute functionAttribute = dataTrain.attribute(0);
//            Attribute studyLevelAttribute = dataTrain.attribute(1);
//            Attribuate skillsAttribute = dataTrain.attribute(3);
//
//            Instance newInstance = new DenseInstance(dataTrain.numAttributes());
//            newInstance.setDataset(dataTrain);
//            newInstance.setValue(functionAttribute, function);
//            newInstance.setValue(studyLevelAttribute, studyLevel);
//            newInstance.setValue(skillsAttribute, skills);
//
//            // Predict the experience level
//            double prediction = regressionTree.classifyInstance(newInstance);
//            System.out.println("Predicted Experience Level: " + (int) prediction);
//
//        } catch (Exception e) {
//            System.out.println("An error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//    public static void main(String[] args){
//        decisionTreePrediction p = new decisionTreePrediction();
//        p.predict();
//    }
//
//}