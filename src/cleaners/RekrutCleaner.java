package cleaners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RekrutCleaner {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void cleanData(String inputFile, String outputFile) throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));
        ArrayNode cleanedData = objectMapper.createArrayNode();

        Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            ObjectNode cleanedJob = objectMapper.createObjectNode();

            cleanedJob.put("Titre", getSafeText(job, "title"));
            cleanedJob.put("URL", getSafeText(job,"url"));
            cleanedJob.put("Site Name", "Rekrut");
            cleanedJob.put("Date de publication", "");
            cleanedJob.put("Date pour postuler", "");
            cleanedJob.put("Adresse d'entreprise", "");
            cleanedJob.put("Site web d'entreprise", getSafeText(job,"entrepriseURL"));
            cleanedJob.put("Nom d'entreprise", "");
            cleanedJob.put("Description d'entreprise", getSafeText(job,"desc"));
            cleanedJob.put("Description du poste", getSafeText(job, "function"));
            cleanedJob.put("Région", "");
            cleanedJob.put("Ville", "");
            cleanedJob.put("Secteur d'activité", getSafeText(job,"activity"));
            cleanedJob.put("Métier", "");
            cleanedJob.put("Type du contrat", cleanContractType(getSafeText(job,"typeC")));
            cleanedJob.put("Niveau d'études", cleanEducationLevel(getSafeText(job, "educationLevel")));
            cleanedJob.put("Spécialité/Diplôme", "");
            cleanedJob.put("Expérience", cleanExperience(getSafeText(job,"experience")));
            cleanedJob.put("Profil recherché", "");
            cleanedJob.put("Traits de personnalité", "");
            cleanedJob.put("Compétences requises (hard skills)", "");
            cleanedJob.put("Soft-Skills", "");
            cleanedJob.put("Compétences recommandées", "");
            cleanedJob.put("Langue", "");
            cleanedJob.put("Niveau de la langue", "");
            cleanedJob.put("Salaire", "");
            cleanedJob.put("Avantages sociaux", "");
            cleanedJob.put("Télétravail", cleanTelework(getSafeText(job,"teletravail")));

            cleanedData.add(cleanedJob);
        }

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), cleanedData);
        System.out.println("Nettoyage effectué avec succès. Fichier sauvegardé à : " + outputFile);
    }

    private static String getSafeText(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText().trim() : "";
    }

  // Méthode pour nettoyer le champ 'Niveau d'études' et ne retenir que le premier Bac+x ou Bac
    private static String cleanEducationLevel(String educationLevel) {
        if (educationLevel == null || educationLevel.isEmpty()) {
            return "";
        }

        // Convertir la chaîne en minuscules et supprimer les espaces inutiles
        educationLevel = educationLevel.replaceAll("\\s+", " ").trim().toLowerCase();

        // Chercher la première occurrence de "bac"
        int bacIndex = educationLevel.indexOf("bac");

        if (bacIndex == -1) {
            return ""; // Si "bac" n'est pas trouvé, on retourne une chaîne vide
        }

        // Vérifier si après "bac", il y a un "+" suivi d'un chiffre
        int plusIndex = educationLevel.indexOf("+", bacIndex);
        if (plusIndex != -1 && plusIndex + 1 < educationLevel.length() && Character.isDigit(educationLevel.charAt(plusIndex + 1))) {
            // Extraire le chiffre après "bac+" et retourner la chaîne "bac+x"
            return "Bac+" + educationLevel.charAt(plusIndex + 1);
        } else {
            // Si il n'y a pas de "+", retourner seulement "bac"
            return "Bac";
        }
    }





    // Méthode pour nettoyer le champ 'Type de contrat'
    private static String cleanContractType(String contractType) {
        if (contractType == null || contractType.isEmpty()) {
            return "";
        }

        // Retourne le premier mot de type de contrat
        String[] parts = contractType.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    // Méthode pour nettoyer le champ 'Télétravail'
    private static String cleanTelework(String telework) {
        if (telework == null || telework.isEmpty()) {
            return "";
        }

        // Retourne le premier mot rencontré
        String[] parts = telework.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    // Méthode pour nettoyer le champ 'Expérience'
    private static String cleanExperience(String experience) {
        if (experience == null || experience.isEmpty()) {
            return "";
        }

        // Utilisation d'une expression régulière pour extraire le premier nombre
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(experience);

        if (matcher.find()) {
            return matcher.group();
        }

        return "";
    }

    public static void main(String[] args) {
        try {
            String inputFilePath = "input_wetech.json";
            String outputFilePath = "output_cleaned_wetech.json";
            cleanData(inputFilePath, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
