package cleaners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



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
                String cleanedTitle = parts[0].trim(); // Location est supprimé ici

                // Nettoyage du niveau d'étude (acceptation de plusieurs niveaux, Bac+N ou Bac seul)
                String niveauEtude = job.get("niveauEtude").asText();
                if (niveauEtude != null && isValidNiveauEtude(niveauEtude)) {

                    // Nettoyage du niveau d'expérience (extraction du premier entier trouvé)
                    String niveauExperience = job.get("niveauExperience").asText();
                    if (niveauExperience != null) {
                        // Utiliser une expression régulière pour trouver le premier entier dans la chaîne
                        Pattern pattern = Pattern.compile("\\d+");  // Expression régulière pour un entier
                        Matcher matcher = pattern.matcher(niveauExperience);

                        if (matcher.find()) {
                            niveauExperience = matcher.group();  // Retourner le premier entier trouvé
                        } else {
                            niveauExperience = "0";  // Valeur par défaut si aucun entier n'est trouvé
                        }
                    } else {
                        niveauExperience = "0";  // Si le champ niveauExperience est null, définir une valeur par défaut
                    }

                    // Nettoyage des compétences (s'assurer qu'elles ne sont pas vides)
                    String competence = job.get("competence").asText();
                    if (competence != null && !competence.isEmpty()) {

                        // Ajouter l'entrée nettoyée à la liste des données valides
                        ObjectNode cleanedJob = objectMapper.createObjectNode();
                        cleanedJob.put("function", cleanedTitle);
                        cleanedJob.put("niveauEtude", niveauEtude.trim());
                        cleanedJob.put("niveauExperience", niveauExperience.trim());
                        cleanedJob.put("activity", competence.trim());

                        // Ajouter le job nettoyé à la liste
                        cleanedData.add(cleanedJob);
                    }
                }
            }
        }

        // Sauvegarder les données nettoyées dans un nouveau fichier JSON
        objectMapper.writeValue(new File(outputFile), cleanedData);
    }

    // Méthode de validation pour le champ niveauEtude (accepte un seul niveau d'étude, "et plus" non autorisé)
    private static boolean isValidNiveauEtude(String niveauEtude) {
        // Expression régulière qui accepte uniquement "Bac" ou "Bac+N" sans "et plus"
        String regex = "Bac(\\+\\d+)?";  // Permet "Bac" ou "Bac+N" (N étant un entier)

        // Vérifier si le niveau d'étude correspond à l'expression régulière et s'il ne contient pas "et plus"
        return niveauEtude.matches(regex) && !niveauEtude.toLowerCase().contains("et plus");
    }
}
