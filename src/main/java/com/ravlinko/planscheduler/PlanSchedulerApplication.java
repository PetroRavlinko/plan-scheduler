package com.ravlinko.planscheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.TimeZone;


@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@SpringBootApplication
public class PlanSchedulerApplication {
    private final SchedulerService schedulerService;

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(PlanSchedulerApplication.class, args);
    }

    @Scheduled(fixedRate = 5000)
    public void runFixedRateTask() {
        var tasks = schedulerService.createChunkOfTodayTreatmentTasks();
        tasks.forEach(task -> log.debug("Scheduled task {}", task.getId()));
    }

}
