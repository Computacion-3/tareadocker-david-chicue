package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Assignment;
import edu.co.icesi.proyectofinal.entity.keys.AssignmentId;

import java.util.List;

public interface AssignmentService {

    Assignment save(Assignment assignment);

    void deleteById(AssignmentId id);

    Assignment getById(AssignmentId id);

    List<Assignment> findAll();

    List<Assignment> getByUserId(Integer userId);

    List<Assignment> getByTrainerId(Integer trainerId);

    Assignment update(AssignmentId id, Assignment updated);

    boolean isUserAssigned(Integer userId);
}
