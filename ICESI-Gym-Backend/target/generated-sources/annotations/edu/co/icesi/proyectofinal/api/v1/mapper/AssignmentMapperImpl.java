package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.AssignmentRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.AssignmentResponse;
import edu.co.icesi.proyectofinal.entity.Assignment;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.AssignmentId;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class AssignmentMapperImpl implements AssignmentMapper {

    @Override
    public Assignment toEntity(AssignmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Assignment assignment = new Assignment();

        assignment.setId( assignmentRequestToAssignmentId( request ) );
        assignment.setUserAssignment( assignmentRequestToUser( request ) );
        assignment.setTrainerAssignment( assignmentRequestToUser1( request ) );
        assignment.setAssignmentDate( request.getAssignmentDate() );

        return assignment;
    }

    @Override
    public AssignmentResponse toResponse(Assignment assignment) {
        if ( assignment == null ) {
            return null;
        }

        AssignmentResponse.AssignmentResponseBuilder assignmentResponse = AssignmentResponse.builder();

        assignmentResponse.userId( assignmentIdUserId( assignment ) );
        assignmentResponse.trainerId( assignmentIdTrainerId( assignment ) );
        assignmentResponse.userFirstName( assignmentUserAssignmentFirstName( assignment ) );
        assignmentResponse.userLastName( assignmentUserAssignmentLastName( assignment ) );
        assignmentResponse.trainerFirstName( assignmentTrainerAssignmentFirstName( assignment ) );
        assignmentResponse.trainerLastName( assignmentTrainerAssignmentLastName( assignment ) );
        assignmentResponse.assignmentDate( assignment.getAssignmentDate() );

        return assignmentResponse.build();
    }

    protected AssignmentId assignmentRequestToAssignmentId(AssignmentRequest assignmentRequest) {
        if ( assignmentRequest == null ) {
            return null;
        }

        AssignmentId assignmentId = new AssignmentId();

        assignmentId.setUserId( assignmentRequest.getUserId() );
        assignmentId.setTrainerId( assignmentRequest.getTrainerId() );

        return assignmentId;
    }

    protected User assignmentRequestToUser(AssignmentRequest assignmentRequest) {
        if ( assignmentRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( assignmentRequest.getUserId() );

        return user;
    }

    protected User assignmentRequestToUser1(AssignmentRequest assignmentRequest) {
        if ( assignmentRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( assignmentRequest.getTrainerId() );

        return user;
    }

    private Integer assignmentIdUserId(Assignment assignment) {
        AssignmentId id = assignment.getId();
        if ( id == null ) {
            return null;
        }
        return id.getUserId();
    }

    private Integer assignmentIdTrainerId(Assignment assignment) {
        AssignmentId id = assignment.getId();
        if ( id == null ) {
            return null;
        }
        return id.getTrainerId();
    }

    private String assignmentUserAssignmentFirstName(Assignment assignment) {
        User userAssignment = assignment.getUserAssignment();
        if ( userAssignment == null ) {
            return null;
        }
        return userAssignment.getFirstName();
    }

    private String assignmentUserAssignmentLastName(Assignment assignment) {
        User userAssignment = assignment.getUserAssignment();
        if ( userAssignment == null ) {
            return null;
        }
        return userAssignment.getLastName();
    }

    private String assignmentTrainerAssignmentFirstName(Assignment assignment) {
        User trainerAssignment = assignment.getTrainerAssignment();
        if ( trainerAssignment == null ) {
            return null;
        }
        return trainerAssignment.getFirstName();
    }

    private String assignmentTrainerAssignmentLastName(Assignment assignment) {
        User trainerAssignment = assignment.getTrainerAssignment();
        if ( trainerAssignment == null ) {
            return null;
        }
        return trainerAssignment.getLastName();
    }
}
