package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Assignment;
import edu.co.icesi.proyectofinal.entity.keys.AssignmentId;
import edu.co.icesi.proyectofinal.repository.AssignmentRepository;
import edu.co.icesi.proyectofinal.services.AssignmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public Assignment save(Assignment assignment) {
        if (assignment.getAssignmentDate() == null) {
            assignment.setAssignmentDate(LocalDate.now());
        }
        return assignmentRepository.save(assignment);
    }

    @Override
    public void deleteById(AssignmentId id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public Assignment getById(AssignmentId id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    @Override
    public List<Assignment> findAll() {
        return assignmentRepository.findAll();
    }

    @Override
    public List<Assignment> getByUserId(Integer userId) {
        return assignmentRepository.findByUserAssignmentIdUser(userId);
    }

    @Override
    public List<Assignment> getByTrainerId(Integer trainerId) {
        return assignmentRepository.findByTrainerAssignmentIdUser(trainerId);
    }

    @Override
    public Assignment update(AssignmentId id, Assignment updated) {
        Assignment existing = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        existing.setAssignmentDate(updated.getAssignmentDate());
        existing.setUserAssignment(updated.getUserAssignment());
        existing.setTrainerAssignment(updated.getTrainerAssignment());
        return assignmentRepository.save(existing);
    }

    @Override
    public boolean isUserAssigned(Integer userId) {
        return assignmentRepository.existsByUserAssignmentIdUser(userId);
    }
}