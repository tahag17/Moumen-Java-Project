package cleaners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class EmploiMaCleaner {

    // Méthode pour nettoyer et filtrer les données
    public static void cleanData(String inputFile, String outputFile) throws IOException {
        // Charger le fichier JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));

        // Créer un tableau pour stocker les données valides
        ArrayNode cleanedData = objectMapper.createArrayNode();

        // Parcourir chaque entrée dans le fichier JSON
        Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            // Vérification du format du titre (avant le tiret est le titre, après c'est le lieu)
            String title = job.get("title").asText();
            if (title != null && title.contains("-")) {
                String[] parts = title.split(" - ");
                String cleanedTitle = parts[0].trim();
                // Location est supprimé ici

                // Nettoyage du niveau d'étude (acceptation de plusieurs niveaux, Bac+N ou Bac seul)
                String niveauEtude = job.get("niveauEtude").asText();
                if (niveauEtude != null && isValidNiveauEtude(niveauEtude)) {

                    // Nettoyage du niveau d'expérience (format "Expérience entre X ans et Y ans")
                    String niveauExperience = job.get("niveauExperience").asText();
                    if (niveauExperience != null && niveauExperience.matches("Expérience entre \\d+ ans et \\d+ ans")) {

                        // Nettoyage des compétences (s'assurer qu'elles ne sont pas vides)
                        String competence = job.get("competence").asText();
                        if (competence != null && !competence.isEmpty()) {

                            // Ajouter l'entrée nettoyée à la liste des données valides
                            ObjectNode cleanedJob = objectMapper.createObjectNode();
                            cleanedJob.put("fucntion", cleanedTitle);
                            // Pas de champ location
                            cleanedJob.put("niveauEtude", niveauEtude.trim());
                            cleanedJob.put("niveauExperience", niveauExperience.trim());
                            cleanedJob.put("activity", competence.trim());

                            // Ajouter le job nettoyé à la liste
                            cleanedData.add(cleanedJob);
                        }
                    }
                }
            }
        }

        // Sauvegarder les données nettoyées dans un nouveau fichier JSON
        objectMapper.writeValue(new File(outputFile), cleanedData);
    }

    // Méthode de validation pour le champ niveauEtude (accepte plusieurs niveaux)
    private static boolean isValidNiveauEtude(String niveauEtude) {
        // Expression régulière qui accepte Bac+N, Bac ou plusieurs niveaux séparés par une virgule
        String regex = "(Bac(\\+\\d+)?)(,\\s?Bac(\\+\\d+)?)*\\s?(et plus)?";
        return niveauEtude.matches(regex);
    }

}
