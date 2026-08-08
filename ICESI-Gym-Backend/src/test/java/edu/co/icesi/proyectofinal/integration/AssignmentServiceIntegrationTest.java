package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Assignment;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.AssignmentId;
import edu.co.icesi.proyectofinal.repository.AssignmentRepository;
import edu.co.icesi.proyectofinal.repository.UserRepository;
import edu.co.icesi.proyectofinal.services.AssignmentService;
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
class AssignmentServiceIntegrationTest {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User trainer;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setFirstName("Carlos");
        user.setLastName("Ruiz");
        user.setInstitutionalEmail("carlos.ruiz@icesi.edu.co");
        user.setPassword("pass");
        userRepository.save(user);

        trainer = new User();
        trainer.setFirstName("Sofia");
        trainer.setLastName("Paz");
        trainer.setInstitutionalEmail("sofia.paz@icesi.edu.co");
        trainer.setPassword("pass");
        userRepository.save(trainer);
    }

    private Assignment buildAssignment() {
        AssignmentId id = new AssignmentId();
        id.setUserId(user.getIdUser());
        id.setTrainerId(trainer.getIdUser());

        Assignment a = new Assignment();
        a.setId(id);
        a.setUserAssignment(user);
        a.setTrainerAssignment(trainer);
        a.setAssignmentDate(LocalDate.now());
        return a;
    }

    @Test
    void save_shouldPersist() {
        Assignment saved = assignmentService.save(buildAssignment());

        assertNotNull(saved.getId());
        assertEquals(1, assignmentRepository.findAll().size());
    }

    @Test
    void getById_whenExists_shouldReturn() {
        Assignment saved = assignmentRepository.save(buildAssignment());

        Assignment result = assignmentService.getById(saved.getId());

        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        AssignmentId id = new AssignmentId();
        id.setUserId(999);
        id.setTrainerId(999);

        assertThrows(RuntimeException.class, () -> assignmentService.getById(id));
    }

    @Test
    void deleteById_shouldRemove() {
        Assignment saved = assignmentRepository.save(buildAssignment());

        assignmentService.deleteById(saved.getId());

        assertTrue(assignmentRepository.findAll().isEmpty());
    }

    @Test
    void findAll_shouldReturnAll() {
        assignmentRepository.save(buildAssignment());

        assertEquals(1, assignmentService.findAll().size());
    }

    @Test
    void getByUserId_shouldReturnFiltered() {
        assignmentRepository.save(buildAssignment());

        List<Assignment> result = assignmentService.getByUserId(user.getIdUser());

        assertEquals(1, result.size());
    }

    @Test
    void getByTrainerId_shouldReturnFiltered() {
        assignmentRepository.save(buildAssignment());

        List<Assignment> result = assignmentService.getByTrainerId(trainer.getIdUser());

        assertEquals(1, result.size());
    }

    @Test
    void isUserAssigned_whenAssigned_shouldReturnTrue() {
        assignmentRepository.save(buildAssignment());

        assertTrue(assignmentService.isUserAssigned(user.getIdUser()));
    }

    @Test
    void isUserAssigned_whenNotAssigned_shouldReturnFalse() {
        assertFalse(assignmentService.isUserAssigned(999));
    }

    @Test
    void update_shouldReflectChanges() {
        Assignment saved = assignmentRepository.save(buildAssignment());

        Assignment updated = buildAssignment();
        updated.setAssignmentDate(LocalDate.now().plusDays(10));

        Assignment result = assignmentService.update(saved.getId(), updated);

        assertEquals(LocalDate.now().plusDays(10), result.getAssignmentDate());
    }
}