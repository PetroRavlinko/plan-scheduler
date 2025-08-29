package com.ravlinko.planscheduler.plan;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
public class TreatmentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private TreatmentAction treatmentAction;
    private String subjectPatient;
    private Timestamp startTime;
    private Timestamp endTime;
    private String recurrencePattern;
}
