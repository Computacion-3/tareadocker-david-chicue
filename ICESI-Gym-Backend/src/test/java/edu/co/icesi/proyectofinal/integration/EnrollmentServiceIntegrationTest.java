package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Enrollment;
import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.EnrollmentId;
import edu.co.icesi.proyectofinal.repository.ActivityRepository;
import edu.co.icesi.proyectofinal.repository.EnrollmentRepository;
import edu.co.icesi.proyectofinal.repository.SpaceRepository;
import edu.co.icesi.proyectofinal.repository.UserRepository;
import edu.co.icesi.proyectofinal.services.EnrollmentService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class EnrollmentServiceIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    private User user;
    private Activity activity;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setFirstName("Luis");
        user.setLastName("Cano");
        user.setInstitutionalEmail("luis.cano@icesi.edu.co");
        user.setPassword("pass");
        userRepository.save(user);

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

    private Enrollment buildEnrollment() {
        EnrollmentId id = new EnrollmentId();

        Enrollment e = new Enrollment();
        e.setId(id);
        e.setUser(user);
        e.setActivity(activity);
        e.setEnrollmentDate(LocalDate.now());
        return e;
    }

    @Test
    void save_shouldPersist() {
        Enrollment saved = enrollmentService.save(buildEnrollment());

        assertNotNull(saved.getId());
        assertEquals(1, enrollmentRepository.findAll().size());
    }

    @Test
    void getById_whenExists_shouldReturn() {
        Enrollment saved = enrollmentRepository.save(buildEnrollment());

        Enrollment result = enrollmentService.getById(saved.getId());

        assertNotNull(result);
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        EnrollmentId id = new EnrollmentId();
        assertThrows(RuntimeException.class, () -> enrollmentService.getById(id));
    }

    @Test
    void deleteById_shouldRemove() {
        Enrollment saved = enrollmentRepository.save(buildEnrollment());

        enrollmentService.deleteById(saved.getId());

        assertTrue(enrollmentRepository.findAll().isEmpty());
    }

    @Test
    void findAll_shouldReturnAll() {
        enrollmentRepository.save(buildEnrollment());

        assertEquals(1, enrollmentService.findAll().size());
    }

    @Test
    void getByUserId_shouldReturnFiltered() {
        enrollmentRepository.save(buildEnrollment());

        List<Enrollment> result = enrollmentService.getByUserId(user.getIdUser());

        assertEquals(1, result.size());
    }

    @Test
    void getByActivityId_shouldReturnFiltered() {
        enrollmentRepository.save(buildEnrollment());

        List<Enrollment> result = enrollmentService.getByActivityId(activity.getIdActivity());

        assertEquals(1, result.size());
    }

    @Test
    void getByActivityName_shouldReturnFiltered() {
        enrollmentRepository.save(buildEnrollment());

        List<Enrollment> result = enrollmentService.getByActivityName("Yoga");

        assertEquals(1, result.size());
    }

    @Test
    void countByActivityId_shouldReturnCorrectCount() {
        enrollmentRepository.save(buildEnrollment());

        long count = enrollmentService.countByActivityId(activity.getIdActivity());

        assertEquals(1L, count);
    }
}