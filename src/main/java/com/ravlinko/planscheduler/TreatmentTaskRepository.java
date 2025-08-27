package com.ravlinko.planscheduler;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.UUID;

public interface TreatmentTaskRepository extends JpaRepository<TreatmentTask, UUID> {
    boolean existsByStartTimeAndTreatmentPlan(Timestamp startTime, TreatmentPlan treatmentPlan);
}
