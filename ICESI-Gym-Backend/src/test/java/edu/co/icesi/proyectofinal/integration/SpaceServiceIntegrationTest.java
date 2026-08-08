package edu.co.icesi.proyectofinal.integration;

import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.repository.SpaceRepository;
import edu.co.icesi.proyectofinal.services.SpaceService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class SpaceServiceIntegrationTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceRepository spaceRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createSpace_shouldPersist() {
        Space space = new Space();
        space.setName("Gym");
        space.setCapacity(40);
        space.setLocation("Bloque A");

        Space saved = spaceService.createSpace(space);

        assertNotNull(saved.getIdSpace());
        assertEquals(1, spaceRepository.findAll().size());
    }

    @Test
    void getSpace_whenExists_shouldReturn() {
        Space space = new Space();
        space.setName("Gym");
        space.setCapacity(40);
        space.setLocation("Bloque A");
        Space saved = spaceRepository.save(space);

        Space result = spaceService.getSpace(saved.getIdSpace());

        assertEquals("Gym", result.getName());
    }

    @Test
    void getSpace_whenNotExists_shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> spaceService.getSpace(999));
    }

    @Test
    void updateSpace_shouldReflectChanges() {
        Space space = new Space();
        space.setName("Viejo");
        space.setCapacity(10);
        space.setLocation("X");
        Space saved = spaceRepository.save(space);

        saved.setName("Nuevo");
        saved.setCapacity(100);
        Space result = spaceService.updateSpace(saved);

        assertEquals("Nuevo", result.getName());
        assertEquals(100, result.getCapacity());
    }

    @Test
    void deleteSpace_shouldRemove() {
        Space space = new Space();
        space.setName("Gym");
        space.setCapacity(30);
        space.setLocation("B");
        Space saved = spaceRepository.save(space);

        spaceService.deleteSpace(saved.getIdSpace());

        assertTrue(spaceRepository.findAll().isEmpty());
    }

    @Test
    void getSpaces_shouldReturnAll() {
        Space s1 = new Space(); s1.setName("A"); s1.setCapacity(10); s1.setLocation("L1");
        Space s2 = new Space(); s2.setName("B"); s2.setCapacity(20); s2.setLocation("L2");
        spaceRepository.save(s1);
        spaceRepository.save(s2);

        assertEquals(2, spaceService.getSpaces().size());
    }

    @Test
    void getByMinCapacity_shouldReturnFiltered() {
        Space s1 = new Space(); s1.setName("Small"); s1.setCapacity(5); s1.setLocation("L1");
        Space s2 = new Space(); s2.setName("Large"); s2.setCapacity(100); s2.setLocation("L2");
        spaceRepository.save(s1);
        spaceRepository.save(s2);

        List<Space> result = spaceService.getByMinCapacity(50);

        assertEquals(1, result.size());
        assertEquals("Large", result.get(0).getName());
    }
}