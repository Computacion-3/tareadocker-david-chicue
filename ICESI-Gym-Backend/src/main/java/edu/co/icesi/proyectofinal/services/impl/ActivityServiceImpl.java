package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.repository.ActivityRepository;
import edu.co.icesi.proyectofinal.services.ActivityService;
import edu.co.icesi.proyectofinal.services.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final NotificationService notificationService;

    public ActivityServiceImpl(ActivityRepository activityRepository, NotificationService notificationService) {
        this.activityRepository = activityRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Activity findById(Integer id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity with id " + id + " not found"));
    }

    //Notification of creation in real Time
    @Override
    public Activity save(Activity activity) {
        Activity saved = activityRepository.save(activity);

        Notification notification = new Notification();
        notification.setType("NEW_ACTIVITY");
        notification.setMessage("A new activity has been scheduled: " + saved.getName());
        notification.setReferenceId(saved.getIdActivity());
        notification.setReferenceType("ACTIVITY");
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
        notification.setUserTarget(null); // Explicitly null for broadcast

        notificationService.saveNotification(notification);

        return saved;
    }

    @Override
    public Activity update(Activity activity) {
        Activity existing = activityRepository.findById(activity.getIdActivity())
                .orElseThrow(() -> new RuntimeException("Activity with id " + activity.getIdActivity() + " not found"));
        existing.setName(activity.getName());
        existing.setDescription(activity.getDescription());
        existing.setStartDate(activity.getStartDate());
        existing.setEndDate(activity.getEndDate());
        existing.setSpace(activity.getSpace());
        return activityRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if(!activityRepository.existsById(id)){
            throw new RuntimeException("Activity with id " + id + " not found");
        }
        activityRepository.deleteById(id);
    }

    @Override
    public List<Activity> findBySpaceId(Integer spaceId) {

        if(activityRepository.findBySpaceIdSpace(spaceId).isEmpty()){
            throw new RuntimeException("Activities with space id " + spaceId + " not found");
        }

        return activityRepository.findBySpaceIdSpace(spaceId);
    }

    @Override
    public List<Activity> findByNameContaining(String name) {
        if(activityRepository.findByNameContainingIgnoreCase(name).isEmpty()){
            throw new RuntimeException("Activities with name containing " + name + " not found");
        }

        return activityRepository.findByNameContainingIgnoreCase(name);
    }
}