package job;

import org.junit.jupiter.api.*;
import database.PostgreSQLConnection;
import job.JobDetails;
import job.JobRepository;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JobRepositoryTest {

    private static Connection connection;
    private JobRepository jobRepository;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Use an in-memory H2 database for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

        // Create the job table for testing
        try (Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE Job (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "titre VARCHAR(255))";
            statement.execute(createTableSQL);
        }
    }

    @BeforeEach
    public void setUp() {
        jobRepository = new JobRepository(new PostgreSQLConnection());
    }

//    @Test
//    public void testInsertJobDetails() throws SQLException {
//        // Prepare test data
//        JobDetails jobDetails = new JobDetails();
//        jobDetails.setTitre("Test Job");
//        List<JobDetails> jobDetailsList = List.of(jobDetails);
//
//        // Insert job details into the database
//        jobRepository.insertJobDetails(jobDetailsList);
//
//        // Verify that the job was inserted into the database
//        String query = "SELECT titre FROM Job WHERE titre = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, "Test Job");
//            ResultSet resultSet = preparedStatement.executeQuery();
//            assertTrue(resultSet.next());
//            assertEquals("Test Job", resultSet.getString("titre"));
//        }
//    }
//
//    @Test
//    public void testInsertJobDetailsWithNullAttributes() throws SQLException {
//        // Prepare test data with null value
//        JobDetails jobDetails = new JobDetails();
//        jobDetails.setTitre(null);
//        List<JobDetails> jobDetailsList = List.of(jobDetails);
//
//        // Insert job details with null title
//        jobRepository.insertJobDetails(jobDetailsList);
//
//        // Verify that a job with null title was inserted into the database
//        String query = "SELECT titre FROM Job WHERE titre IS NULL";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            ResultSet resultSet = preparedStatement.executeQuery();
//            assertTrue(resultSet.next());
//            assertNull(resultSet.getString("titre"));
//        }
//    }

    @Test
    public void testRollbackOnException() {
        // Start transaction
        try {
            connection.setAutoCommit(false);

            // Prepare test data
            JobDetails jobDetails = new JobDetails();
            jobDetails.setTitre("Test Job");

            // Simulate an exception during insertion (e.g., invalid SQL)
            jobRepository.insertJobDetailsWithException(List.of(jobDetails));

            // If an exception occurs, the rollback should happen, and no record should be inserted
            String query = "SELECT COUNT(*) FROM Job WHERE titre = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "Test Job");
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                assertEquals(0, resultSet.getInt(1));  // Ensure no job was inserted
            }

            // If an exception occurs, the transaction will be rolled back
            connection.rollback();
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback in case of failure
            } catch (SQLException rollbackEx) {
                fail("Failed to rollback transaction");
            }
        }
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        // Clean up after each test
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Job");
        }
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        // Close the database connection
        if (connection != null) {
            connection.close();
        }
    }
}
