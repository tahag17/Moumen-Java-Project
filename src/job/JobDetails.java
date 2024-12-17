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
    private String experienceLevel;//done

    @JsonProperty("niveauEtude")
    private String niveauEtude;//done

    @JsonProperty("function")
    private String jobTitle;//done

    @JsonProperty("region")
    private String region;

    @JsonProperty("url")
    private String url;

    @JsonProperty("siteName")
    private String siteName;

    @JsonProperty("date")
    private String date;

    @JsonProperty("siteEntreprise")
    private String siteEntreprise;

    @JsonProperty("nomEntreprise")
    private String nomEntreprise;

    @JsonProperty("description")
    private String Description;

    @JsonProperty("activitySector")
    private String activitySector;//done

    @JsonProperty("metier")
    private String metier;

    @JsonProperty("ContractType")
    private String ContractType;

    @JsonProperty("Diplomat")
    private String Diplomat;

    @JsonProperty("Profil")
    private String Profil;

    @JsonProperty("PersonnalityTraits")
    private String PersonnalityTraits;

    @JsonProperty("Language")
    private String Language;

    @JsonProperty("LanguageLevel")
    private String LanguageLevel;

    @JsonProperty("socialAdvantages")
    private String socialAdvantages;

    @JsonProperty("Teletravail")
    private String Teletravail;

    @JsonProperty("skills")
    @JsonDeserialize(using = ActivityDeserializer.class)
    private List<String> skills;//done

    public JobDetails(String experienceLevel, String jobTitle, String activitySector, String niveauEtude,
                      String region, String url, String siteName, String date, String siteEntreprise, String nomEntreprise,
                      String description, String contractType, String diplomat, String profil, String personalityTraits,
                      String language, String languageLevel, String socialAdvantages, String teletravail) {
        this.experienceLevel = experienceLevel;
        this.jobTitle = jobTitle;
        this.activitySector = activitySector;
        this.niveauEtude = niveauEtude;
        this.region = region;
        this.url = url;
        this.siteName = siteName;
        this.date = date;
        this.siteEntreprise = siteEntreprise;
        this.nomEntreprise = nomEntreprise;
        this.Description = description;
        this.ContractType = contractType;
        this.Diplomat = diplomat;
        this.Profil = profil;
        this.PersonnalityTraits = personalityTraits;
        this.Language = language;
        this.LanguageLevel = languageLevel;
        this.socialAdvantages = socialAdvantages;
        this.Teletravail = teletravail;
        this.skills = skills; // Assumes 'skills' is already formatted as needed
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
