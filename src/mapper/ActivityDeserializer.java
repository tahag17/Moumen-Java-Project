package mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<String> activities = new ArrayList<>();
        JsonToken currentToken = p.getCurrentToken();

        // Check if the token is a string (i.e., a single value)
        if (currentToken == JsonToken.VALUE_STRING) {
            String activityStr = p.getValueAsString();
            // If the activity contains multiple values separated by hyphens, split them into a list
            String[] splitActivities = activityStr.split(" - ");
            for (String activity : splitActivities) {
                activities.add(activity.trim()); // Add each split value
            }
        } else if (currentToken == JsonToken.START_ARRAY) {
            // Handle case where activity is already an array
            p.nextToken(); // Move into the array
            while (p.getCurrentToken() != JsonToken.END_ARRAY) {
                activities.add(p.getValueAsString());
                p.nextToken();
            }
        }

        return activities;
    }
}
