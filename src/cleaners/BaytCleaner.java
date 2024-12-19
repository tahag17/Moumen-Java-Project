package cleaners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class BaytCleaner {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void cleanData(String inputFile, String outputFile) throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));
        ArrayNode cleanedData = objectMapper.createArrayNode();

        Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            ObjectNode cleanedJob = objectMapper.createObjectNode();

            cleanedJob.put("Titre", getSafeText(job, "title"));
            cleanedJob.put("URL", ""); // Pas de champ URL dans l'exemple d'entrée
            cleanedJob.put("Site Name", "Bayt"); // Nom du site spécifique
            cleanedJob.put("Date de publication", getSafeText(job,"date")); // Pas de champ date dans l'exemple d'entrée
            cleanedJob.put("Date pour postuler", ""); // Pas de champ date de postulation dans l'exemple d'entrée
            cleanedJob.put("Adresse d'entreprise", ""); // Pas de champ d'adresse
            cleanedJob.put("Site web d'entreprise", ""); // Pas de champ site web entreprise
            cleanedJob.put("Nom d'entreprise", ""); // Pas de champ nom entreprise
            cleanedJob.put("Description d'entreprise", ""); // Pas de champ description entreprise
            cleanedJob.put("Description du poste", getSafeText(job, "description"));
            cleanedJob.put("Région", ""); // Pas de champ région
            cleanedJob.put("Ville", ""); // Pas de champ ville
            cleanedJob.put("Secteur d'activité", ""); // Pas de champ secteur d'activité spécifique
            cleanedJob.put("Métier", ""); // Pas de champ métier
            cleanedJob.put("Type du contrat", ""); // Pas de champ type de contrat
            cleanedJob.put("Niveau d'études", "");
            cleanedJob.put("Spécialité/Diplôme", ""); // Pas de champ spécialité
            cleanedJob.put("Expérience", "");
            cleanedJob.put("Profil recherché", "");
            cleanedJob.put("Traits de personnalité", ""); // Pas de champ traits de personnalité
            cleanedJob.put("Compétences requises (hard skills)","");
            cleanedJob.put("Soft-Skills","");
            cleanedJob.put("Compétences recommandées", ""); // Pas de champ compétences recommandées
            cleanedJob.put("Langue", "");
            cleanedJob.put("Niveau de la langue", ""); // Pas de champ niveau de la langue
            cleanedJob.put("Salaire", ""); // Pas de champ salaire
            cleanedJob.put("Avantages sociaux", ""); // Pas de champ avantages sociaux
            cleanedJob.put("Télétravail", ""); // Pas de champ télétravail

            cleanedData.add(cleanedJob);
        }

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), cleanedData);
        System.out.println("Nettoyage effectué avec succès. Fichier sauvegardé à : " + outputFile);
    }

    private static String getSafeText(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText().trim() : "";
    }



}
