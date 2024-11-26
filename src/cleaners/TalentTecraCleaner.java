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

            // Nettoyer et formater les champs nécessaires
            String experienceLevel = formatExperienceLevel(job.get("experienceLevel").asText());
            String niveauEtude = formatNiveauEtude(job.get("niveauEtude").asText());
            String function = formatFunction(job.get("function").asText());
            String activity = formatActivity(job.get("activity").asText());

            // Créer un nouvel ObjectNode pour chaque job nettoyé
            ObjectNode cleanedJob = objectMapper.createObjectNode();

            // Ajouter les champs nettoyés au nouvel ObjectNode
            cleanedJob.put("niveauExperience", experienceLevel);
            cleanedJob.put("niveauEtude", niveauEtude);
            cleanedJob.put("funtion", function);
            cleanedJob.put("activity", activity);

            // Ajouter le job nettoyé à la liste
            cleanedData.add(cleanedJob);
        }

        // Sauvegarder les données nettoyées dans un nouveau fichier JSON
        objectMapper.writeValue(new File(outputFile), cleanedData);
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
    private static String formatFunction(String function) {
        if (function != null && function.contains("Fonction :")) {
            return function.replace("Fonction :", "").trim();
        }
        return function != null ? function.trim() : "";
    }

    // Formater le champ "activity"
    private static String formatActivity(String activity) {
        if (activity != null && activity.contains("Secteur d’activité :")) {
            return activity.replace("Secteur d’activité :", "").trim();
        }
        return activity != null ? activity.trim() : "";
    }
}
