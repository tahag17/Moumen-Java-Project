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

                if (currentToken == JsonToken.VALUE_STRING) {
            String activityStr = p.getValueAsString();
                        String[] splitActivities = activityStr.split(" - ");
            for (String activity : splitActivities) {
                activities.add(activity.trim());             }
        } else if (currentToken == JsonToken.START_ARRAY) {
                        p.nextToken();             while (p.getCurrentToken() != JsonToken.END_ARRAY) {
                activities.add(p.getValueAsString());
                p.nextToken();
            }
        }

        return activities;
    }
}
