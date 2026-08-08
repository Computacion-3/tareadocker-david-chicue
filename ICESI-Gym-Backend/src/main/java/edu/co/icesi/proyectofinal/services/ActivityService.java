package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Activity;

import java.util.List;

public interface ActivityService{

    List<Activity> findAll();

    Activity findById(Integer id);

    Activity save(Activity activity);

    Activity update(Activity activity);

    void delete(Integer id);

    List<Activity> findBySpaceId(Integer spaceId);

    List<Activity> findByNameContaining(String name);
}
