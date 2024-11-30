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

        public static void cleanData(String inputFile, String outputFile) throws IOException {
                ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(inputFile));

                ArrayNode cleanedData = objectMapper.createArrayNode();

                Iterator<JsonNode> elementsIterator = rootNode.iterator();
        while (elementsIterator.hasNext()) {
            JsonNode job = elementsIterator.next();

                        String title = job.get("title").asText();
            if (title != null && title.contains("-")) {
                String[] parts = title.split(" - ");
                String cleanedTitle = parts[0].trim(); 
                                String niveauEtude = job.get("niveauEtude").asText();
                if (niveauEtude != null && isValidNiveauEtude(niveauEtude)) {

                                        String niveauExperience = job.get("niveauExperience").asText();
                    if (niveauExperience != null) {
                                                Pattern pattern = Pattern.compile("\\d+");                          Matcher matcher = pattern.matcher(niveauExperience);

                        if (matcher.find()) {
                            niveauExperience = matcher.group();                          } else {
                            niveauExperience = "0";                          }
                    } else {
                        niveauExperience = "0";                      }

                                        String competence = job.get("competence").asText();
                    if (competence != null && !competence.isEmpty()) {

                                                ObjectNode cleanedJob = objectMapper.createObjectNode();
                        cleanedJob.put("function", cleanedTitle);
                        cleanedJob.put("niveauEtude", niveauEtude.trim());
                        cleanedJob.put("niveauExperience", niveauExperience.trim());
                        cleanedJob.put("activity", competence.trim());

                                                cleanedData.add(cleanedJob);
                    }
                }
            }
        }

                objectMapper.writeValue(new File(outputFile), cleanedData);
    }

        private static boolean isValidNiveauEtude(String niveauEtude) {
                String regex = "Bac(\\+\\d+)?";  
                return niveauEtude.matches(regex) && !niveauEtude.toLowerCase().contains("et plus");
    }
}
