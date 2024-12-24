package job;

public class JobService {
    public String formatJobDetails(JobDetails job) {
        StringBuilder jobDetailsBuilder = new StringBuilder();

        // Basic job details
        jobDetailsBuilder.append("Job ID: ").append(job.getId()).append("\n")
                .append("Title: ").append(job.getTitre()).append("\n")
                .append("URL: ").append(job.getUrl()).append("\n")
                .append("Site Name: ").append(job.getSiteName()).append("\n")
                .append("Publication Date: ").append(job.getDatePublication()).append("\n")
                .append("Apply By: ").append(job.getDatePourPostuler()).append("\n")
                .append("Company Address: ").append(job.getAdresseEntreprise()).append("\n")
                .append("Company Website: ").append(job.getSiteEntreprise()).append("\n")
                .append("Company Name: ").append(job.getNomEntreprise()).append("\n")
                .append("Company Description: ").append(job.getDescriptionEntreprise()).append("\n")
                .append("Job Description: ").append(job.getDescriptionPoste()).append("\n")
                .append("Region: ").append(job.getRegion()).append("\n")
                .append("City: ").append(job.getVille()).append("\n")
                .append("Job Type: ").append(job.getMetier()).append("\n")
                .append("Contract Type: ").append(job.getTypeContrat()).append("\n")
                .append("Education Level: ").append(job.getNiveauEtude()).append("\n")
                .append("Degree Specialization: ").append(job.getDiplomat()).append("\n")
                .append("Experience: ").append(job.getExperience()).append("\n")
                .append("Profile: ").append(job.getProfil()).append("\n")
                .append("Personality Traits: ").append(job.getTraitsDePersonnalite()).append("\n")
                .append("Recommended Skills: ").append(job.getCompetencesRecommandes()).append("\n")
                .append("Language: ").append(job.getLangue()).append("\n")
                .append("Language Level: ").append(job.getNiveauLangue()).append("\n")
                .append("Salary: ").append(job.getSalaire()).append("\n")
                .append("Social Benefits: ").append(job.getAvantagesSociaux()).append("\n")
                .append("Remote Work: ").append(job.getTeletravail()).append("\n");

        // Printing associated sectors
        if (!job.getSecteurActivites().isEmpty()) {
            jobDetailsBuilder.append("Sectors:\n");
            for (String secteur : job.getSecteurActivites()) {
                jobDetailsBuilder.append("  - ").append(secteur).append("\n");
            }
        } else {
            jobDetailsBuilder.append("No sectors specified.\n");
        }

        // Printing hard skills
        if (!job.getHardSkills().isEmpty()) {
            jobDetailsBuilder.append("Hard Skills:\n");
            for (String skill : job.getHardSkills()) {
                jobDetailsBuilder.append("  - ").append(skill).append("\n");
            }
        } else {
            jobDetailsBuilder.append("No hard skills specified.\n");
        }

        // Printing soft skills
        if (!job.getSoftSkills().isEmpty()) {
            jobDetailsBuilder.append("Soft Skills:\n");
            for (String skill : job.getSoftSkills()) {
                jobDetailsBuilder.append("  - ").append(skill).append("\n");
            }
        } else {
            jobDetailsBuilder.append("No soft skills specified.\n");
        }

        return jobDetailsBuilder.toString();
    }

}
