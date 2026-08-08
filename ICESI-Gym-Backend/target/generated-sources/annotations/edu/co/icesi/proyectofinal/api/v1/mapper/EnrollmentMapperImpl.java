package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.EnrollmentRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.EnrollmentResponse;
import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Enrollment;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.keys.EnrollmentId;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:47-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class EnrollmentMapperImpl implements EnrollmentMapper {

    @Override
    public Enrollment toEntity(EnrollmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setId( enrollmentRequestToEnrollmentId( request ) );
        enrollment.setUser( enrollmentRequestToUser( request ) );
        enrollment.setActivity( enrollmentRequestToActivity( request ) );
        enrollment.setEnrollmentDate( request.getEnrollmentDate() );

        return enrollment;
    }

    @Override
    public EnrollmentResponse toResponse(Enrollment enrollment) {
        if ( enrollment == null ) {
            return null;
        }

        EnrollmentResponse.EnrollmentResponseBuilder enrollmentResponse = EnrollmentResponse.builder();

        enrollmentResponse.userId( enrollmentIdUserId( enrollment ) );
        enrollmentResponse.activityId( enrollmentIdActivityId( enrollment ) );
        enrollmentResponse.activityName( enrollmentActivityName( enrollment ) );
        enrollmentResponse.activityEndDate( enrollmentActivityEndDate( enrollment ) );
        enrollmentResponse.enrollmentDate( enrollment.getEnrollmentDate() );

        return enrollmentResponse.build();
    }

    protected EnrollmentId enrollmentRequestToEnrollmentId(EnrollmentRequest enrollmentRequest) {
        if ( enrollmentRequest == null ) {
            return null;
        }

        EnrollmentId enrollmentId = new EnrollmentId();

        enrollmentId.setUserId( enrollmentRequest.getUserId() );
        enrollmentId.setActivityId( enrollmentRequest.getActivityId() );

        return enrollmentId;
    }

    protected User enrollmentRequestToUser(EnrollmentRequest enrollmentRequest) {
        if ( enrollmentRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( enrollmentRequest.getUserId() );

        return user;
    }

    protected Activity enrollmentRequestToActivity(EnrollmentRequest enrollmentRequest) {
        if ( enrollmentRequest == null ) {
            return null;
        }

        Activity activity = new Activity();

        activity.setIdActivity( enrollmentRequest.getActivityId() );

        return activity;
    }

    private Integer enrollmentIdUserId(Enrollment enrollment) {
        EnrollmentId id = enrollment.getId();
        if ( id == null ) {
            return null;
        }
        return id.getUserId();
    }

    private Integer enrollmentIdActivityId(Enrollment enrollment) {
        EnrollmentId id = enrollment.getId();
        if ( id == null ) {
            return null;
        }
        return id.getActivityId();
    }

    private String enrollmentActivityName(Enrollment enrollment) {
        Activity activity = enrollment.getActivity();
        if ( activity == null ) {
            return null;
        }
        return activity.getName();
    }

    private LocalDate enrollmentActivityEndDate(Enrollment enrollment) {
        Activity activity = enrollment.getActivity();
        if ( activity == null ) {
            return null;
        }
        return activity.getEndDate();
    }
}
