CREATE TABLE treatment_plan
(
    id UUID PRIMARY KEY,
    treatment_action   VARCHAR(255),
    subject_patient    VARCHAR(255),
    start_time         TIMESTAMP,
    end_time           TIMESTAMP,
    recurrence_pattern VARCHAR(255)
);

CREATE TABLE treatment_task
(
    id UUID PRIMARY KEY,
    start_time TIMESTAMP,
    status     VARCHAR(255),
    treatment_plan_id UUID,
    CONSTRAINT fk_treatment_plan
        FOREIGN KEY (treatment_plan_id)
            REFERENCES treatment_plan (id)
);