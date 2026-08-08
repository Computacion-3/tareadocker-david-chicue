package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.entity.Routine;
import edu.co.icesi.proyectofinal.entity.RoutineExercise;
import edu.co.icesi.proyectofinal.entity.keys.RoutineExerciseId;
import edu.co.icesi.proyectofinal.repository.ExerciseRepository;
import edu.co.icesi.proyectofinal.repository.RoutineExerciseRepository;
import edu.co.icesi.proyectofinal.repository.RoutineRepository;
import edu.co.icesi.proyectofinal.services.RoutineExerciseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoutineExerciseServiceImpl implements RoutineExerciseService {

    private final RoutineExerciseRepository routineExerciseRepository;
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    public RoutineExerciseServiceImpl(RoutineExerciseRepository routineExerciseRepository, 
                                     RoutineRepository routineRepository,
                                     ExerciseRepository exerciseRepository) {
        this.routineExerciseRepository = routineExerciseRepository;
        this.routineRepository = routineRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public RoutineExercise save(RoutineExercise routineExercise) {
        return routineExerciseRepository.save(routineExercise);
    }

    @Override
    public void deleteById(RoutineExerciseId id) {

        if(!routineExerciseRepository.existsById(id)){
            throw new RuntimeException("Theres no routine associated with id " + id +
                    " not found");
        }

        routineExerciseRepository.deleteById(id);
    }

    @Override
    public RoutineExercise getById(RoutineExerciseId id) {
        return routineExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "RoutineExercise with routineId " + id.getRoutineId()
                                + " and exerciseId " + id.getExerciseId() + " not found"));
    }

    @Override
    public List<RoutineExercise> findAll() {
        return routineExerciseRepository.findAll();
    }

    @Override
    public List<RoutineExercise> getByRoutineId(Integer routineId) {
        return routineExerciseRepository.findByRoutineIdRoutine(routineId);
    }

    @Override
    public List<RoutineExercise> getByExerciseId(Integer exerciseId) {
        return routineExerciseRepository.findByExerciseIdExercise(exerciseId);
    }

    @Override
    public RoutineExercise update(RoutineExerciseId id, RoutineExercise updated) {
        RoutineExercise existing = routineExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "RoutineExercise with routineId " + id.getRoutineId()
                                + " and exerciseId " + id.getExerciseId() + " not found"));
        existing.setSets(updated.getSets());
        existing.setTargetReps(updated.getTargetReps());
        existing.setExerciseOrder(updated.getExerciseOrder());
        return routineExerciseRepository.save(existing);
    }

    @Override
    public long countByRoutineId(Integer routineId) {
        return routineExerciseRepository.countByRoutineIdRoutine(routineId);
    }

    @Override
    @Transactional
    public List<RoutineExercise> assignExercisesToRoutine(Integer routineId, List<Integer> exerciseIds) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found with id: " + routineId));

        List<RoutineExercise> createdAssignments = new ArrayList<>();
        int order = 1;

        for (Integer exerciseId : exerciseIds) {
            Exercise exercise = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + exerciseId));

            RoutineExerciseId id = new RoutineExerciseId();
            id.setRoutineId(routineId);
            id.setExerciseId(exerciseId);

            RoutineExercise re = new RoutineExercise();
            re.setId(id);
            re.setRoutine(routine);
            re.setExercise(exercise);
            re.setSets(3); // Default values
            re.setTargetReps(10);
            re.setExerciseOrder(order++);

            createdAssignments.add(routineExerciseRepository.save(re));
        }

        return createdAssignments;
    }
}