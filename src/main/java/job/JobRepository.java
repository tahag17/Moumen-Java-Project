package job;

import database.PostgreSQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobRepository {
    private final PostgreSQLConnection dbConnection;

    public JobRepository(PostgreSQLConnection postgreSQLConnection) {
        dbConnection = postgreSQLConnection;
    }

    public void insertJobDetailsWithException(List<JobDetails> jobDetailsList) throws SQLException {
        try (Connection connection = dbConnection.connect()) {
            connection.setAutoCommit(false); // Start transaction

            // Simulate an exception by throwing one intentionally
            throw new SQLException("Simulated database exception");
        }
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

    // New method to get all job details from the database
    public List<JobDetails> getAllJobDetails() throws SQLException {
        List<JobDetails> jobDetailsList = new ArrayList<>();
        String query = "SELECT j.id, j.titre, j.url, j.site_name, j.date_de_publication, j.date_pour_postuler, " +
                "j.adresse_d_entreprise, j.site_web_d_entreprise, j.nom_d_entreprise, j.description_d_entreprise, " +
                "j.description_du_poste, j.region, j.ville, j.metier, j.type_du_contrat, j.niveau_d_etudes, " +
                "j.specialite_diplome, j.experience, j.profil_recherche, j.traits_de_personnalite, " +
                "j.competences_recommandees, j.langue, j.niveau_de_la_langue, j.salaire, j.avantages_sociaux, j.teletravail, " +
                "s.secteur_d_activite, h.hard_skills, so.soft_skills " +
                "FROM jobs j " +
                "LEFT JOIN secteurs_d_activite s ON j.id = s.job_id " +
                "LEFT JOIN hard_skills h ON j.id = h.job_id " +
                "LEFT JOIN soft_skills so ON j.id = so.job_id";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                JobDetails jobDetails = new JobDetails();
                jobDetails.setId(resultSet.getInt("id"));
                jobDetails.setTitre(resultSet.getString("titre"));
                jobDetails.setUrl(resultSet.getString("url"));
                jobDetails.setSiteName(resultSet.getString("site_name"));
                jobDetails.setDatePublication(resultSet.getString("date_de_publication"));
                jobDetails.setDatePourPostuler(resultSet.getString("date_pour_postuler"));
                jobDetails.setAdresseEntreprise(resultSet.getString("adresse_d_entreprise"));
                jobDetails.setSiteEntreprise(resultSet.getString("site_web_d_entreprise"));
                jobDetails.setNomEntreprise(resultSet.getString("nom_d_entreprise"));
                jobDetails.setDescriptionEntreprise(resultSet.getString("description_d_entreprise"));
                jobDetails.setDescriptionPoste(resultSet.getString("description_du_poste"));
                jobDetails.setRegion(resultSet.getString("region"));
                jobDetails.setVille(resultSet.getString("ville"));
                jobDetails.setMetier(resultSet.getString("metier"));
                jobDetails.setTypeContrat(resultSet.getString("type_du_contrat"));
                jobDetails.setNiveauEtude(resultSet.getString("niveau_d_etudes"));
                jobDetails.setDiplomat(resultSet.getString("specialite_diplome"));
                jobDetails.setExperience(resultSet.getString("experience"));
                jobDetails.setProfil(resultSet.getString("profil_recherche"));
                jobDetails.setTraitsDePersonnalite(resultSet.getString("traits_de_personnalite"));
                jobDetails.setCompetencesRecommandes(resultSet.getString("competences_recommandees"));
                jobDetails.setLangue(resultSet.getString("langue"));
                jobDetails.setNiveauLangue(resultSet.getString("niveau_de_la_langue"));
                jobDetails.setSalaire(resultSet.getString("salaire"));
                jobDetails.setAvantagesSociaux(resultSet.getString("avantages_sociaux"));
                jobDetails.setTeletravail(resultSet.getString("teletravail"));

                // Fetch associated sectors, hard skills, and soft skills
                List<String> secteurs = new ArrayList<>();
                String secteur = resultSet.getString("secteur_d_activite");
                if (secteur != null) {
                    secteurs.add(secteur);
                }
                jobDetails.setSecteurActivites(secteurs);

                List<String> hardSkills = new ArrayList<>();
                String hardSkill = resultSet.getString("hard_skills");
                if (hardSkill != null) {
                    hardSkills.add(hardSkill);
                }
                jobDetails.setHardSkills(hardSkills);

                List<String> softSkills = new ArrayList<>();
                String softSkill = resultSet.getString("soft_skills");
                if (softSkill != null) {
                    softSkills.add(softSkill);
                }
                jobDetails.setSoftSkills(softSkills);

                jobDetailsList.add(jobDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving job details from the database", e);
        }

        return jobDetailsList;
    }

}
