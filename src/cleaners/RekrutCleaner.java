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

        public static void cleanData(String inputFile, String outputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));

                ArrayNode cleanedData = objectMapper.createArrayNode();

                Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

                        String title = job.get("title").asText();
            String jobTitle = null;
            String location = null;
            if (title != null && title.contains("|")) {
                String[] parts = title.split("\\|");
                jobTitle = parts[0].replaceAll("[^\\w\\s]", "").trim();                 location = parts.length > 1 ? parts[1].replaceAll("[^\\w\\s]", "").trim() : null;             } else {
                jobTitle = title != null ? title.replaceAll("[^\\w\\s]", "").trim() : null;
            }

            String experience = job.get("experience").asText();

                        if (experience != null && !experience.isEmpty()) {
                                Pattern pattern = Pattern.compile("\\d+");                  Matcher matcher = pattern.matcher(experience);

                if (matcher.find()) {
                    experience = matcher.group();                  } else {
                    experience = "0";                  }
            } else {
                experience = "0";              }

                        String function = job.get("function").asText();
            if (function != null && function.contains("(metier de)")) {
                function = function.replace("(metier de)", "").trim();             }
            if (function == null) {
                function = "Non spécifié";             }

                        String activity = job.get("activity").asText();
            if (activity == null || activity.isEmpty()) {
                activity = "Non spécifié";             } else {
                activity = activity.replace("/", "-");             }

                        String educationLevel = job.get("educationLevel").asText();
            if (educationLevel == null || educationLevel.isEmpty()) {
                educationLevel = "Non spécifié";             } else {
                educationLevel = formatNiveauEtude(educationLevel);             }

                        ObjectNode cleanedJob = objectMapper.createObjectNode();
            cleanedJob.put("function", jobTitle);
            cleanedJob.put("niveauEtude", educationLevel);
            cleanedJob.put("niveauExperience", experience);
            cleanedJob.put("activity", activity);


                        cleanedData.add(cleanedJob);
        }

                objectMapper.writeValue(new File(outputFile), cleanedData);
    }


    private static String formatNiveauEtude(String niveauEtude) {
        if (niveauEtude != null && !niveauEtude.isEmpty()) {
                        Pattern pattern = Pattern.compile("Bac\\s?\\+?\\d*");              Matcher matcher = pattern.matcher(niveauEtude);

            if (matcher.find()) {
                                String result = matcher.group();
                result = result.replaceAll("\\s?\\+", "+");                  return result;              }
        }
        return "Non spécifié";      }

        private static boolean containsMultipleMoins(String experience) {
        if (experience != null) {
            long moinsCount = experience.split("Moins").length - 1;             return moinsCount > 1;         }
        return false;
    }
}
