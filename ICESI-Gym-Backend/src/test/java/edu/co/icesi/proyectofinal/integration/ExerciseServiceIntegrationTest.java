package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.repository.ExerciseRepository;
import edu.co.icesi.proyectofinal.services.ExerciseService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ExerciseServiceIntegrationTest {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @BeforeEach
    void setUp() {
    }

    private Exercise buildExercise(String name, String type, String difficulty, boolean predefined) {
        Exercise e = new Exercise();
        e.setName(name);
        e.setType(type);
        e.setDifficulty(difficulty);
        e.setDurationMin(20);
        e.setPredefined(predefined);
        return e;
    }

    @Test
    void saveExercise_shouldPersist() {
        Exercise saved = exerciseService.saveExercise(
                buildExercise("Push Up", "fuerza", "medio", true));

        assertNotNull(saved.getIdExercise());
    }

    @Test
    void getExerciseById_whenExists_shouldReturn() {
        Exercise saved = exerciseRepository.save(
                buildExercise("Burpee", "cardio", "alto", true));

        Exercise result = exerciseService.getExerciseById(saved.getIdExercise());

        assertEquals("Burpee", result.getName());
    }

    @Test
    void getExerciseById_whenNotExists_shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> exerciseService.getExerciseById(999));
    }

    @Test
    void updateExercise_shouldReflectChanges() {
        Exercise saved = exerciseRepository.save(
                buildExercise("Plank", "fuerza", "bajo", false));

        saved.setName("Side Plank");
        saved.setDifficulty("medio");
        Exercise result = exerciseService.updateExercise(saved);

        assertEquals("Side Plank", result.getName());
        assertEquals("medio", result.getDifficulty());
    }

    @Test
    void deleteExercise_shouldRemove() {
        Exercise saved = exerciseRepository.save(
                buildExercise("Squat", "fuerza", "medio", true));

        exerciseService.deleteExercise(saved.getIdExercise());

        assertTrue(exerciseRepository.findAll().isEmpty());
    }

    @Test
    void findByType_shouldReturnFiltered() {
        exerciseRepository.save(buildExercise("Run", "cardio", "bajo", true));
        exerciseRepository.save(buildExercise("Push Up", "fuerza", "medio", true));

        List<Exercise> result = exerciseService.findByType("cardio");

        assertEquals(1, result.size());
        assertEquals("Run", result.get(0).getName());
    }

    @Test
    void findPredefined_shouldReturnOnlyPredefined() {
        exerciseRepository.save(buildExercise("A", "cardio", "bajo", true));
        exerciseRepository.save(buildExercise("B", "fuerza", "medio", false));

        List<Exercise> result = exerciseService.findPredefined();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isPredefined());
    }
}