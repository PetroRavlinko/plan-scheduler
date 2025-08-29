package com.ravlinko.planscheduler.task;

import com.ravlinko.planscheduler.plan.TreatmentPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class TreatmentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Timestamp startTime;
    @Enumerated(EnumType.STRING)
    private TreatmentTaskStatus status;

    @ManyToOne
    private TreatmentPlan treatmentPlan;
}
