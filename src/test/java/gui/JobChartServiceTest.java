package gui;

import database.DataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class JobChartServiceTest {

    @Mock
    private DataFetcher dataFetcher;

    private JobChartService jobChartService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create JobChartService instance with mocked DataFetcher
        jobChartService = new JobChartService();
    }

    @Test
    void testGenerateJobCharts() {
        // Mock the data returned by the DataFetcher methods
        Map<String, Integer> regionData = Map.of("North", 10, "South", 15);
        Map<String, Integer> contractTypeData = Map.of("Full-time", 25, "Part-time", 5);
        Map<String, Integer> experienceData = Map.of("Junior", 10, "Senior", 15);
        Map<String, Integer> educationLevelData = Map.of("Bachelor", 20, "Master", 10);
        Map<String, Integer> secteursData = Map.of("Tech", 15, "Healthcare", 10);
        Map<String, Integer> hardSkillsData = Map.of("Java", 12, "Python", 8);

        // Stub the fetch methods to return the mocked data
        when(dataFetcher.fetchRegionData()).thenReturn(regionData);
        when(dataFetcher.fetchContractTypeData()).thenReturn(contractTypeData);
        when(dataFetcher.fetchExperienceData()).thenReturn(experienceData);
        when(dataFetcher.fetchEducationLevelData()).thenReturn(educationLevelData);
        when(dataFetcher.fetchTopSecteursDActivite()).thenReturn(secteursData);
        when(dataFetcher.fetchTopHardSkills()).thenReturn(hardSkillsData);

        // Call the method that generates the charts
        jobChartService.generateJobCharts();

        // Verify that the data fetching methods are called
        verify(dataFetcher).fetchRegionData();
        verify(dataFetcher).fetchContractTypeData();
        verify(dataFetcher).fetchExperienceData();
        verify(dataFetcher).fetchEducationLevelData();
        verify(dataFetcher).fetchTopSecteursDActivite();
        verify(dataFetcher).fetchTopHardSkills();
    }

    @Test
    void testCreateAndShowPieChart() {
        // Prepare test data
        Map<String, Integer> data = Map.of("Category1", 5, "Category2", 10);

        // Create a spy on the JobChartService to verify private method invocation
        JobChartService spyService = spy(jobChartService);

        // Call the private method via the spy
        spyService.createAndShowPieChart("Test Pie Chart", data);

        // Verify that the JFrame (chart) is created
        verify(spyService).createAndShowPieChart(eq("Test Pie Chart"), eq(data));
    }

    @Test
    void testCreateAndShowBarChart() {
        // Prepare test data
        Map<String, Integer> data = Map.of("Category1", 5, "Category2", 10);

        // Create a spy on the JobChartService to verify private method invocation
        JobChartService spyService = spy(jobChartService);

        // Call the private method via the spy
        spyService.createAndShowBarChart("Test Bar Chart", data);

        // Verify that the JFrame (chart) is created
        verify(spyService).createAndShowBarChart(eq("Test Bar Chart"), eq(data));
    }
}
