CREATE TABLE job_details (
                             id SERIAL PRIMARY KEY,
                             experience_level VARCHAR(255) NOT NULL,
                             niveau_etude VARCHAR(255),
                             job_title VARCHAR(255) NOT NULL
);

CREATE TABLE skills (
                        id SERIAL PRIMARY KEY,
                        job_id INT NOT NULL,
                        skill_name VARCHAR(255) NOT NULL,
                        FOREIGN KEY (job_id) REFERENCES job_details (id) ON DELETE CASCADE
);
