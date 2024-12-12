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

        public static void cleanData(String inputFile, String outputFile) throws IOException {
                ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));

                ArrayNode cleanedData = objectMapper.createArrayNode();

                Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            String experienceLevel = formatExperienceLevel(getSafeText(job, "experienceLevel"));
            String niveauEtude = formatNiveauEtude(getSafeText(job, "niveauEtude"));
            String function = formatFunction(getSafeText(job, "jobTitle"));
            JsonNode skillsNode = job.get("skills");  
            String activity = formatActivity(skillsNode);

            ObjectNode cleanedJob = objectMapper.createObjectNode();

            cleanedJob.put("function", function);
            cleanedJob.put("niveauEtude", niveauEtude);
            cleanedJob.put("niveauExperience", experienceLevel);
            cleanedJob.put("activity", activity);

            cleanedData.add(cleanedJob);
        }

                objectMapper.writeValue(new File(outputFile), cleanedData);
    }

        private static String getSafeText(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText().trim() : "";      }

        private static String formatExperienceLevel(String experienceLevel) {
        if (experienceLevel != null && !experienceLevel.isEmpty() && experienceLevel.contains("Expérience requise")) {
            experienceLevel = experienceLevel.replace("Expérience requise :", "").trim();
        }
        return extractFirstInteger(experienceLevel);
    }

        private static String formatNiveauEtude(String niveauEtude) {
        if (niveauEtude != null && !niveauEtude.isEmpty()) {
                        niveauEtude = niveauEtude.replaceAll("\\s?et plus", "").trim();

                        if (niveauEtude.toLowerCase().contains("bac")) {
                                if (niveauEtude.equalsIgnoreCase("bac")) {
                    return "Bac";                  } else {
                                        Pattern pattern = Pattern.compile("Bac\\+\\d+");
                    Matcher matcher = pattern.matcher(niveauEtude);

                    if (matcher.find()) {
                        return matcher.group();                      }
                }
            }
        }
        return "Non spécifié";      }

        private static String formatFunction(String jobTitle) {
        if (jobTitle != null && !jobTitle.isEmpty() && jobTitle.contains("Fonction :")) {
            jobTitle = jobTitle.replace("Fonction :", "").trim();          }
        return jobTitle.isEmpty() ? "Non spécifié" : jobTitle;
    }

        private static String formatActivity(JsonNode skillsNode) {
        if (skillsNode != null && skillsNode.isArray()) {
            int size = skillsNode.size();

                        if (size > 3) {
                StringBuilder cleanedSkills = new StringBuilder();

                                for (int i = 3; i < size; i++) {
                    if (i > 3) {
                        cleanedSkills.append(" - ");                      }
                    cleanedSkills.append(skillsNode.get(i).asText().trim());                  }
                return cleanedSkills.toString();              }
        }
        return "Non spécifié";
        }

        private static String extractFirstInteger(String input) {
        if (input != null && !input.isEmpty()) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                return matcher.group();              }
        }
        return "Non spécifié";      }
}