package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Schedule;
import edu.co.icesi.proyectofinal.repository.ScheduleRepository;
import edu.co.icesi.proyectofinal.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getSchedule(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule with id " + id + " not found"));
    }

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {
        Schedule existing = scheduleRepository.findById(schedule.getIdSchedule())
                .orElseThrow(() -> new RuntimeException("Schedule with id " + schedule.getIdSchedule() + " not found"));
        existing.setDayOfWeek(schedule.getDayOfWeek());
        existing.setStartTime(schedule.getStartTime());
        existing.setEndTime(schedule.getEndTime());
        existing.setActivity(schedule.getActivity());
        return scheduleRepository.save(existing);
    }

    @Override
    public void deleteSchedule(Integer id) {


        if(!scheduleRepository.existsById(id)){
            throw new RuntimeException("Schedule with id " + id +
                    " not found");
        }
        scheduleRepository.deleteById(id);
    }

    @Override
    public List<Schedule> getByActivityId(Integer activityId) {
        return scheduleRepository.findByActivityIdActivity(activityId);
    }

    @Override
    public List<Schedule> getByActivityName(String activityName) {
        return scheduleRepository.findByActivityName(activityName);
    }
}