package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Exercise;

import java.util.List;

public interface ExerciseService {

    List<Exercise> findAll();

    Exercise getExerciseById(Integer id);

    Exercise saveExercise(Exercise exercise);

    Exercise updateExercise(Exercise exercise);

    void deleteExercise(Integer id);

    List<Exercise> findByType(String type);

    List<Exercise> findByDifficulty(String difficulty);

    List<Exercise> findPredefined();

    List<Exercise> findByUserId(Integer userId);
}