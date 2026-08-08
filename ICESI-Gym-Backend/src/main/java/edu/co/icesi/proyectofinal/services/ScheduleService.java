package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    List<Schedule> getSchedules();

    Schedule getSchedule(Integer id);

    Schedule saveSchedule(Schedule schedule);

    Schedule updateSchedule(Schedule schedule);

    void deleteSchedule(Integer id);

    List<Schedule> getByActivityId(Integer activityId);

    List<Schedule> getByActivityName(String activityName);
}
