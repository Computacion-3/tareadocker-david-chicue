package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.repository.ExerciseRepository;
import edu.co.icesi.proyectofinal.services.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise();
        exercise.setIdExercise(1);
        exercise.setName("Push Up");
        exercise.setType("fuerza");
        exercise.setDifficulty("medio");
        exercise.setDurationMin(15);
        exercise.setPredefined(true);
    }

    @Test
    void findAll_shouldReturnList() {
        when(exerciseRepository.findAll()).thenReturn(List.of(exercise));

        assertEquals(1, exerciseService.findAll().size());
    }

    @Test
    void getExerciseById_whenExists_shouldReturn() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getExerciseById(1);

        assertNotNull(result);
        assertEquals("Push Up", result.getName());
    }

    @Test
    void getExerciseById_whenNotExists_shouldThrowRuntimeException() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.getExerciseById(99));
    }

    @Test
    void saveExercise_shouldReturnSaved() {
        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        Exercise result = exerciseService.saveExercise(exercise);

        assertNotNull(result);
        assertEquals("Push Up", result.getName());
    }

    @Test
    void updateExercise_whenExists_shouldUpdateAndReturn() {
        Exercise updated = new Exercise();
        updated.setIdExercise(1);
        updated.setName("Pull Up");
        updated.setType("fuerza");
        updated.setDifficulty("difícil");
        updated.setDurationMin(20);
        updated.setPredefined(false);

        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(Exercise.class))).thenAnswer(x -> x.getArgument(0));

        Exercise result = exerciseService.updateExercise(updated);

        assertEquals("Pull Up", result.getName());
        assertEquals("difícil", result.getDifficulty());
    }

    @Test
    void updateExercise_whenNotExists_shouldThrowRuntimeException() {
        exercise.setIdExercise(99);
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.updateExercise(exercise));
    }

    @Test
    void deleteExercise_shouldCallRepository() {
        when(exerciseRepository.existsById(1)).thenReturn(true);
        doNothing().when(exerciseRepository).deleteById(1);
        exerciseService.deleteExercise(1);
        verify(exerciseRepository).deleteById(1);
    }


    @Test
    void findByType_shouldReturnFiltered() {
        when(exerciseRepository.findByType("fuerza")).thenReturn(List.of(exercise));

        assertEquals(1, exerciseService.findByType("fuerza").size());
    }

    @Test
    void findByDifficulty_shouldReturnFiltered() {
        when(exerciseRepository.findByDifficulty("medio")).thenReturn(List.of(exercise));

        assertEquals(1, exerciseService.findByDifficulty("medio").size());
    }

    @Test
    void findPredefined_shouldReturnOnlyPredefined() {
        when(exerciseRepository.findByPredefined(true)).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.findPredefined();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isPredefined());
    }

    @Test
    void findByUserId_shouldReturnUserExercises() {
        when(exerciseRepository.findByUserExerciseIdUser(5)).thenReturn(List.of(exercise));

        assertEquals(1, exerciseService.findByUserId(5).size());
    }
}