import machine_learning.decisionTreePrediction;
import machine_learning.predictionNaiveBayesEducationLevel;
import weka.core.Instances;

public class MLMain {
    public static void main(String[] args) throws Exception {
//        System.out.println("Study level prediction: ");
//        predictionNaiveBayesEducationLevel p = new predictionNaiveBayesEducationLevel();
//        p.prediction();
        System.out.println("Experience level prediction: ");
        decisionTreePrediction p1 = new decisionTreePrediction();
        p1.predict();

    }
}
