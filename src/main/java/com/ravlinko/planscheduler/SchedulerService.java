package com.ravlinko.planscheduler;

import com.ravlinko.planscheduler.plan.TreatmentPlan;
import com.ravlinko.planscheduler.plan.TreatmentPlanRepository;
import com.ravlinko.planscheduler.task.TreatmentTask;
import com.ravlinko.planscheduler.task.TreatmentTaskRepository;
import com.ravlinko.planscheduler.task.TreatmentTaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {
    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentTaskRepository treatmentTaskRepository;

    public Collection<TreatmentTask> createChunkOfTodayTreatmentTasks() {
        var localDateStartTime = LocalDate.now().atStartOfDay();
        var todayStart = Date.from(localDateStartTime.toInstant(ZoneOffset.UTC));
        var todayPlans = treatmentPlanRepository.findPlansForDay(todayStart);

        return todayPlans.stream()
                .filter(p -> CronExpression.isValidExpression(p.getRecurrencePattern()))
                .filter(p -> Objects.requireNonNull(CronExpression.parse(p.getRecurrencePattern()).next(localDateStartTime)).isBefore(localDateStartTime.plusDays(1)))
                .flatMap(p -> {
                    log.info("Getting tasks for plan {}", p.getSubjectPatient());
                    var startTime = localDateStartTime;
                    var slots = new ArrayList<Timestamp>();
                    do {
                        startTime = CronExpression.parse(p.getRecurrencePattern()).next(startTime);
                        if (startTime != null && startTime.isBefore(localDateStartTime.plusDays(1))) {
                            slots.add(Timestamp.from(startTime.toInstant(ZoneOffset.UTC)));
                        }
                    } while (startTime != null && startTime.isBefore(localDateStartTime.plusDays(1)));
                    return slots.stream()
                            .filter(s -> isNew(s, p))
                            .map(s -> treatmentTaskRepository.save(new TreatmentTask(null, s, TreatmentTaskStatus.ACTIVE, p)));
                })
                .toList();
    }

    private boolean isNew(Timestamp time, TreatmentPlan treatmentPlan) {
        boolean answer = treatmentTaskRepository.existsByStartTimeAndTreatmentPlan(time, treatmentPlan);
        if (answer) log.warn("Existed task for {} of {}", time, treatmentPlan.getId());
        else log.info("New task for {} of {}", time, treatmentPlan.getId());
        return !answer;
    }
}
