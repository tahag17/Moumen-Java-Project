//import machine_learning.decisionTreePrediction;
//import machine_learning.predictionNaiveBayesEducationLevel;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class MLGuiApplication {
//
//    public static void main(String[] args) {
//        // Create the main frame
//        JFrame frame = new JFrame("Machine Learning Prediction");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 400);
//
//        // Panel to hold all components
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        // Label for selecting functionality
//        JLabel labelSelect = new JLabel("Select Prediction Method:");
//        panel.add(labelSelect);
//
//        // Dropdown to choose the functionality
//        String[] options = {"Decision Tree", "Naive Bayes"};
//        JComboBox<String> dropdown = new JComboBox<>(options);
//        panel.add(dropdown);
//
//        // Dynamic input fields panel
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new GridLayout(5, 2));
//
//        // Add placeholders for input fields
//        JLabel labelFunction = new JLabel("Function:");
//        JTextField fieldFunction = new JTextField();
//        inputPanel.add(labelFunction);
//        inputPanel.add(fieldFunction);
//
//        JLabel labelSecondVar = new JLabel(); // Label for the second variable (dynamic)
//        JTextField fieldSecondVar = new JTextField();
//        inputPanel.add(labelSecondVar);
//        inputPanel.add(fieldSecondVar);
//
//        JLabel labelThirdVar = new JLabel("Activity/Skills:");
//        JTextField fieldThirdVar = new JTextField();
//        inputPanel.add(labelThirdVar);
//        inputPanel.add(fieldThirdVar);
//
//        panel.add(inputPanel);
//
//        // Update labels dynamically based on dropdown selection
//        dropdown.addActionListener(e -> {
//            String selected = (String) dropdown.getSelectedItem();
//            if (selected.equals("Decision Tree")) {
//                labelSecondVar.setText("Study Level:");
//                fieldSecondVar.setText(""); // Clear previous input
//            } else {
//                labelSecondVar.setText("Experience Level:");
//                fieldSecondVar.setText("");
//            }
//        });
//
//        // Button to run prediction
//        JButton runButton = new JButton("Run Prediction");
//        panel.add(runButton);
//
//        // Text area to display results
//        JTextArea resultArea = new JTextArea(5, 30);
//        resultArea.setEditable(false);
//        panel.add(new JScrollPane(resultArea));
//
//        // Add action listener to the button
//        runButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String selectedMethod = (String) dropdown.getSelectedItem();
//                String function = fieldFunction.getText();
//                String secondVar = fieldSecondVar.getText();
//                String thirdVar = fieldThirdVar.getText();
//
//                try {
//                    if (selectedMethod.equals("Decision Tree")) {
//                        decisionTreePrediction treePrediction = new decisionTreePrediction();
//                        // Call prediction method with inputs
//                        String result = treePrediction.predict(function, secondVar, thirdVar);
//                        resultArea.setText("Decision Tree Prediction: " + result);
//                    } else {
//                        predictionNaiveBayesEducationLevel naiveBayesPrediction = new predictionNaiveBayesEducationLevel();
//                        // Call prediction method with inputs
//                        String result = naiveBayesPrediction.predict(function, secondVar, thirdVar);
//                        resultArea.setText("Naive Bayes Prediction: " + result);
//                    }
//                } catch (Exception ex) {
//                    resultArea.setText("An error occurred: " + ex.getMessage());
//                }
//            }
//        });
//
//        // Add the panel to the frame
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//}
