package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Enrollment;
import edu.co.icesi.proyectofinal.entity.keys.EnrollmentId;
import edu.co.icesi.proyectofinal.repository.EnrollmentRepository;
import edu.co.icesi.proyectofinal.services.EnrollmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getEnrollmentDate() == null) {
            enrollment.setEnrollmentDate(LocalDate.now());
        }

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteById(EnrollmentId id) {
        enrollmentRepository.deleteById(id);
    }

    @Override
    public Enrollment getById(EnrollmentId id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> getByUserId(Integer userId) {
        return enrollmentRepository.findByUserIdUser(userId);
    }

    @Override
    public List<Enrollment> getByActivityId(Integer activityId) {
        return enrollmentRepository.findByActivityIdActivity(activityId);
    }

    @Override
    public List<Enrollment> getByActivityName(String activityName) {
        return enrollmentRepository.findByActivityName(activityName);
    }

    @Override
    public Enrollment update(EnrollmentId id, Enrollment updated) {
        Enrollment existing = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        existing.setEnrollmentDate(updated.getEnrollmentDate());
        existing.setUser(updated.getUser());
        existing.setActivity(updated.getActivity());
        return enrollmentRepository.save(existing);
    }

    @Override
    public long countByActivityId(Integer activityId) {
        return enrollmentRepository.countByActivityIdActivity(activityId);
    }
}