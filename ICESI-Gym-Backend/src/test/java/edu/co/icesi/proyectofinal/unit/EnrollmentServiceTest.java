package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Enrollment;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.EnrollmentId;
import edu.co.icesi.proyectofinal.repository.EnrollmentRepository;
import edu.co.icesi.proyectofinal.services.impl.EnrollmentServiceImpl;
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
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Enrollment enrollment;
    private EnrollmentId enrollmentId;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setIdUser(1);

        Activity activity = new Activity();
        activity.setIdActivity(10);
        activity.setName("Yoga");

        enrollmentId = new EnrollmentId();

        enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        enrollment.setUser(user);
        enrollment.setActivity(activity);
        enrollment.setEnrollmentDate(LocalDate.now());
    }

    @Test
    void save_shouldReturnSavedEnrollment() {
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        Enrollment result = enrollmentService.save(enrollment);

        assertNotNull(result);
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void deleteById_shouldCallRepository() {
        doNothing().when(enrollmentRepository).deleteById(enrollmentId);

        enrollmentService.deleteById(enrollmentId);

        verify(enrollmentRepository).deleteById(enrollmentId);
    }

    @Test
    void getById_whenExists_shouldReturnEnrollment() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        Enrollment result = enrollmentService.getById(enrollmentId);

        assertNotNull(result);
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enrollmentService.getById(enrollmentId));
    }

    @Test
    void findAll_shouldReturnList() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        assertEquals(1, enrollmentService.findAll().size());
    }

    @Test
    void getByUserId_shouldReturnFilteredList() {
        when(enrollmentRepository.findByUserIdUser(1)).thenReturn(List.of(enrollment));

        assertEquals(1, enrollmentService.getByUserId(1).size());
    }

    @Test
    void getByActivityId_shouldReturnFilteredList() {
        when(enrollmentRepository.findByActivityIdActivity(10)).thenReturn(List.of(enrollment));

        assertEquals(1, enrollmentService.getByActivityId(10).size());
    }

    @Test
    void getByActivityName_shouldReturnFilteredList() {
        when(enrollmentRepository.findByActivityName("Yoga")).thenReturn(List.of(enrollment));

        List<Enrollment> result = enrollmentService.getByActivityName("Yoga");

        assertEquals(1, result.size());
    }

    @Test
    void getByActivityName_whenNoMatch_shouldReturnEmptyList() {
        when(enrollmentRepository.findByActivityName("Unknown")).thenReturn(List.of());

        assertTrue(enrollmentService.getByActivityName("Unknown").isEmpty());
    }

    @Test
    void update_whenExists_shouldUpdateAndReturn() {
        Enrollment updated = new Enrollment();
        updated.setEnrollmentDate(LocalDate.now().plusDays(3));
        updated.setUser(enrollment.getUser());
        updated.setActivity(enrollment.getActivity());

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(x -> x.getArgument(0));

        Enrollment result = enrollmentService.update(enrollmentId, updated);

        assertEquals(updated.getEnrollmentDate(), result.getEnrollmentDate());
    }

    @Test
    void update_whenNotExists_shouldThrowRuntimeException() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enrollmentService.update(enrollmentId, enrollment));
    }

    @Test
    void countByActivityId_shouldReturnCount() {
        when(enrollmentRepository.countByActivityIdActivity(10)).thenReturn(5L);

        assertEquals(5L, enrollmentService.countByActivityId(10));
    }
}