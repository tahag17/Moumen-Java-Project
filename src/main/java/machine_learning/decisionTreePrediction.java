package machine_learning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

import static config.Config.TRAINING_DATA;

public class decisionTreePrediction {
    private Classifier tree;

    public Classifier trainTree() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(TRAINING_DATA));
            ArffReader arff = new ArffReader(reader);
            Instances dataTrain = arff.getData();

            dataTrain.setClassIndex(dataTrain.numAttributes() - 2);

            weka.classifiers.trees.REPTree regressionTree = new weka.classifiers.trees.REPTree();
            regressionTree.buildClassifier(dataTrain);

            Evaluation eval = new Evaluation(dataTrain);
            eval.crossValidateModel(regressionTree, dataTrain, 10, new Random(1));
            JOptionPane.showMessageDialog(null, eval.toSummaryString(), "Model Evaluation", JOptionPane.INFORMATION_MESSAGE);

            this.tree = regressionTree;
            return regressionTree;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error during training: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public void testTree(Classifier trainedTree, String function, String studyLevel, String skills) {
        try {
            if (trainedTree == null) {
                JOptionPane.showMessageDialog(null, "Model is not trained.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            BufferedReader reader = new BufferedReader(new FileReader(TRAINING_DATA));
            ArffReader arff = new ArffReader(reader);
            Instances dataTrain = arff.getData();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 2);

            Attribute functionAttribute = dataTrain.attribute(0);
            Attribute studyLevelAttribute = dataTrain.attribute(1);
            Attribute skillsAttribute = dataTrain.attribute(3);

            Instance newInstance = new DenseInstance(dataTrain.numAttributes());
            newInstance.setDataset(dataTrain);
            newInstance.setValue(functionAttribute, function);
            newInstance.setValue(studyLevelAttribute, studyLevel);
            newInstance.setValue(skillsAttribute, skills);

            double prediction = trainedTree.classifyInstance(newInstance);
            JOptionPane.showMessageDialog(null, "Predicted Experience Level: " + (int) prediction, "Prediction", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error during testing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public static void ModelGui(){
        decisionTreePrediction prediction = new decisionTreePrediction();
        JFrame frame = new JFrame("Decision Tree Prediction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel functionLabel = new JLabel("Function:");
        JTextField functionField = new JTextField();
        JLabel studyLevelLabel = new JLabel("Study Level:");
        JTextField studyLevelField = new JTextField();
        JLabel skillsLabel = new JLabel("Skills:");
        JTextField skillsField = new JTextField();

        inputPanel.add(functionLabel);
        inputPanel.add(functionField);
        inputPanel.add(studyLevelLabel);
        inputPanel.add(studyLevelField);
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
                prediction.tree = prediction.trainTree();
            }
        });

        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String function = functionField.getText();
                String studyLevel = studyLevelField.getText();
                String skills = skillsField.getText();

                prediction.testTree(prediction.tree, function, studyLevel, skills);
            }
        });

        frame.setVisible(true);
    }


}
