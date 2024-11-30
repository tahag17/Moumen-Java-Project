package job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import mapper.ActivityDeserializer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDetails {
    private Integer id;

    @JsonProperty("niveauExperience")
    private String experienceLevel;

    @JsonProperty("niveauEtude")
    private String niveauEtude;

    @JsonProperty("function")
    private String jobTitle;

    @JsonProperty("activity")
    @JsonDeserialize(using = ActivityDeserializer.class)
    private List<String> skills;

        public JobDetails(String experienceLevel, String jobTitle, String activity, String niveauEtude) {
        this.experienceLevel = experienceLevel;
        this.jobTitle = jobTitle;
        this.skills = formatSkills(activity);
        this.niveauEtude = niveauEtude;
    }
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<String> getSkills() {return skills;}

    private List<String> formatSkills(String activity) {
        if (activity != null && !activity.isEmpty()) {
            return List.of(activity.split("[,\\s-]+"))
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

}
