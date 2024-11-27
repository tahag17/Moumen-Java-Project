package job;

import database.PostgreSQLConnection;

import java.sql.*;
import java.util.List;

public class JobRepository {
    private final PostgreSQLConnection dbConnection;

    public JobRepository() {
        dbConnection = new PostgreSQLConnection();
    }

    public void insertJobDetails(List<JobDetails> jobDetailsList) {
        // Connect to the database using PostgreSQLConnection class
        try (Connection connection = dbConnection.connect()) {
            connection.setAutoCommit(false); // Use transaction for batch inserts

            // Prepare SQL queries
            String insertJobDetailsSQL = "INSERT INTO job_details (experience_level, niveau_etude, job_title) " +
                    "VALUES (?, ?, ?) RETURNING id";
            String insertSkillsSQL = "INSERT INTO skills (job_id, skill_name) VALUES (?, ?)";

            try (PreparedStatement jobStatement = connection.prepareStatement(insertJobDetailsSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement skillsStatement = connection.prepareStatement(insertSkillsSQL)) {

                // Insert each JobDetails record
                for (JobDetails jobDetails : jobDetailsList) {
                    // Set parameters for job_details table
                    jobStatement.setString(1, jobDetails.getExperienceLevel());
                    jobStatement.setString(2, jobDetails.getNiveauEtude());
                    jobStatement.setString(3, jobDetails.getJobTitle());

                    // Execute insert into job_details and get the generated id
                    int affectedRows = jobStatement.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = jobStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int jobId = generatedKeys.getInt(1); // Retrieve generated job_id

                                // Insert each skill associated with the job
                                for (String skill : jobDetails.getSkills()) {
                                    skillsStatement.setInt(1, jobId);   // Set the job_id as foreign key
                                    skillsStatement.setString(2, skill); // Set the skill_name
                                    skillsStatement.addBatch();          // Add to batch
                                }
                                skillsStatement.executeBatch(); // Execute all batch inserts for skills
                            }
                        }
                    }
                }

                // Commit the transaction
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(); // Rollback on error
                throw new RuntimeException("Error inserting job details", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection error", e);
        }
    }
}
