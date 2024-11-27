package job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;

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
    private String skills;

    public JobDetails(String experienceLevel, String jobTitle, String skills, String niveauEtude) {
        this.experienceLevel = experienceLevel;
        this.jobTitle = jobTitle;
        this.skills = skills;
        this.niveauEtude = niveauEtude;
    }


}
