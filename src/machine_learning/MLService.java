package machine_learning;

import machine_learning.decisionTreePrediction;
import machine_learning.predictionNaiveBayesEducationLevel;

public class MLService {

    public void runMLPredictions() throws Exception {
        // Uncomment and run the prediction if necessary
//        predictionNaiveBayesEducationLevel p = new predictionNaiveBayesEducationLevel();
//        p.prediction();

        // Running decision tree prediction
        decisionTreePrediction pp = new decisionTreePrediction();
        pp.predict();
    }
}
