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
                try (Connection connection = dbConnection.connect()) {
            connection.setAutoCommit(false); 
                        String insertJobDetailsSQL = "INSERT INTO job_details (experience_level, niveau_etude, job_title) " +
                    "VALUES (?, ?, ?) RETURNING id";
            String insertSkillsSQL = "INSERT INTO skills (job_id, skill_name) VALUES (?, ?)";

            try (PreparedStatement jobStatement = connection.prepareStatement(insertJobDetailsSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement skillsStatement = connection.prepareStatement(insertSkillsSQL)) {

                                for (JobDetails jobDetails : jobDetailsList) {
                                        jobStatement.setString(1, jobDetails.getExperienceLevel());
                    jobStatement.setString(2, jobDetails.getNiveauEtude());
                    jobStatement.setString(3, jobDetails.getJobTitle());

                                        int affectedRows = jobStatement.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = jobStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int jobId = generatedKeys.getInt(1); 
                                                                for (String skill : jobDetails.getSkills()) {
                                    skillsStatement.setInt(1, jobId);                                       skillsStatement.setString(2, skill);                                     skillsStatement.addBatch();                                          }
                                skillsStatement.executeBatch();                             }
                        }
                    }
                }

                                connection.commit();
            } catch (SQLException e) {
                connection.rollback();                 throw new RuntimeException("Error inserting job details", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection error", e);
        }
    }
}
