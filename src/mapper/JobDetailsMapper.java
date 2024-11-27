package mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import job.JobDetails;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JobDetailsMapper {
    private final ObjectMapper objectMapper;

    public JobDetailsMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public List<JobDetails> mapJsonFileToJobDetails(File file) throws IOException {
        return objectMapper.readValue(file, new TypeReference<List<JobDetails>>() {});
    }
}
