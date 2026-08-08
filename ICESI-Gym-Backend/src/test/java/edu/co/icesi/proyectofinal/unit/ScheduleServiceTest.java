package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Schedule;
import edu.co.icesi.proyectofinal.repository.ScheduleRepository;
import edu.co.icesi.proyectofinal.services.impl.ScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        Activity activity = new Activity();
        activity.setIdActivity(1);
        activity.setName("Yoga");

        schedule = new Schedule();
        schedule.setIdSchedule(1);
        schedule.setActivity(activity);
        schedule.setDayOfWeek("LUNES");
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(9, 0));
    }

    @Test
    void getSchedules_shouldReturnList() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        assertEquals(1, scheduleService.getSchedules().size());
    }

    @Test
    void getSchedule_whenExists_shouldReturn() {
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));

        Schedule result = scheduleService.getSchedule(1);

        assertNotNull(result);
        assertEquals("LUNES", result.getDayOfWeek());
    }

    @Test
    void getSchedule_whenNotExists_shouldThrowRuntimeException() {
        when(scheduleRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> scheduleService.getSchedule(99));
    }

    @Test
    void saveSchedule_shouldReturnSaved() {
        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        Schedule result = scheduleService.saveSchedule(schedule);

        assertNotNull(result);
        assertEquals("LUNES", result.getDayOfWeek());
    }

    @Test
    void updateSchedule_whenExists_shouldUpdateAndReturn() {
        Schedule updated = new Schedule();
        updated.setIdSchedule(1);
        updated.setDayOfWeek("MARTES");
        updated.setStartTime(LocalTime.of(10, 0));
        updated.setEndTime(LocalTime.of(11, 0));
        updated.setActivity(schedule.getActivity());

        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(x -> x.getArgument(0));

        Schedule result = scheduleService.updateSchedule(updated);

        assertEquals("MARTES", result.getDayOfWeek());
        assertEquals(LocalTime.of(10, 0), result.getStartTime());
    }

    @Test
    void updateSchedule_whenNotExists_shouldThrowRuntimeException() {
        schedule.setIdSchedule(99);
        when(scheduleRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> scheduleService.updateSchedule(schedule));
    }

    @Test
    void deleteSchedule_shouldCallRepository() {
        when(scheduleRepository.existsById(1)).thenReturn(true);
        doNothing().when(scheduleRepository).deleteById(1);
        scheduleService.deleteSchedule(1);
        verify(scheduleRepository).deleteById(1);
    }

    @Test
    void getByActivityId_shouldReturnFiltered() {
        when(scheduleRepository.findByActivityIdActivity(1)).thenReturn(List.of(schedule));

        assertEquals(1, scheduleService.getByActivityId(1).size());
    }

    @Test
    void getByActivityName_shouldReturnFiltered() {
        when(scheduleRepository.findByActivityName("Yoga")).thenReturn(List.of(schedule));

        assertEquals(1, scheduleService.getByActivityName("Yoga").size());
    }
}