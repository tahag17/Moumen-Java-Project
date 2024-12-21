package machine_learning;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;
import java.util.Random;

import static config.Config.TRAINING_DATA;

public class predictionNaiveBayesEducationLevel {

    NaiveBayes classifier;

    // Method to train the NaiveBayes model
    public NaiveBayes predict() {
        try {
            // Load the training dataset
            ConverterUtils.DataSource source1 = new ConverterUtils.DataSource(TRAINING_DATA);
            Instances train = source1.getDataSet();

            // Set the class index (you can keep this as it is without changing it)
            if (train.classIndex() == -1) {
                train.setClassIndex(1); // Keep this as per your original code
            }

            // Convert the string class attribute to nominal if necessary
            StringToNominal stringToNominal = new StringToNominal();
            stringToNominal.setOptions(new String[]{"-R", String.valueOf(train.classIndex() + 1)});
            stringToNominal.setInputFormat(train);
            train = Filter.useFilter(train, stringToNominal);

            // Train the Naive Bayes classifier
            NaiveBayes naiveBayes = new NaiveBayes();
            naiveBayes.buildClassifier(train);

            // Perform cross-validation
            Evaluation eval_rocTrain = new Evaluation(train);
            eval_rocTrain.crossValidateModel(naiveBayes, train, 10, new Random(1));
            JOptionPane.showMessageDialog(null, eval_rocTrain.toSummaryString(), "Model Evaluation", JOptionPane.INFORMATION_MESSAGE);
            classifier = naiveBayes; // Save the trained classifier
            return naiveBayes;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error during training: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    // Method to test the trained model
    public void testModel(NaiveBayes trainedModel, String function, int expLevel, String activity) throws Exception {
        if (trainedModel == null) {
            JOptionPane.showMessageDialog(null, "Model is not trained.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Load the training data again for prediction
        ConverterUtils.DataSource source1 = new ConverterUtils.DataSource(TRAINING_DATA);
        Instances train = source1.getDataSet();
        if (train.classIndex() == -1) {
            train.setClassIndex(1); // Ensure this is kept as it is in your code
        }

        // Create an instance with user input
        Instance userInstance = new DenseInstance(train.numAttributes());
        userInstance.setDataset(train);
        userInstance.setValue(train.attribute(0), function); // Function
        userInstance.setValue(train.attribute(2), expLevel); // Experience Level
        userInstance.setValue(train.attribute(3), activity); // Activity
        userInstance.setMissing(train.classIndex());// Class value to predict

        // Classify and display the prediction
        double prediction = trainedModel.classifyInstance(userInstance);
        String predictedClass = train.classAttribute().value((int) prediction); // Convert to actual class label
        JOptionPane.showMessageDialog(null, "Predicted Class: " + predictedClass, "Prediction Result", JOptionPane.INFORMATION_MESSAGE);
    }

    // GUI for training and prediction
    public static void ModelGui() {
        predictionNaiveBayesEducationLevel p = new predictionNaiveBayesEducationLevel();

        JFrame frame = new JFrame("Naive Bayes Prediction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        JLabel functionLabel = new JLabel("Function:");
        JTextField functionField = new JTextField();
        JLabel experienceLevelLabel = new JLabel("Experience Level:");
        JTextField experienceLevelField = new JTextField();
        JLabel skillsLabel = new JLabel("Skills:");
        JTextField skillsField = new JTextField();

        inputPanel.add(functionLabel);
        inputPanel.add(functionField);
        inputPanel.add(experienceLevelLabel);
        inputPanel.add(experienceLevelField);
        inputPanel.add(skillsLabel);
        inputPanel.add(skillsField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton trainButton = new JButton("Train Model");
        JButton predictButton = new JButton("Predict");

        buttonPanel.add(trainButton);
        buttonPanel.add(predictButton);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.classifier = p.predict();
            }
        });

        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String function = functionField.getText();
                int experienceLevel = Integer.parseInt(experienceLevelField.getText());
                String skills = skillsField.getText(); // Assuming skills is an integer value

                try {
                    p.testModel(p.classifier, function, experienceLevel, skills);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        frame.setVisible(true);
    }

}
