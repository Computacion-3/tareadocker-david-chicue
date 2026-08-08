package edu.co.icesi.proyectofinal.services.impl;

import edu.co.icesi.proyectofinal.entity.Notification;
import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.repository.SpaceRepository;
import edu.co.icesi.proyectofinal.services.NotificationService;
import edu.co.icesi.proyectofinal.services.SpaceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final NotificationService notificationService;

    public SpaceServiceImpl(SpaceRepository spaceRepository, NotificationService notificationService) {
        this.spaceRepository = spaceRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<Space> getSpaces() {
        return spaceRepository.findAll();
    }

    @Override
    public Space getSpace(Integer id) {
        return spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Space with id " + id + " not found"));
    }

    //Notification add-on for spaces.
    @Override
    public Space createSpace(Space space) {
        Space saved = spaceRepository.save(space);

        Notification notification = new Notification();
        notification.setType("NEW_SPACE");
        notification.setMessage("A new space has been opened: " + saved.getName());
        notification.setReferenceId(saved.getIdSpace());
        notification.setReferenceType("SPACE");
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
        // userTarget is null for broadcast

        notificationService.saveNotification(notification);

        return saved;
    }

    @Override
    public Space updateSpace(Space space) {
        Space existing = spaceRepository.findById(space.getIdSpace())
                .orElseThrow(() -> new RuntimeException("Space with id " + space.getIdSpace() + " not found"));
        existing.setName(space.getName());
        existing.setCapacity(space.getCapacity());
        existing.setLocation(space.getLocation());
        return spaceRepository.save(existing);
    }

    @Override
    public void deleteSpace(Integer id) {

        if(!spaceRepository.existsById(id)){
            throw new RuntimeException("Space with id " + id +
                    " not found");
        }

        spaceRepository.deleteById(id);
    }

    @Override
    public List<Space> getByMinCapacity(int capacity) {
        return spaceRepository.findByCapacityGreaterThanEqual(capacity);
    }
}