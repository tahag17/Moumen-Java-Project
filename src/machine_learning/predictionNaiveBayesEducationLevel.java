package machine_learning;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

import static config.Config.TESTING_DATA_EDUCATIONAL_LEVEL;
import static config.Config.TRAINING_DATA;
public class predictionNaiveBayesEducationLevel {
    public void prediction() throws Exception {
        ConverterUtils.DataSource source1 = new ConverterUtils.DataSource(TRAINING_DATA);
        Instances train = source1.getDataSet();

        // Set the class attribute for training data
        if (train.classIndex() == -1) {
            train.setClassIndex(1);
        }

        // Convert string class attribute to nominal (if it's a string)
        StringToNominal stringToNominal = new StringToNominal();
        stringToNominal.setOptions(new String[] {"-R", String.valueOf(train.classIndex() + 1)});
        stringToNominal.setInputFormat(train);
        train = Filter.useFilter(train, stringToNominal);

        // Load testing data
        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource(TESTING_DATA_EDUCATIONAL_LEVEL);
        Instances test = source2.getDataSet();

        // Set the class attribute for testing data
        if (test.classIndex() == -1) {
            test.setClassIndex(1);
        }

        // Apply the same filter to the test data
        test = Filter.useFilter(test, stringToNominal);

        // Create and train the Naive Bayes classifier
        NaiveBayes naiveBayes = new NaiveBayes();
        naiveBayes.buildClassifier(train);

        // Perform cross-validation on the training data
        Evaluation eval_rocTrain = new Evaluation(train);
        eval_rocTrain.crossValidateModel(naiveBayes, train, 10, new Random(1));

        // Perform cross-validation on the testing data
        Evaluation eval_rocTest = new Evaluation(test);
        eval_rocTest.crossValidateModel(naiveBayes, test, 10, new Random(1));

        // Print the predicted class for each instance in the training set
        System.out.println("______________La prédiction pour l'ensemble d'entraînement est : ______________");

        for (int i = 0; i < train.numInstances(); i++) {
            Instance instance = train.instance(i);
            double prediction = naiveBayes.classifyInstance(instance);
            String predictedClass = train.classAttribute().value((int) prediction);

            // Print the instance index and its predicted class
            System.out.printf("Instance %d: Predicted class = %s%n", i, predictedClass);

            // Print the values of each attribute for the instance
            // Print the values of each attribute for the instance
            System.out.print("Attribute values: ");
            for (int j = 0; j < instance.numAttributes(); j++) {
                String attributeValue = "";

                // Check the type of the attribute
                if (instance.attribute(j).isNumeric()) {
                    // If the attribute is numeric, get the numeric value
                    attributeValue = String.valueOf(instance.value(j));
                } else if (instance.attribute(j).isNominal() || instance.attribute(j).isString() || instance.attribute(j).isDate()) {
                    // If the attribute is nominal, string, or date, get the string value
                    attributeValue = instance.stringValue(j);
                } else {
                    // For unsupported types, print a placeholder or handle accordingly
                    attributeValue = "Unsupported attribute type";
                }

                System.out.printf("%s = %s; ", instance.attribute(j).name(), attributeValue);
            }
            System.out.println();  // Move to the next line after printing all attributes
            // Move to the next line after printing all attributes
        }
    }

}
