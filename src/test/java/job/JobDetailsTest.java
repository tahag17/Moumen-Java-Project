package job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JobDetailsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJobDetailsSerialization() throws IOException {
        // Create a JobDetails object
        JobDetails jobDetails = new JobDetails(
                1,
                "Software Engineer",
                "http://example.com",
                "JobSite",
                "2024-12-21",
                "2025-01-01",
                "123 Main St",
                "http://company.com",
                "TechCorp",
                "Leading tech company",
                "Develop software",
                "California",
                "Los Angeles",
                List.of("IT", "Technology"),
                "Engineering",
                "Full-Time",
                "Bachelor's Degree",
                "Computer Science",
                "3 years",
                "Problem solver",
                "Analytical",
                List.of("Java", "Spring"),
                List.of("Communication", "Teamwork"),
                "Leadership",
                "English",
                "Fluent",
                "$120,000",
                "Health insurance",
                "Optional"
        );

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(jobDetails);

        // Deserialize back to an object
        JobDetails deserializedJobDetails = objectMapper.readValue(json, JobDetails.class);

        // Assert the equality of the objects
        assertEquals(jobDetails, deserializedJobDetails);
    }

    @Test
    void testJobDetailsMapper() throws IOException {
        // Prepare test JSON file
        File jsonFile = new File("src/test/resources/jobDetails.json");

        // Mapper instance
        mapper.JobDetailsMapper mapper = new mapper.JobDetailsMapper();

        // Deserialize JSON file to List<JobDetails>
        List<JobDetails> jobDetailsList = mapper.mapJsonFileToJobDetails(jsonFile);

        // Assert the list is not null and contains the expected data
        assertNotNull(jobDetailsList);
        assertFalse(jobDetailsList.isEmpty());

        // Validate the first JobDetails object
        JobDetails firstJobDetails = jobDetailsList.get(0);
        assertEquals("Software Engineer", firstJobDetails.getTitre());
        assertEquals("http://example.com", firstJobDetails.getUrl());
        assertEquals("JobSite", firstJobDetails.getSiteName());
    }

    @Test
    void testJobDetailsDefaultConstructorAndSetters() {
        // Create an empty JobDetails object
        JobDetails jobDetails = new JobDetails();

        // Set values
        jobDetails.setId(1);
        jobDetails.setTitre("Software Engineer");
        jobDetails.setUrl("http://example.com");
        jobDetails.setSiteName("JobSite");
        jobDetails.setDatePublication("2024-12-21");
        jobDetails.setDatePourPostuler("2025-01-01");

        // Assert the values are set correctly
        assertEquals(1, jobDetails.getId());
        assertEquals("Software Engineer", jobDetails.getTitre());
        assertEquals("http://example.com", jobDetails.getUrl());
        assertEquals("JobSite", jobDetails.getSiteName());
        assertEquals("2024-12-21", jobDetails.getDatePublication());
        assertEquals("2025-01-01", jobDetails.getDatePourPostuler());
    }

    @Test
    void testJobDetailsEqualityAndHashCode() {
        // Create two identical JobDetails objects
        JobDetails jobDetails1 = new JobDetails(1, "Title", "URL", "Site", "2024-12-21", "2025-01-01", "Address", "Website", "Company", "Description", "Post Description", "Region", "City", List.of("Activity"), "Job", "Contract", "Education", "Diploma", "Experience", "Profile", "Traits", List.of("Skill1"), List.of("Skill2"), "Recommended Skills", "Language", "Fluent", "$1000", "Benefits", "Yes");
        JobDetails jobDetails2 = new JobDetails(1, "Title", "URL", "Site", "2024-12-21", "2025-01-01", "Address", "Website", "Company", "Description", "Post Description", "Region", "City", List.of("Activity"), "Job", "Contract", "Education", "Diploma", "Experience", "Profile", "Traits", List.of("Skill1"), List.of("Skill2"), "Recommended Skills", "Language", "Fluent", "$1000", "Benefits", "Yes");

        // Assert equality and hash codes
        assertEquals(jobDetails1, jobDetails2);
        assertEquals(jobDetails1.hashCode(), jobDetails2.hashCode());
    }
}
