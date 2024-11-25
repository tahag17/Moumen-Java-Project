package Job;
import lombok.*;

import java.util.Objects;


@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class JobDetails {

    private Integer id;
    private String domain;
    private String function;
    private String educationalLevel;
    private String experience;
    private String skills;

    public JobDetails(String domain, String function, String educationalLevel, String experience) {
        this.domain = domain;
        this.function = function;
        this.educationalLevel = educationalLevel;
        this.experience = experience;
    }
}
