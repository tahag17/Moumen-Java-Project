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

public class MjobCleaner {
    public static void cleanData(String inputFile, String outputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));
        ArrayNode cleanedData = objectMapper.createArrayNode();
        Iterator<JsonNode> elementsIterator = rootNode.iterator();

        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

            // Extract and clean data

            String experience = cleanExperience(job.get("experienceLevel"));
            String educationLevel = cleanEducationLevel(job.get("niveauEtude"));
            String activity = formatActivity(job.get("skills"));
            String function = formatFunction(getSafeText(job, "jobTitle"));

            // Build cleaned job object
            ObjectNode cleanedJob = objectMapper.createObjectNode();
            cleanedJob.put("function", function);
            cleanedJob.put("niveauEtude", educationLevel);
            cleanedJob.put("niveauExperience", experience);
            cleanedJob.put("activity", activity);


            cleanedData.add(cleanedJob);
        }

        // Write cleaned data to output file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), cleanedData);
        System.out.println("Cleaned data saved to " + outputFile);
    }

    private static String cleanExperience(JsonNode experienceNode) {
        if (experienceNode != null && !experienceNode.isNull()) {
            String experience = experienceNode.asText().trim();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(experience);

            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "0"; // Default experience level
    }

    private static String cleanEducationLevel(JsonNode educationNode) {
        if (educationNode != null && !educationNode.isNull()) {
            String niveauEtude = educationNode.asText().trim();
            return formatNiveauEtude(niveauEtude);
        }
        return "Non spécifié";
    }

    private static String formatNiveauEtude(String niveauEtude) {
        if (niveauEtude != null && !niveauEtude.isEmpty()) {
            // Case-insensitive pattern to match "BAC", "Bac", or "bac" with optional "+<number>"
            Pattern pattern = Pattern.compile("(?i)BAC\\s*\\+?\\d*");
            Matcher matcher = pattern.matcher(niveauEtude);

            if (matcher.find()) {
                // Extract the matched string
                String result = matcher.group();
                // Normalize the case to "Bac" and format with "+"
                result = result.replaceAll("(?i)BAC", "Bac").replaceAll("\\s*\\+", "+");
                return result;
            }
        }
        return "Non spécifié";
    }

    private static String formatActivity(JsonNode skillsNode) {
        if (skillsNode != null && skillsNode.isArray()) {
            StringBuilder cleanedSkills = new StringBuilder();
            for (int i = 0; i < skillsNode.size(); i++) {
                if (i > 0) {
                    cleanedSkills.append(" - ");
                }
                cleanedSkills.append(skillsNode.get(i).asText().trim());
            }
            return cleanedSkills.toString();
        }
        return "Non spécifié";
    }

    private static String getSafeText(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText().trim() : "Non spécifié";
    }

    private static String formatFunction(String jobTitle) {
        if (jobTitle != null && !jobTitle.isEmpty() && jobTitle.contains("Fonction :")) {
            return jobTitle.replace("Fonction :", "").trim();
        }
        return jobTitle.isEmpty() ? "Non spécifié" : jobTitle;
    }
}
