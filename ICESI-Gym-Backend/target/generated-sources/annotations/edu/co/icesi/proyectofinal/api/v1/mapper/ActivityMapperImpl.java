package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.ActivityRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ActivityResponse;
import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Space;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:47-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ActivityMapperImpl implements ActivityMapper {

    @Override
    public ActivityResponse toResponse(Activity activity) {
        if ( activity == null ) {
            return null;
        }

        ActivityResponse.ActivityResponseBuilder activityResponse = ActivityResponse.builder();

        activityResponse.spaceId( activitySpaceIdSpace( activity ) );
        activityResponse.description( activity.getDescription() );
        activityResponse.endDate( activity.getEndDate() );
        activityResponse.idActivity( activity.getIdActivity() );
        activityResponse.name( activity.getName() );
        activityResponse.startDate( activity.getStartDate() );

        return activityResponse.build();
    }

    @Override
    public Activity toEntity(ActivityRequest activityRequest) {
        if ( activityRequest == null ) {
            return null;
        }

        Activity activity = new Activity();

        activity.setSpace( idToSpace( activityRequest.getSpaceId() ) );
        activity.setDescription( activityRequest.getDescription() );
        activity.setEndDate( activityRequest.getEndDate() );
        activity.setName( activityRequest.getName() );
        activity.setStartDate( activityRequest.getStartDate() );

        return activity;
    }

    private Integer activitySpaceIdSpace(Activity activity) {
        Space space = activity.getSpace();
        if ( space == null ) {
            return null;
        }
        return space.getIdSpace();
    }
}
