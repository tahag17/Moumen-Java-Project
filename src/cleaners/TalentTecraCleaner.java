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
            String function = formatFunction(getSafeText(job, "jobTitle"));
            JsonNode skillsNode = job.get("skills");  // Récupérer le tableau des compétences

            // Formater les compétences (en ignorant les 3 premiers éléments et en joignant les autres par un tiret)
            String activity = formatActivity(skillsNode);

            // Créer un nouvel ObjectNode pour chaque job nettoyé
            ObjectNode cleanedJob = objectMapper.createObjectNode();

            // Ajouter les champs nettoyés au nouvel ObjectNode
            cleanedJob.put("function", function);
            cleanedJob.put("niveauEtude", niveauEtude);
            cleanedJob.put("niveauExperience", experienceLevel);
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
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText().trim() : "";  // Retourne une chaîne vide si le champ est manquant ou null
    }

    // Formater le champ "experienceLevel" pour extraire le premier entier
    private static String formatExperienceLevel(String experienceLevel) {
        if (experienceLevel != null && !experienceLevel.isEmpty() && experienceLevel.contains("Expérience requise")) {
            experienceLevel = experienceLevel.replace("Expérience requise :", "").trim();
        }
        return extractFirstInteger(experienceLevel);
    }

    // Formater le champ "niveauEtude" pour accepter Bac, Bac+N ou Bac et plus
    private static String formatNiveauEtude(String niveauEtude) {
        if (niveauEtude != null && !niveauEtude.isEmpty()) {
            // Supprimer "et plus" s'il est présent
            niveauEtude = niveauEtude.replaceAll("\\s?et plus", "").trim();

            // Vérifier si le niveau d'étude est Bac seul ou Bac+N
            if (niveauEtude.toLowerCase().contains("bac")) {
                // Si Bac seul
                if (niveauEtude.equalsIgnoreCase("bac")) {
                    return "Bac";  // Retourner simplement Bac
                } else {
                    // Extraire le niveau Bac+N, si présent
                    Pattern pattern = Pattern.compile("Bac\\+\\d+");
                    Matcher matcher = pattern.matcher(niveauEtude);

                    if (matcher.find()) {
                        return matcher.group();  // Retourner Bac+N
                    }
                }
            }
        }
        return "Non spécifié";  // Valeur par défaut si aucun format valide n'est trouvé
    }

    // Formater le champ "function" pour extraire la fonction sans la partie "Fonction :"
    private static String formatFunction(String jobTitle) {
        if (jobTitle != null && !jobTitle.isEmpty() && jobTitle.contains("Fonction :")) {
            jobTitle = jobTitle.replace("Fonction :", "").trim();  // Extraire la fonction après "Fonction :"
        }
        return jobTitle.isEmpty() ? "Non spécifié" : jobTitle;
    }

    // Méthode de nettoyage pour le champ "skills"
    private static String formatActivity(JsonNode skillsNode) {
        if (skillsNode != null && skillsNode.isArray()) {
            int size = skillsNode.size();

            // Vérifier si la liste de compétences contient plus de 3 éléments
            if (size > 3) {
                StringBuilder cleanedSkills = new StringBuilder();

                // Commencer à partir du 4ème élément (index 3)
                for (int i = 3; i < size; i++) {
                    if (i > 3) {
                        cleanedSkills.append(" - ");  // Ajouter un tiret entre les éléments
                    }
                    cleanedSkills.append(skillsNode.get(i).asText().trim());  // Ajouter chaque compétence
                }
                return cleanedSkills.toString();  // Retourner les compétences jointes par des tirets
            }
        }
        return "Non spécifié";  // Valeur par défaut si la liste a moins de 4 éléments
    }

    // Méthode pour extraire le premier entier trouvé dans une chaîne
    private static String extractFirstInteger(String input) {
        if (input != null && !input.isEmpty()) {
            Pattern pattern = Pattern.compile("\\d+");  // Expression régulière pour un entier
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                return matcher.group();  // Retourner le premier entier trouvé
            }
        }
        return "Non spécifié";  // Valeur par défaut si aucun entier n'est trouvé
    }
}