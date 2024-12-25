CREATE TABLE jobs (
                      id SERIAL PRIMARY KEY,
                      titre VARCHAR(255000),
                      url VARCHAR(255000),
                      site_name VARCHAR(255000),
                      date_de_publication VARCHAR(255000),
                      date_pour_postuler VARCHAR(255000),
                      adresse_d_entreprise VARCHAR(255000),
                      site_web_d_entreprise VARCHAR(255000),
                      nom_d_entreprise VARCHAR(255000),
                      description_d_entreprise VARCHAR(255000),
                      description_du_poste VARCHAR(255000),
                      region VARCHAR(255000),
                      ville VARCHAR(255000),
                      metier VARCHAR(255000),
                          type_du_contrat VARCHAR(255000),
                      niveau_d_etudes VARCHAR(255000),
                      specialite_diplome VARCHAR(255000),
                          experience VARCHAR(255000),
                      profil_recherche VARCHAR(255000),
                      traits_de_personnalite VARCHAR(255000),
                      competences_recommandees VARCHAR(255000),
                      langue VARCHAR(255000),
                      niveau_de_la_langue VARCHAR(255000),
                      salaire VARCHAR(255000),
                      avantages_sociaux VARCHAR(255000),
                      teletravail VARCHAR(255000)
);

CREATE TABLE secteurs_d_activite (
                                     id SERIAL PRIMARY KEY,
                                     secteur_d_activite VARCHAR(255),
                                     job_id INTEGER,
                                     FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE hard_skills (
                             id SERIAL PRIMARY KEY,
                             hard_skills VARCHAR(255),
                             job_id INTEGER,
                             FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE soft_skills (
                             id SERIAL PRIMARY KEY,
                             soft_skills VARCHAR(255),
                             job_id INTEGER,
                             FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);
