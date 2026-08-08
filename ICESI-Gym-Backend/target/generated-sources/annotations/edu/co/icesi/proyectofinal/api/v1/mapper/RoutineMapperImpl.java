package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.RoutineRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.RoutineResponse;
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
public class RoutineMapperImpl implements RoutineMapper {

    @Override
    public RoutineResponse toResponse(Routine routine) {
        if ( routine == null ) {
            return null;
        }

        RoutineResponse.RoutineResponseBuilder routineResponse = RoutineResponse.builder();

        routineResponse.userId( routineUserRoutineIdUser( routine ) );
        routineResponse.predesigned( routine.isPredesigned() );
        routineResponse.creationDate( routine.getCreationDate() );
        routineResponse.description( routine.getDescription() );
        routineResponse.idRoutine( routine.getIdRoutine() );
        routineResponse.name( routine.getName() );

        return routineResponse.build();
    }

    @Override
    public Routine toEntity(RoutineRequest routineRequest) {
        if ( routineRequest == null ) {
            return null;
        }

        Routine routine = new Routine();

        routine.setUserRoutine( idToUser( routineRequest.getUserId() ) );
        routine.setPredesigned( routineRequest.isPredesigned() );
        routine.setCreationDate( routineRequest.getCreationDate() );
        routine.setDescription( routineRequest.getDescription() );
        routine.setName( routineRequest.getName() );

        return routine;
    }

    private Integer routineUserRoutineIdUser(Routine routine) {
        User userRoutine = routine.getUserRoutine();
        if ( userRoutine == null ) {
            return null;
        }
        return userRoutine.getIdUser();
    }
}
