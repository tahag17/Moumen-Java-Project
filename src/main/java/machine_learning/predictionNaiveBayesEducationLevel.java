//package machine_learning;
//
//import java.util.Scanner;
//import java.util.Random;
//import weka.classifiers.Evaluation;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.core.DenseInstance;
//import weka.core.Instance;
//import weka.core.Instances;
//import weka.core.converters.ConverterUtils;
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.StringToNominal;
//
//import static config.Config.TRAINING_DATA;
//
//public class predictionNaiveBayesEducationLevel {
//    public static void main(String[] args) throws Exception {
//        // Load training data
//        ConverterUtils.DataSource source1 = new ConverterUtils.DataSource(TRAINING_DATA);
//        Instances train = source1.getDataSet();
//
//        // Set the class attribute for training data
//        if (train.classIndex() == -1) {
//            train.setClassIndex(1);
//        }
//
//        // Convert string class attribute to nominal (if it's a string)
//        StringToNominal stringToNominal = new StringToNominal();
//        stringToNominal.setOptions(new String[]{"-R", String.valueOf(train.classIndex() + 1)});
//        stringToNominal.setInputFormat(train);
//        train = Filter.useFilter(train, stringToNominal);
//
//        // Create and train the Naive Bayes classifier
//        NaiveBayes naiveBayes = new NaiveBayes();
//        naiveBayes.buildClassifier(train);
//
//        // Perform cross-validation on the training data
//        Evaluation eval_rocTrain = new Evaluation(train);
//        eval_rocTrain.crossValidateModel(naiveBayes, train, 10, new Random(1));
//        System.out.println("=== Model Evaluation Metrics ===");
//        System.out.println("Correctly Classified Instances: " + eval_rocTrain.correct() + " (" + String.format("%.2f", eval_rocTrain.pctCorrect()) + "%)");
//        System.out.println("Incorrectly Classified Instances: " + eval_rocTrain.incorrect() + " (" + String.format("%.2f", eval_rocTrain.pctIncorrect()) + "%)");
//        System.out.println("Kappa Statistic: " + String.format("%.4f", eval_rocTrain.kappa()));
//        System.out.println("Mean Absolute Error: " + String.format("%.4f", eval_rocTrain.meanAbsoluteError()));
//        System.out.println("Root Mean Squared Error: " + String.format("%.4f", eval_rocTrain.rootMeanSquaredError()));
//        System.out.println("\nConfusion Matrix:");
//        System.out.println(eval_rocTrain.toMatrixString());
//        System.out.println("Summary: ");
//        System.out.println(eval_rocTrain.toSummaryString());
//
//        System.out.println("Model trained successfully. You can now input data for prediction.");
//
//        // Accept user input for testing
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("Enter function: ");
//        String function = scanner.nextLine();
//
//        System.out.print("Enter niveauExperience (e.g., 1,...): ");
//        double niveauExperience = scanner.nextDouble();
//        scanner.nextLine(); // Consume the leftover newline
//
//        System.out.print("Enter activity: ");
//        String activity = scanner.nextLine();
//
//        // Create a new instance with user input
//        Instance userInstance = new DenseInstance(train.numAttributes());
//        userInstance.setDataset(train);
//
//        // Set attribute values based on user input
//        userInstance.setValue(train.attribute(0), function);
//        userInstance.setValue(train.attribute(2), niveauExperience);
//        userInstance.setValue(train.attribute(3), activity);
//        userInstance.setMissing(train.classIndex()); // Class value to predict
//
//        // Classify the new instance
//        double prediction = naiveBayes.classifyInstance(userInstance);
//        String predictedClass = train.classAttribute().value((int) prediction);
//
//        System.out.println("Predicted Study level: " + predictedClass);
//
//        scanner.close();
//    }
//}
