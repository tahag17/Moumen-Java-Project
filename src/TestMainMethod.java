import config.Config;
import job.JobDetails;
import mapper.JobDetailsMapper;

import java.io.File;
import java.util.List;

public class TestMainMethod {
    public static void main(String[] args) {
        System.out.println("Hello World");
        try {
            // Path to your JSON file
//            File jsonFile = new File(Config.BASE_PATH+"emploimacleaned_data.json");
            File jsonFile = new File(Config.BASE_PATH+"rekrutcleaned_data.json");


            // Instantiate the mapper
            JobDetailsMapper mapper = new JobDetailsMapper();

            // Map the JSON file to a list of JobDetails
            List<JobDetails> jobDetailsList = mapper.mapJsonFileToJobDetails(jsonFile);

            // Print out the results to verify
            jobDetailsList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace(); // Print any errors
        }


    }
}
