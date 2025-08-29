package com.ravlinko.planscheduler;

import com.ravlinko.planscheduler.plan.TreatmentAction;
import com.ravlinko.planscheduler.plan.TreatmentPlan;
import com.ravlinko.planscheduler.plan.TreatmentPlanRepository;
import com.ravlinko.planscheduler.task.TreatmentTaskRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {
    @Mock
    private TreatmentPlanRepository treatmentPlanRepository;
    @Mock
    private TreatmentTaskRepository treatmentTaskRepository;

    private SchedulerService schedulerService;

    @BeforeAll
    static void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @BeforeEach
    void setUp() {
        schedulerService = new SchedulerService(treatmentPlanRepository, treatmentTaskRepository);
    }

    @Test
    @DisplayName("Should return empty list when there are no plans for today")
    void testScheduleTasks_nothingToDo() {
        // When
        var tasks = schedulerService.createChunkOfTodayTreatmentTasks();
        // Then
        assertThat(tasks, hasSize(0));
    }

    @ParameterizedTest(name = "Cron: {0} -> Expected tasks: {1}")
    @CsvSource({
            "'0 0 13 * * *', 1",
            "'0 0 */6 * * *', 3",
            "'0 0 */3 * * *', 7",
            "'0 */30 * * * *', 47"
    })
    @DisplayName("Should schedule correct number of tasks for various cron patterns")
    void testScheduleTasks_parameterized(String cron, int expectedTaskCount) {
        // Given
        given(treatmentPlanRepository.findPlansForDay(any())).willReturn(List.of(
                new TreatmentPlan(
                        UUID.randomUUID(),
                        TreatmentAction.ACTION_A,
                        null,
                        Timestamp.valueOf(LocalDate.now().atStartOfDay()),
                        Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay()),
                        cron
                )
        ));
        // When
        var tasks = schedulerService.createChunkOfTodayTreatmentTasks();
        // Then
        assertThat(tasks, hasSize(expectedTaskCount));
    }

    @Test
    @DisplayName("Should not schedule tasks if they already exist for the plan and time")
    void testScheduleTasks_tasksAlreadyExists() {
        // Given
        given(treatmentPlanRepository.findPlansForDay(any())).willReturn(List.of(
                new TreatmentPlan(
                        UUID.randomUUID(),
                        TreatmentAction.ACTION_A,
                        null,
                        Timestamp.valueOf(LocalDate.now().atStartOfDay()),
                        Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay()),
                        "0 0 */6 * * *"
                )
        ));

        given(treatmentTaskRepository.existsByStartTimeAndTreatmentPlan(any(Timestamp.class), any(TreatmentPlan.class))).willReturn(true);
        // When
        var tasks = schedulerService.createChunkOfTodayTreatmentTasks();
        // Then
        assertThat(tasks, hasSize(0));
    }

    @Test
    @DisplayName("Should schedule only missing tasks if some already exist for the plan and time")
    void testScheduleTasks_tasksAlreadyPartiallyExists() {
        // Given
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        given(treatmentPlanRepository.findPlansForDay(any())).willReturn(List.of(
                new TreatmentPlan(
                        UUID.randomUUID(),
                        TreatmentAction.ACTION_A,
                        null,
                        Timestamp.valueOf(localDateTime),
                        Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay()),
                        "0 0 */7 * * *"
                )
        ));

        given(treatmentTaskRepository.existsByStartTimeAndTreatmentPlan(
                eq(Timestamp.valueOf(localDateTime.plusHours(7))),
                any(TreatmentPlan.class)))
                .willReturn(true);
        given(treatmentTaskRepository.existsByStartTimeAndTreatmentPlan(
                eq(Timestamp.valueOf(localDateTime.plusHours(14))),
                any(TreatmentPlan.class)))
                .willReturn(false);
        given(treatmentTaskRepository.existsByStartTimeAndTreatmentPlan(
                eq(Timestamp.valueOf(localDateTime.plusHours(21))),
                any(TreatmentPlan.class)))
                .willReturn(true);
        // When
        var tasks = schedulerService.createChunkOfTodayTreatmentTasks();
        // Then
        assertThat(tasks, hasSize(1));
    }
}