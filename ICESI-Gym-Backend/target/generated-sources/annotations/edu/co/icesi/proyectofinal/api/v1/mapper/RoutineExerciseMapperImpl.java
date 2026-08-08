package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.RoutineExerciseRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.RoutineExerciseResponse;
import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.entity.Routine;
import edu.co.icesi.proyectofinal.entity.RoutineExercise;
import edu.co.icesi.proyectofinal.entity.keys.RoutineExerciseId;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class RoutineExerciseMapperImpl implements RoutineExerciseMapper {

    @Override
    public RoutineExercise toEntity(RoutineExerciseRequest request) {
        if ( request == null ) {
            return null;
        }

        RoutineExercise routineExercise = new RoutineExercise();

        routineExercise.setId( routineExerciseRequestToRoutineExerciseId( request ) );
        routineExercise.setRoutine( routineExerciseRequestToRoutine( request ) );
        routineExercise.setExercise( routineExerciseRequestToExercise( request ) );
        routineExercise.setExerciseOrder( request.getExerciseOrder() );
        routineExercise.setSets( request.getSets() );
        routineExercise.setTargetReps( request.getTargetReps() );

        return routineExercise;
    }

    @Override
    public RoutineExerciseResponse toResponse(RoutineExercise entity) {
        if ( entity == null ) {
            return null;
        }

        RoutineExerciseResponse routineExerciseResponse = new RoutineExerciseResponse();

        routineExerciseResponse.setRoutineId( entityIdRoutineId( entity ) );
        routineExerciseResponse.setExerciseId( entityIdExerciseId( entity ) );
        routineExerciseResponse.setExerciseOrder( entity.getExerciseOrder() );
        routineExerciseResponse.setSets( entity.getSets() );
        routineExerciseResponse.setTargetReps( entity.getTargetReps() );

        return routineExerciseResponse;
    }

    protected RoutineExerciseId routineExerciseRequestToRoutineExerciseId(RoutineExerciseRequest routineExerciseRequest) {
        if ( routineExerciseRequest == null ) {
            return null;
        }

        RoutineExerciseId routineExerciseId = new RoutineExerciseId();

        routineExerciseId.setRoutineId( routineExerciseRequest.getRoutineId() );
        routineExerciseId.setExerciseId( routineExerciseRequest.getExerciseId() );

        return routineExerciseId;
    }

    protected Routine routineExerciseRequestToRoutine(RoutineExerciseRequest routineExerciseRequest) {
        if ( routineExerciseRequest == null ) {
            return null;
        }

        Routine routine = new Routine();

        routine.setIdRoutine( routineExerciseRequest.getRoutineId() );

        return routine;
    }

    protected Exercise routineExerciseRequestToExercise(RoutineExerciseRequest routineExerciseRequest) {
        if ( routineExerciseRequest == null ) {
            return null;
        }

        Exercise exercise = new Exercise();

        exercise.setIdExercise( routineExerciseRequest.getExerciseId() );

        return exercise;
    }

    private Integer entityIdRoutineId(RoutineExercise routineExercise) {
        RoutineExerciseId id = routineExercise.getId();
        if ( id == null ) {
            return null;
        }
        return id.getRoutineId();
    }

    private Integer entityIdExerciseId(RoutineExercise routineExercise) {
        RoutineExerciseId id = routineExercise.getId();
        if ( id == null ) {
            return null;
        }
        return id.getExerciseId();
    }
}
