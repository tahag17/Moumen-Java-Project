package cleaners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TalentTecraCleaner {

    // Méthode pour nettoyer et formater les données
    public static void cleanData(String inputFile, String outputFile) throws IOException {
        // Charger le fichier JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));

        // Créer un tableau pour stocker les données nettoyées
        ArrayNode cleanedData = objectMapper.createArrayNode();

        // Parcourir chaque entrée dans le fichier JSON
        Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            // Utiliser la méthode sécurisée pour obtenir les valeurs
            String experienceLevel = formatExperienceLevel(getSafeText(job, "experienceLevel"));
            String niveauEtude = formatNiveauEtude(getSafeText(job, "niveauEtude"));
            String function = formatFunction(getSafeText(job, "jobTitle")); // Changed to jobTitle here
            String activity = formatActivity(getSafeText(job, "skills"));

            // Créer un nouvel ObjectNode pour chaque job nettoyé
            ObjectNode cleanedJob = objectMapper.createObjectNode();

            // Ajouter les champs nettoyés au nouvel ObjectNode
            cleanedJob.put("niveauExperience", experienceLevel);
            cleanedJob.put("niveauEtude", niveauEtude);
            cleanedJob.put("funtion", function); // Ensure correct spelling if needed
            cleanedJob.put("activity", activity);

            // Ajouter le job nettoyé à la liste
            cleanedData.add(cleanedJob);
        }

        // Sauvegarder les données nettoyées dans un nouveau fichier JSON
        objectMapper.writeValue(new File(outputFile), cleanedData);
    }

    // Méthode pour obtenir le texte de manière sécurisée
    private static String getSafeText(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText() : "";  // Retourne une chaîne vide si le champ est manquant ou null
    }

    // Formater le champ "experienceLevel"
    private static String formatExperienceLevel(String experienceLevel) {
        if (experienceLevel != null && experienceLevel.contains("Expérience requise")) {
            return experienceLevel.replace("Expérience requise :", "").trim();
        }
        return experienceLevel != null ? experienceLevel.trim() : "";
    }

    // Formater le champ "niveauEtude"
    private static String formatNiveauEtude(String niveauEtude) {
        if (niveauEtude != null && niveauEtude.contains("Niveau d’étude demandé")) {
            return niveauEtude.replace("Niveau d’étude demandé :", "").trim();
        }
        return niveauEtude != null ? niveauEtude.trim() : "";
    }

    // Formater le champ "function"
    private static String formatFunction(String jobTitle) {
        if (jobTitle != null && jobTitle.contains("Fonction :")) {
            return jobTitle.replace("Fonction :", "").trim(); // Extract function after "Fonction :"
        }
        return jobTitle != null ? jobTitle.trim() : "";
    }

    // Formater le champ "activity"
    private static String formatActivity(String skills) {
        if (skills != null && skills.contains("Secteur d’activité :")) {
            return skills.replace("Secteur d’activité :", "").trim();
        }
        return skills != null ? skills.trim() : "";
    }
}
