package job;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class JobDetails {
    private Integer id;
    private String experienceLevel;
    private String niveauEtude;
    private String function;
    private String activity;

    public JobDetails(String experienceLevel, String function, String activity, String niveauEtude) {
        this.experienceLevel = experienceLevel;
        this.function = function;
        this.activity = activity;
        this.niveauEtude = niveauEtude;
    }


}
