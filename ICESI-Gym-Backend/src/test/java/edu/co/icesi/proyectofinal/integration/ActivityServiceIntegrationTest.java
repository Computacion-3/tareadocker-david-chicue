package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Activity;
import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.repository.ActivityRepository;
import edu.co.icesi.proyectofinal.repository.SpaceRepository;
import edu.co.icesi.proyectofinal.services.ActivityService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ActivityServiceIntegrationTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    private Space space;

    @BeforeEach
    void setUp() {

        space = new Space();
        space.setName("Gym");
        space.setCapacity(40);
        space.setLocation("Bloque A");
        spaceRepository.save(space);
    }

    private Activity buildActivity(String name) {
        Activity a = new Activity();
        a.setName(name);
        a.setDescription("Descripción de " + name);
        a.setStartDate(LocalDate.now());
        a.setEndDate(LocalDate.now().plusMonths(1));
        a.setSpace(space);
        return a;
    }

    @Test
    void save_shouldPersist() {
        Activity saved = activityService.save(buildActivity("Yoga"));

        assertNotNull(saved.getIdActivity());
        assertEquals(1, activityRepository.findAll().size());
    }

    @Test
    void findById_whenExists_shouldReturn() {
        Activity saved = activityRepository.save(buildActivity("Pilates"));

        Activity result = activityService.findById(saved.getIdActivity());

        assertEquals("Pilates", result.getName());
    }

    @Test
    void findById_whenNotExists_shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> activityService.findById(999));
    }

    @Test
    void update_shouldReflectChanges() {
        Activity saved = activityRepository.save(buildActivity("Yoga"));
        saved.setName("Zumba");
        saved.setDescription("Nueva desc");

        Activity result = activityService.update(saved);

        assertEquals("Zumba", result.getName());
        assertEquals("Nueva desc", result.getDescription());
    }

    @Test
    void delete_shouldRemove() {
        Activity saved = activityRepository.save(buildActivity("Yoga"));

        activityService.delete(saved.getIdActivity());

        assertTrue(activityRepository.findAll().isEmpty());
    }

    @Test
    void findAll_shouldReturnAll() {
        activityRepository.save(buildActivity("Yoga"));
        activityRepository.save(buildActivity("Crossfit"));

        assertEquals(2, activityService.findAll().size());
    }

    @Test
    void findByNameContaining_shouldReturnMatches() {
        activityRepository.save(buildActivity("Yoga Matutino"));
        activityRepository.save(buildActivity("Crossfit"));

        List<Activity> result = activityService.findByNameContaining("yoga");

        assertEquals(1, result.size());
    }

    @Test
    void findBySpaceId_shouldReturnFiltered() {
        activityRepository.save(buildActivity("Yoga"));
        activityRepository.save(buildActivity("Pilates"));

        List<Activity> result = activityService.findBySpaceId(space.getIdSpace());

        assertEquals(2, result.size());
    }
}