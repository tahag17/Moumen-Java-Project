import config.Config;
import job.JobDetails;
import job.JobRepository;
import mapper.JobDetailsMapper;

import java.io.File;
import java.util.List;

public class TestMainMethod {
    public static void main(String[] args) {
        try {
            // Path to your JSON file
            File jsonFile = new File(Config.BASE_PATH+"emploimacleaned_data.json");

            // Instantiate the mapper
            JobDetailsMapper mapper = new JobDetailsMapper();

            // Map the JSON file to a list of JobDetails
            List<JobDetails> jobDetailsList = mapper.mapJsonFileToJobDetails(jsonFile);

            // Instantiate JobDetailsRepository and insert data
            JobRepository repository = new JobRepository();
            repository.insertJobDetails(jobDetailsList);

            System.out.println("Job details and skills inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Print any errors
        }
    }
}
