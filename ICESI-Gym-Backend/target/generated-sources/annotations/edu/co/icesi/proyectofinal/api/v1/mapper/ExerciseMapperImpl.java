package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.ExerciseRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ExerciseResponse;
import edu.co.icesi.proyectofinal.entity.Exercise;
import edu.co.icesi.proyectofinal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:47-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ExerciseMapperImpl implements ExerciseMapper {

    @Override
    public ExerciseResponse toResponse(Exercise exercise) {
        if ( exercise == null ) {
            return null;
        }

        ExerciseResponse.ExerciseResponseBuilder exerciseResponse = ExerciseResponse.builder();

        exerciseResponse.userId( exerciseUserExerciseIdUser( exercise ) );
        exerciseResponse.description( exercise.getDescription() );
        exerciseResponse.difficulty( exercise.getDifficulty() );
        exerciseResponse.durationMin( exercise.getDurationMin() );
        exerciseResponse.idExercise( exercise.getIdExercise() );
        exerciseResponse.name( exercise.getName() );
        exerciseResponse.predefined( exercise.isPredefined() );
        exerciseResponse.type( exercise.getType() );
        exerciseResponse.videoUrl( exercise.getVideoUrl() );

        return exerciseResponse.build();
    }

    @Override
    public Exercise toEntity(ExerciseRequest exerciseRequest) {
        if ( exerciseRequest == null ) {
            return null;
        }

        Exercise exercise = new Exercise();

        exercise.setUserExercise( idToUser( exerciseRequest.getUserId() ) );
        exercise.setPredefined( exerciseRequest.isPredefined() );
        exercise.setDescription( exerciseRequest.getDescription() );
        exercise.setDifficulty( exerciseRequest.getDifficulty() );
        exercise.setDurationMin( exerciseRequest.getDurationMin() );
        exercise.setName( exerciseRequest.getName() );
        exercise.setType( exerciseRequest.getType() );
        exercise.setVideoUrl( exerciseRequest.getVideoUrl() );

        return exercise;
    }

    private Integer exerciseUserExerciseIdUser(Exercise exercise) {
        User userExercise = exercise.getUserExercise();
        if ( userExercise == null ) {
            return null;
        }
        return userExercise.getIdUser();
    }
}
