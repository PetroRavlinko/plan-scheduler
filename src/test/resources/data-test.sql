-- Two plans for today (no recurrence)
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_A', 'patient1', DATEADD('HOUR', 8, CURRENT_DATE), DATEADD('HOUR', 10, CURRENT_DATE), NULL);
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_B', 'patient2', DATEADD('HOUR', 12, CURRENT_DATE), DATEADD('HOUR', 14, CURRENT_DATE), NULL);

-- Two plans for tomorrow (daily recurrence at 9 AM and 1 PM)
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_A', 'patient3', DATEADD('HOUR', 9, DATEADD('DAY', 1, CURRENT_DATE)), DATEADD('HOUR', 11, DATEADD('DAY', 1, CURRENT_DATE)), '0 0 9 * * *');
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_B', 'patient4', DATEADD('HOUR', 13, DATEADD('DAY', 1, CURRENT_DATE)), DATEADD('HOUR', 15, DATEADD('DAY', 1, CURRENT_DATE)), '0 0 13 * * *');

-- Two plans for today and tomorrow (spanning both days, weekly recurrence)
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_A', 'patient5', DATEADD('HOUR', 16, CURRENT_DATE), DATEADD('HOUR', 10, DATEADD('DAY', 1, CURRENT_DATE)), '0 0 16 * * MON');
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_B', 'patient6', DATEADD('HOUR', 18, CURRENT_DATE), DATEADD('HOUR', 20, DATEADD('DAY', 1, CURRENT_DATE)), '0 0 18 * * MON');

-- Two plans that already ended (no recurrence)
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_A', 'patient7', DATEADD('HOUR', 8, DATEADD('DAY', -2, CURRENT_DATE)), DATEADD('HOUR', 10, DATEADD('DAY', -1, CURRENT_DATE)), NULL);
INSERT INTO treatment_plan (id, treatment_action, subject_patient, start_time, end_time, recurrence_pattern)
VALUES (RANDOM_UUID(), 'ACTION_B', 'patient8', DATEADD('HOUR', 12, DATEADD('DAY', -1, CURRENT_DATE)), DATEADD('HOUR', -1, CURRENT_DATE), NULL);