package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.SpaceRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.SpaceResponse;
import edu.co.icesi.proyectofinal.entity.Space;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class SpaceMapperImpl implements SpaceMapper {

    @Override
    public Space toEntity(SpaceRequest request) {
        if ( request == null ) {
            return null;
        }

        Space space = new Space();

        space.setCapacity( request.getCapacity() );
        space.setLocation( request.getLocation() );
        space.setName( request.getName() );

        return space;
    }

    @Override
    public SpaceResponse toResponse(Space space) {
        if ( space == null ) {
            return null;
        }

        SpaceResponse spaceResponse = new SpaceResponse();

        spaceResponse.setCapacity( space.getCapacity() );
        spaceResponse.setIdSpace( space.getIdSpace() );
        spaceResponse.setLocation( space.getLocation() );
        spaceResponse.setName( space.getName() );

        return spaceResponse;
    }
}
