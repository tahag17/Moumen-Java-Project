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
        //1
        private Integer id;
        //missed
        @JsonProperty("Titre")
        private String titre;
        //2
        @JsonProperty("URL")
        private String url;
        //3
        @JsonProperty("site Name")
        private String siteName;
        //4
        @JsonProperty("Date de publication")
        private String datePublication;
        //5
        @JsonProperty("Date pour postuler")
        private String datePourPostuler;
        //6
        @JsonProperty("Adresse d'entreprise")
        private String adresseEntreprise;
        //7
        @JsonProperty("Site web d'entreprise")
        private String siteEntreprise;
        //8
        @JsonProperty("Nom d'entreprise")
        private String nomEntreprise;
        //9
        @JsonProperty("Description d'entreprise")
        private String descriptionEntreprise;
        //10
        @JsonProperty("Description du poste")
        private String descriptionPoste;
        //11
        @JsonProperty("Région")
        private String region;
        //12
        @JsonProperty("Ville")
        private String ville;
        //13
        @JsonProperty("Secteur d'activité")
        @JsonDeserialize(using = ActivityDeserializer.class)
        private List<String> secteurActivites;
        //14
        @JsonProperty("Métier")
        private String metier;
        //15
        @JsonProperty("Type du contrat")
        private String typeContrat;
        //16
        @JsonProperty("Niveau d'études")
        private String niveauEtude;
        //17
        @JsonProperty("Spécialité/Diplôme")
        private String Diplomat;
        //18
        @JsonProperty("Expérience")
        private String experience;
        //19
        @JsonProperty("Profil recherché")
        private String Profil;
        //20
        @JsonProperty("Traits de personnalité")
        private String traitsDePersonnalite;
        //21
        @JsonProperty("Compétences requises (hard skills)")
        @JsonDeserialize(using = ActivityDeserializer.class)
        private List<String> hardSkills;
        //22
        @JsonProperty("Soft-Skills")
        @JsonDeserialize(using = ActivityDeserializer.class)
        private List<String> softSkills;
        //23
        @JsonProperty("Compétences recommandées")
        private String competencesRecommandes;
        //24
        @JsonProperty("Langue")
        private String langue;
        //25
        @JsonProperty("Niveau de la langue")
        private String niveauLangue;
        //26
        @JsonProperty("Salaire")
        private String salaire;
        //27
        @JsonProperty("Avantages sociaux")
        private String avantagesSociaux;
        //28
        @JsonProperty("Teletravail")
        private String Teletravail;




    }
