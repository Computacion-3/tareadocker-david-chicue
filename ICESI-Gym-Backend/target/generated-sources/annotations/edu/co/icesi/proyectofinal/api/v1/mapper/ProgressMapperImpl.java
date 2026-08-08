package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.ProgressRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ProgressResponse;
import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.entity.Progress;
import edu.co.icesi.proyectofinal.entity.Routine;
import edu.co.icesi.proyectofinal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ProgressMapperImpl implements ProgressMapper {

    @Override
    public Progress toEntity(ProgressRequest request) {
        if ( request == null ) {
            return null;
        }

        Progress progress = new Progress();

        progress.setUserProgress( progressRequestToUser( request ) );
        progress.setExercise( progressRequestToExercise( request ) );
        progress.setRoutine( progressRequestToRoutine( request ) );
        progress.setDateLogged( request.getDateLogged() );
        progress.setDurationMin( request.getDurationMin() );
        progress.setEffortLevel( request.getEffortLevel() );
        progress.setReps( request.getReps() );
        progress.setSetNumber( request.getSetNumber() );
        progress.setWeightKg( request.getWeightKg() );

        return progress;
    }

    @Override
    public ProgressResponse toResponse(Progress progress) {
        if ( progress == null ) {
            return null;
        }

        ProgressResponse progressResponse = new ProgressResponse();

        progressResponse.setUserId( progressUserProgressIdUser( progress ) );
        progressResponse.setExerciseId( progressExerciseIdExercise( progress ) );
        progressResponse.setRoutineId( progressRoutineIdRoutine( progress ) );
        progressResponse.setDateLogged( progress.getDateLogged() );
        progressResponse.setDurationMin( progress.getDurationMin() );
        progressResponse.setEffortLevel( progress.getEffortLevel() );
        progressResponse.setIdProgress( progress.getIdProgress() );
        progressResponse.setReps( progress.getReps() );
        progressResponse.setSetNumber( progress.getSetNumber() );
        progressResponse.setWeightKg( progress.getWeightKg() );

        return progressResponse;
    }

    protected User progressRequestToUser(ProgressRequest progressRequest) {
        if ( progressRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( progressRequest.getUserId() );

        return user;
    }

    protected Exercise progressRequestToExercise(ProgressRequest progressRequest) {
        if ( progressRequest == null ) {
            return null;
        }

        Exercise exercise = new Exercise();

        exercise.setIdExercise( progressRequest.getExerciseId() );

        return exercise;
    }

    protected Routine progressRequestToRoutine(ProgressRequest progressRequest) {
        if ( progressRequest == null ) {
            return null;
        }

        Routine routine = new Routine();

        routine.setIdRoutine( progressRequest.getRoutineId() );

        return routine;
    }

    private Integer progressUserProgressIdUser(Progress progress) {
        User userProgress = progress.getUserProgress();
        if ( userProgress == null ) {
            return null;
        }
        return userProgress.getIdUser();
    }

    private Integer progressExerciseIdExercise(Progress progress) {
        Exercise exercise = progress.getExercise();
        if ( exercise == null ) {
            return null;
        }
        return exercise.getIdExercise();
    }

    private Integer progressRoutineIdRoutine(Progress progress) {
        Routine routine = progress.getRoutine();
        if ( routine == null ) {
            return null;
        }
        return routine.getIdRoutine();
    }
}
