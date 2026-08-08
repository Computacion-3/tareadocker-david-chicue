package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Assignment;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.AssignmentId;
import edu.co.icesi.proyectofinal.repository.AssignmentRepository;
import edu.co.icesi.proyectofinal.services.impl.AssignmentServiceImpl;
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
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    private Assignment assignment;
    private AssignmentId assignmentId;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setIdUser(1);

        User trainer = new User();
        trainer.setIdUser(2);

        assignmentId = new AssignmentId();
        assignmentId.setUserId(1);
        assignmentId.setTrainerId(2);

        assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setUserAssignment(user);
        assignment.setTrainerAssignment(trainer);
        assignment.setAssignmentDate(LocalDate.now());
    }

    @Test
    void save_shouldReturnSavedAssignment() {
        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        Assignment result = assignmentService.save(assignment);

        assertNotNull(result);
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void deleteById_shouldCallRepository() {
        doNothing().when(assignmentRepository).deleteById(assignmentId);

        assignmentService.deleteById(assignmentId);

        verify(assignmentRepository).deleteById(assignmentId);
    }

    @Test
    void getById_whenExists_shouldReturnAssignment() {
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        Assignment result = assignmentService.getById(assignmentId);

        assertNotNull(result);
        assertEquals(assignmentId, result.getId());
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> assignmentService.getById(assignmentId));
    }

    @Test
    void findAll_shouldReturnList() {
        when(assignmentRepository.findAll()).thenReturn(List.of(assignment));

        List<Assignment> result = assignmentService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void getByUserId_shouldReturnFilteredList() {
        when(assignmentRepository.findByUserAssignmentIdUser(1)).thenReturn(List.of(assignment));

        List<Assignment> result = assignmentService.getByUserId(1);

        assertEquals(1, result.size());
    }

    @Test
    void getByTrainerId_shouldReturnFilteredList() {
        when(assignmentRepository.findByTrainerAssignmentIdUser(2)).thenReturn(List.of(assignment));

        List<Assignment> result = assignmentService.getByTrainerId(2);

        assertEquals(1, result.size());
    }

    @Test
    void update_whenExists_shouldUpdateAndReturn() {
        Assignment updated = new Assignment();
        updated.setAssignmentDate(LocalDate.now().plusDays(5));
        updated.setUserAssignment(assignment.getUserAssignment());
        updated.setTrainerAssignment(assignment.getTrainerAssignment());

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenAnswer(x -> x.getArgument(0));

        Assignment result = assignmentService.update(assignmentId, updated);

        assertEquals(updated.getAssignmentDate(), result.getAssignmentDate());
    }

    @Test
    void update_whenNotExists_shouldThrowRuntimeException() {
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> assignmentService.update(assignmentId, assignment));
    }

    @Test
    void isUserAssigned_whenExists_shouldReturnTrue() {
        when(assignmentRepository.existsByUserAssignmentIdUser(1)).thenReturn(true);

        assertTrue(assignmentService.isUserAssigned(1));
    }

    @Test
    void isUserAssigned_whenNotExists_shouldReturnFalse() {
        when(assignmentRepository.existsByUserAssignmentIdUser(99)).thenReturn(false);

        assertFalse(assignmentService.isUserAssigned(99));
    }
}