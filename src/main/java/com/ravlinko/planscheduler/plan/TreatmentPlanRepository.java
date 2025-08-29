package com.ravlinko.planscheduler.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, UUID> {
    @Query("SELECT p FROM TreatmentPlan p WHERE CAST(p.startTime AS date ) <= :today AND CAST(p.endTime AS date) >= :today")
    List<TreatmentPlan> findPlansForDay(@Param("today") Date today);
}
