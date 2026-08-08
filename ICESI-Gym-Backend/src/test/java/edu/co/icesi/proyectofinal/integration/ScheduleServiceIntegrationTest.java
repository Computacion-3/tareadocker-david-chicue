package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Schedule;
import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.repository.ActivityRepository;
import edu.co.icesi.proyectofinal.repository.ScheduleRepository;
import edu.co.icesi.proyectofinal.repository.SpaceRepository;
import edu.co.icesi.proyectofinal.services.ScheduleService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ScheduleServiceIntegrationTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    private Activity activity;

    @BeforeEach
    void setUp() {

        Space space = new Space();
        space.setName("Gym");
        space.setCapacity(30);
        space.setLocation("Bloque A");
        spaceRepository.save(space);

        activity = new Activity();
        activity.setName("Yoga");
        activity.setStartDate(LocalDate.now());
        activity.setEndDate(LocalDate.now().plusMonths(1));
        activity.setSpace(space);
        activityRepository.save(activity);
    }

    private Schedule buildSchedule(String day) {
        Schedule s = new Schedule();
        s.setActivity(activity);
        s.setDayOfWeek(day);
        s.setStartTime(LocalTime.of(8, 0));
        s.setEndTime(LocalTime.of(9, 0));
        return s;
    }

    @Test
    void saveSchedule_shouldPersist() {
        Schedule saved = scheduleService.saveSchedule(buildSchedule("LUNES"));

        assertNotNull(saved.getIdSchedule());
    }

    @Test
    void getSchedule_whenExists_shouldReturn() {
        Schedule saved = scheduleRepository.save(buildSchedule("MARTES"));

        Schedule result = scheduleService.getSchedule(saved.getIdSchedule());

        assertEquals("MARTES", result.getDayOfWeek());
    }

    @Test
    void getSchedule_whenNotExists_shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> scheduleService.getSchedule(999));
    }

    @Test
    void updateSchedule_shouldReflectChanges() {
        Schedule saved = scheduleRepository.save(buildSchedule("LUNES"));
        saved.setDayOfWeek("VIERNES");
        saved.setStartTime(LocalTime.of(17, 0));
        saved.setEndTime(LocalTime.of(18, 0));

        Schedule result = scheduleService.updateSchedule(saved);

        assertEquals("VIERNES", result.getDayOfWeek());
    }

    @Test
    void deleteSchedule_shouldRemove() {
        Schedule saved = scheduleRepository.save(buildSchedule("LUNES"));

        scheduleService.deleteSchedule(saved.getIdSchedule());

        assertTrue(scheduleRepository.findAll().isEmpty());
    }

    @Test
    void getByActivityId_shouldReturnFiltered() {
        scheduleRepository.save(buildSchedule("LUNES"));
        scheduleRepository.save(buildSchedule("MIERCOLES"));

        List<Schedule> result = scheduleService.getByActivityId(activity.getIdActivity());

        assertEquals(2, result.size());
    }

    @Test
    void getByActivityName_shouldReturnFiltered() {
        scheduleRepository.save(buildSchedule("JUEVES"));

        List<Schedule> result = scheduleService.getByActivityName("Yoga");

        assertEquals(1, result.size());
    }
}