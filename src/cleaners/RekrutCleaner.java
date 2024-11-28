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

    // Méthode pour nettoyer et formater les données
    public static void cleanData(String inputFile, String outputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));

        // Créer un tableau pour stocker les données nettoyées
        ArrayNode cleanedData = objectMapper.createArrayNode();

        // Parcourir chaque entrée dans le fichier JSON
        Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            // Nettoyer le champ "title"
            String title = job.get("title").asText();
            String jobTitle = null;
            String location = null;
            if (title != null && title.contains("|")) {
                String[] parts = title.split("\\|");
                jobTitle = parts[0].replaceAll("[^\\w\\s]", "").trim(); // Supprimer les caractères non-alphanumériques
                location = parts.length > 1 ? parts[1].replaceAll("[^\\w\\s]", "").trim() : null; // Supprimer les caractères non-alphanumériques
            } else {
                jobTitle = title != null ? title.replaceAll("[^\\w\\s]", "").trim() : null;
            }

            String experience = job.get("experience").asText();

            // Extraire le premier entier rencontré dans la chaîne
            if (experience != null && !experience.isEmpty()) {
                // Utiliser une expression régulière pour trouver le premier entier
                Pattern pattern = Pattern.compile("\\d+");  // Expression régulière pour un entier
                Matcher matcher = pattern.matcher(experience);

                if (matcher.find()) {
                    experience = matcher.group();  // Si un entier est trouvé, le mettre dans "experience"
                } else {
                    experience = "0";  // Valeur par défaut si aucun entier n'est trouvé
                }
            } else {
                experience = "0";  // Valeur par défaut si la chaîne est vide ou nulle
            }

            // Nettoyer le champ "function" en supprimant la chaîne "(metier de)" si elle existe
            String function = job.get("function").asText();
            if (function != null && function.contains("(metier de)")) {
                function = function.replace("(metier de)", "").trim(); // Supprimer la chaîne "(metier de)" et les espaces supplémentaires
            }
            if (function == null) {
                function = "Non spécifié"; // Valeur par défaut si la fonction est manquante
            }

            // Nettoyer le champ "activity" (conserver même s'il n'est pas bien formaté)
            String activity = job.get("activity").asText();
            if (activity == null || activity.isEmpty()) {
                activity = "Non spécifié"; // Valeur par défaut si l'activité est manquante
            } else {
                activity = activity.replace("/", "-"); // Remplacer les '/' par des '-'
            }

            // Nettoyer le champ "educationLevel"
            String educationLevel = job.get("educationLevel").asText();
            if (educationLevel == null || educationLevel.isEmpty()) {
                educationLevel = "Non spécifié"; // Valeur par défaut si le niveau d'étude est manquant
            } else {
                educationLevel = formatNiveauEtude(educationLevel); // Nettoyage du niveau d'étude
            }

            // Ajouter les données nettoyées
            ObjectNode cleanedJob = objectMapper.createObjectNode();
            cleanedJob.put("function", jobTitle);
            cleanedJob.put("niveauExperience", experience);
            cleanedJob.put("activity", activity);
            cleanedJob.put("niveauEtude", educationLevel);

            // Ajouter le job nettoyé à la liste
            cleanedData.add(cleanedJob);
        }

        // Sauvegarder les données nettoyées dans un nouveau fichier JSON
        objectMapper.writeValue(new File(outputFile), cleanedData);
    }


    private static String formatNiveauEtude(String niveauEtude) {
        if (niveauEtude != null && !niveauEtude.isEmpty()) {
            // Expression régulière qui accepte "Bac" ou "Bac+N" (N étant un entier), avec ou sans espace avant le "+"
            Pattern pattern = Pattern.compile("Bac\\s?\\+?\\d*");  // Permet Bac, Bac+N ou Bac + N (espaces optionnels)
            Matcher matcher = pattern.matcher(niveauEtude);

            if (matcher.find()) {
                // Retirer les espaces entre "Bac" et "+" dans le résultat
                String result = matcher.group();
                result = result.replaceAll("\\s?\\+", "+");  // Remplacer l'espace avant le "+" par rien
                return result;  // Retourner le niveau d'étude propre
            }
        }
        return "Non spécifié";  // Valeur par défaut si aucune correspondance n'est trouvée
    }

    // Méthode pour vérifier si "Moins" apparaît plus d'une fois dans l'expérience
    private static boolean containsMultipleMoins(String experience) {
        if (experience != null) {
            long moinsCount = experience.split("Moins").length - 1; // Compte le nombre d'occurrences de "Moins"
            return moinsCount > 1; // Retourne true si "Moins" apparaît plus d'une fois
        }
        return false;
    }
}
