package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.ScheduleRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.ScheduleResponse;
import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Schedule;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ScheduleMapperImpl implements ScheduleMapper {

    @Override
    public Schedule toEntity(ScheduleRequest request) {
        if ( request == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setActivity( scheduleRequestToActivity( request ) );
        schedule.setDayOfWeek( request.getDayOfWeek() );
        schedule.setEndTime( request.getEndTime() );
        schedule.setStartTime( request.getStartTime() );

        return schedule;
    }

    @Override
    public ScheduleResponse toResponse(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        ScheduleResponse scheduleResponse = new ScheduleResponse();

        scheduleResponse.setActivityId( scheduleActivityIdActivity( schedule ) );
        scheduleResponse.setDayOfWeek( schedule.getDayOfWeek() );
        scheduleResponse.setEndTime( schedule.getEndTime() );
        scheduleResponse.setIdSchedule( schedule.getIdSchedule() );
        scheduleResponse.setStartTime( schedule.getStartTime() );

        return scheduleResponse;
    }

    protected Activity scheduleRequestToActivity(ScheduleRequest scheduleRequest) {
        if ( scheduleRequest == null ) {
            return null;
        }

        Activity activity = new Activity();

        activity.setIdActivity( scheduleRequest.getActivityId() );

        return activity;
    }

    private Integer scheduleActivityIdActivity(Schedule schedule) {
        Activity activity = schedule.getActivity();
        if ( activity == null ) {
            return null;
        }
        return activity.getIdActivity();
    }
}
