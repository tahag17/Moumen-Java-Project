import machine_learning.decisionTreePrediction;
import machine_learning.predictionNaiveBayesEducationLevel;
public class MLMain {
    public static void main(String[] args) throws Exception {
//        predictionNaiveBayesEducationLevel p = new predictionNaiveBayesEducationLevel();
//        p.prediction();
        decisionTreePrediction pp = new decisionTreePrediction();
        pp.predict();
    }
}
