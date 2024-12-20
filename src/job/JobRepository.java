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
            connection.setAutoCommit(false); // Start transaction

            String insertJobSQL = "INSERT INTO jobs (titre, url, site_name, date_de_publication, date_pour_postuler, " +
                    "adresse_d_entreprise, site_web_d_entreprise, nom_d_entreprise, description_d_entreprise, " +
                    "description_du_poste, region, ville, metier, type_du_contrat, niveau_d_etudes, specialite_diplome, " +
                    "experience, profil_recherche, traits_de_personnalite, competences_recommandees, langue, " +
                    "niveau_de_la_langue, salaire, avantages_sociaux, teletravail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

            String insertSecteursSQL = "INSERT INTO secteurs_d_activite (secteur_d_activite, job_id) VALUES (?, ?)";
            String insertHardSkillsSQL = "INSERT INTO hard_skills (hard_skills, job_id) VALUES (?, ?)";
            String insertSoftSkillsSQL = "INSERT INTO soft_skills (soft_skills, job_id) VALUES (?, ?)";

            try (PreparedStatement jobStatement = connection.prepareStatement(insertJobSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement secteursStatement = connection.prepareStatement(insertSecteursSQL);
                 PreparedStatement hardSkillsStatement = connection.prepareStatement(insertHardSkillsSQL);
                 PreparedStatement softSkillsStatement = connection.prepareStatement(insertSoftSkillsSQL)) {

                for (JobDetails jobDetails : jobDetailsList) {
                    // Setting Job Details
                    setJobStatementParams(jobStatement, jobDetails);

                    int affectedRows = jobStatement.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = jobStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int jobId = generatedKeys.getInt(1); // Retrieve the generated job ID

                                // Inserting sectors, hard skills, and soft skills
                                insertJobAttributes(jobDetails.getSecteurActivites(), jobId, secteursStatement);
                                insertJobAttributes(jobDetails.getHardSkills(), jobId, hardSkillsStatement);
                                insertJobAttributes(jobDetails.getSoftSkills(), jobId, softSkillsStatement);

                            }
                        }
                    }
                }

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback on error
                throw new RuntimeException("Error inserting job details", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection error", e);
        }
    }

    // Utility method to set the jobStatement parameters
    private void setJobStatementParams(PreparedStatement statement, JobDetails jobDetails) throws SQLException {
        statement.setString(1, (jobDetails.getTitre() == null || jobDetails.getTitre().isEmpty()) ? null : jobDetails.getTitre());
        statement.setString(2, (jobDetails.getUrl() == null || jobDetails.getUrl().isEmpty()) ? null : jobDetails.getUrl());
        statement.setString(3, (jobDetails.getSiteName() == null || jobDetails.getSiteName().isEmpty()) ? null : jobDetails.getSiteName());
        statement.setString(4, (jobDetails.getDatePublication() == null || jobDetails.getDatePublication().isEmpty()) ? null : jobDetails.getDatePublication());
        statement.setString(5, (jobDetails.getDatePourPostuler() == null || jobDetails.getDatePourPostuler().isEmpty()) ? null : jobDetails.getDatePourPostuler());
        statement.setString(6, (jobDetails.getAdresseEntreprise() == null || jobDetails.getAdresseEntreprise().isEmpty()) ? null : jobDetails.getAdresseEntreprise());
        statement.setString(7, (jobDetails.getSiteEntreprise() == null || jobDetails.getSiteEntreprise().isEmpty()) ? null : jobDetails.getSiteEntreprise());
        statement.setString(8, (jobDetails.getNomEntreprise() == null || jobDetails.getNomEntreprise().isEmpty()) ? null : jobDetails.getNomEntreprise());
        statement.setString(9, (jobDetails.getDescriptionEntreprise() == null || jobDetails.getDescriptionEntreprise().isEmpty()) ? null : jobDetails.getDescriptionEntreprise());
        statement.setString(10, (jobDetails.getDescriptionPoste() == null || jobDetails.getDescriptionPoste().isEmpty()) ? null : jobDetails.getDescriptionPoste());
        statement.setString(11, (jobDetails.getRegion() == null || jobDetails.getRegion().isEmpty()) ? null : jobDetails.getRegion());
        statement.setString(12, (jobDetails.getVille() == null || jobDetails.getVille().isEmpty()) ? null : jobDetails.getVille());
        statement.setString(13, (jobDetails.getMetier() == null || jobDetails.getMetier().isEmpty()) ? null : jobDetails.getMetier());
        statement.setString(14, (jobDetails.getTypeContrat() == null || jobDetails.getTypeContrat().isEmpty()) ? null : jobDetails.getTypeContrat());
        statement.setString(15, (jobDetails.getNiveauEtude() == null || jobDetails.getNiveauEtude().isEmpty()) ? null : jobDetails.getNiveauEtude());
        statement.setString(16, (jobDetails.getDiplomat() == null || jobDetails.getDiplomat().isEmpty()) ? null : jobDetails.getDiplomat());
        statement.setString(17, (jobDetails.getExperience() == null || jobDetails.getExperience().isEmpty()) ? null : jobDetails.getExperience());
        statement.setString(18, (jobDetails.getProfil() == null || jobDetails.getProfil().isEmpty()) ? null : jobDetails.getProfil());
        statement.setString(19, (jobDetails.getTraitsDePersonnalite() == null || jobDetails.getTraitsDePersonnalite().isEmpty()) ? null : jobDetails.getTraitsDePersonnalite());
        statement.setString(20, (jobDetails.getCompetencesRecommandes() == null || jobDetails.getCompetencesRecommandes().isEmpty()) ? null : jobDetails.getCompetencesRecommandes());
        statement.setString(21, (jobDetails.getLangue() == null || jobDetails.getLangue().isEmpty()) ? null : jobDetails.getLangue());
        statement.setString(22, (jobDetails.getNiveauLangue() == null || jobDetails.getNiveauLangue().isEmpty()) ? null : jobDetails.getNiveauLangue());
        statement.setString(23, (jobDetails.getSalaire() == null || jobDetails.getSalaire().isEmpty()) ? null : jobDetails.getSalaire());
        statement.setString(24, (jobDetails.getAvantagesSociaux() == null || jobDetails.getAvantagesSociaux().isEmpty()) ? null : jobDetails.getAvantagesSociaux());
        statement.setString(25, (jobDetails.getTeletravail() == null || jobDetails.getTeletravail().isEmpty()) ? null : jobDetails.getTeletravail());
    }

    // Utility method to handle batch insertions for sectors, hard skills, and soft skills
    private void insertJobAttributes(List<String> attributes, int jobId, PreparedStatement statement) throws SQLException {
        if (attributes != null) {
            for (String attribute : attributes) {
                statement.setString(1, (attribute == null || attribute.isEmpty()) ? null : attribute);
                statement.setInt(2, jobId);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}
