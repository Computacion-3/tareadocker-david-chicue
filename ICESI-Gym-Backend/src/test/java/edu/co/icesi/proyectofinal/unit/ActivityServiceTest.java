package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.repository.ActivityRepository;
import edu.co.icesi.proyectofinal.services.impl.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private edu.co.icesi.proyectofinal.services.NotificationService notificationService;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity activity;

    @BeforeEach
    void setUp() {
        Space space = new Space();
        space.setIdSpace(1);
        space.setName("Gym");

        activity = new Activity();
        activity.setIdActivity(1);
        activity.setName("Yoga");
        activity.setDescription("Morning yoga");
        activity.setStartDate(LocalDate.now());
        activity.setEndDate(LocalDate.now().plusMonths(1));
        activity.setSpace(space);
    }

    @Test
    void findAll_shouldReturnList() {
        when(activityRepository.findAll()).thenReturn(List.of(activity));

        List<Activity> result = activityService.findAll();

        assertEquals(1, result.size());
        verify(activityRepository).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnActivity() {
        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        Activity result = activityService.findById(1);

        assertNotNull(result);
        assertEquals("Yoga", result.getName());
    }

    @Test
    void findById_whenNotExists_shouldThrowRuntimeException() {
        when(activityRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.findById(99));
    }

    @Test
    void save_shouldReturnSavedActivity() {
        when(activityRepository.save(activity)).thenReturn(activity);

        Activity result = activityService.save(activity);

        assertNotNull(result);
        assertEquals("Yoga", result.getName());
        verify(activityRepository).save(activity);
    }

    @Test
    void update_whenExists_shouldUpdateAndReturn() {
        Activity updated = new Activity();
        updated.setIdActivity(1);
        updated.setName("Pilates");
        updated.setDescription("Evening pilates");
        updated.setStartDate(LocalDate.now());
        updated.setEndDate(LocalDate.now().plusMonths(2));
        updated.setSpace(activity.getSpace());

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenAnswer(x -> x.getArgument(0));

        Activity result = activityService.update(updated);

        assertEquals("Pilates", result.getName());
        assertEquals("Evening pilates", result.getDescription());
    }

    @Test
    void update_whenNotExists_shouldThrowRuntimeException() {
        activity.setIdActivity(99);
        when(activityRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> activityService.update(activity));
    }

    @Test
    void delete_shouldCallRepository() {
        when(activityRepository.existsById(1)).thenReturn(true);
        doNothing().when(activityRepository).deleteById(1);
        activityService.delete(1);
        verify(activityRepository).deleteById(1);
    }

    @Test
    void findBySpaceId_shouldReturnFilteredList() {
        when(activityRepository.findBySpaceIdSpace(1)).thenReturn(List.of(activity));

        List<Activity> result = activityService.findBySpaceId(1);

        assertEquals(1, result.size());
    }

    @Test
    void findByNameContaining_whenMatch_shouldReturnList() {
        when(activityRepository.findByNameContainingIgnoreCase("yoga")).thenReturn(List.of(activity));

        List<Activity> result = activityService.findByNameContaining("yoga");

        assertEquals(1, result.size());
        assertEquals("Yoga", result.get(0).getName());
    }

    @Test
    void findByNameContaining_whenNoMatch_shouldThrowRuntimeException() {
        when(activityRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> activityService.findByNameContaining("xyz"));
    }



}