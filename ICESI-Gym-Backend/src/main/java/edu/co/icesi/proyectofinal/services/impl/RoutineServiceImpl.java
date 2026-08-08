package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.entity.Routine;
import edu.co.icesi.proyectofinal.entity.RoutineExercise;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.RoutineExerciseId;
import edu.co.icesi.proyectofinal.repository.ExerciseRepository;
import edu.co.icesi.proyectofinal.repository.RoutineRepository;
import edu.co.icesi.proyectofinal.services.RoutineService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    public RoutineServiceImpl(RoutineRepository routineRepository,
                              ExerciseRepository exerciseRepository) {
        this.routineRepository = routineRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    @Transactional
    public void assignExercises(Integer routineId, List<Integer> exerciseIds) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine with id " + routineId + " not found"));

        routine.getRoutineExercises().clear();

        if (exerciseIds != null) {
            int order = 1;
            for (Integer exerciseId : exerciseIds) {
                Exercise exercise = exerciseRepository.findById(exerciseId)
                        .orElseThrow(() -> new RuntimeException("Exercise with id " + exerciseId + " not found"));

                RoutineExerciseId reId = new RoutineExerciseId();
                reId.setRoutineId(routineId);
                reId.setExerciseId(exerciseId);

                RoutineExercise re = new RoutineExercise();
                re.setId(reId);
                re.setRoutine(routine);
                re.setExercise(exercise);
                re.setExerciseOrder(order++);

                routine.getRoutineExercises().add(re);
            }
        }

        routineRepository.save(routine);
    }

    @Override
    public List<Routine> findAll() {
        return routineRepository.findAll();
    }

    @Override
    public Routine findById(Integer id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routine with id " + id + " not found"));
    }

    @Override
    public Routine save(Routine routine) {

        if(routine.getCreationDate() == null){
            routine.setCreationDate(LocalDate.now());
        }

        return routineRepository.save(routine);
    }

    @Override
    public void delete(Integer id) {

        if(!routineRepository.existsById(id)){
            throw new RuntimeException("Routine with id " + id +
                    " not found");
        }

        routineRepository.deleteById(id);
    }

    @Override
    public Routine update(Integer id, Routine updated) {
        Routine existing = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routine with id " + id + " not found"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCreationDate(updated.getCreationDate());
        existing.setPredesigned(updated.isPredesigned());
        existing.setUserRoutine(updated.getUserRoutine());

        if(updated.getCreationDate() == null){
            existing.setCreationDate(LocalDate.now());
        }else{
            existing.setCreationDate(updated.getCreationDate());
        }

        return routineRepository.save(existing);
    }

    @Override
    public List<Routine> findByUserId(Integer userId) {
        return routineRepository.findByUserRoutineIdUser(userId);
    }

    @Override
    public List<Routine> findPredesigned() {
        return routineRepository.findByPredesigned(true);
    }

    @Override
    @Transactional
    public Routine adoptRoutine(Integer routineId, User targetUser) {
        Routine source = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        String adoptedName = source.getName() + " (Adoptada)";
        
        if (routineRepository.existsByNameAndUserRoutineIdUser(adoptedName, targetUser.getIdUser())) {
            throw new RuntimeException("Ya has adoptado esta rutina anteriormente.");
        }

        Routine copy = new Routine();
        copy.setName(adoptedName);
        copy.setDescription(source.getDescription());
        copy.setCreationDate(LocalDate.now());
        copy.setPredesigned(false);
        copy.setUserRoutine(targetUser);
        copy.setRoutineExercises(new ArrayList<>());

        Routine savedCopy = routineRepository.save(copy);

        for (RoutineExercise re : source.getRoutineExercises()) {
            RoutineExercise copyRe = new RoutineExercise();
            RoutineExerciseId id = new RoutineExerciseId();
            id.setRoutineId(savedCopy.getIdRoutine());
            id.setExerciseId(re.getExercise().getIdExercise());

            copyRe.setId(id);
            copyRe.setRoutine(savedCopy);
            copyRe.setExercise(re.getExercise());
            copyRe.setSets(re.getSets());
            copyRe.setTargetReps(re.getTargetReps());
            copyRe.setExerciseOrder(re.getExerciseOrder());

            savedCopy.getRoutineExercises().add(copyRe);
        }

        return routineRepository.save(savedCopy);
    }
}