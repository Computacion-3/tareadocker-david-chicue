package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Routine;

import java.util.List;

public interface RoutineService {

    void assignExercises(Integer routineId, List<Integer> exerciseIds);

    List<Routine> findAll();

    Routine findById(Integer id);

    Routine save(Routine routine);

    void delete(Integer id);

    Routine update(Integer id, Routine updated);

    List<Routine> findByUserId(Integer userId);

    List<Routine> findPredesigned();

    Routine adoptRoutine(Integer routineId, edu.co.icesi.proyectofinal.entity.User targetUser);
}
