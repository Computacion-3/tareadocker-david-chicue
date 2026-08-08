package edu.co.icesi.proyectofinal.services;


import edu.co.icesi.proyectofinal.entity.Enrollment;
import edu.co.icesi.proyectofinal.entity.keys.EnrollmentId;

import java.util.List;

public interface EnrollmentService{

    Enrollment save(Enrollment enrollment);

    void deleteById(EnrollmentId id);

    Enrollment getById(EnrollmentId id);

    List<Enrollment> findAll();

    List<Enrollment> getByUserId(Integer userId);

    List<Enrollment> getByActivityId(Integer activityId);

    List<Enrollment> getByActivityName(String activityName);

    Enrollment update(EnrollmentId id, Enrollment updated);

    long countByActivityId(Integer activityId);
}
