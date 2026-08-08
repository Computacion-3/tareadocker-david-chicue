package edu.co.icesi.proyectofinal.unit;

import edu.co.icesi.proyectofinal.entity.Space;
import edu.co.icesi.proyectofinal.repository.SpaceRepository;
import edu.co.icesi.proyectofinal.services.impl.SpaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpaceServiceTest {

    @Mock
    private SpaceRepository spaceRepository;

    @Mock
    private edu.co.icesi.proyectofinal.services.NotificationService notificationService;

    @InjectMocks
    private SpaceServiceImpl spaceService;

    private Space space;

    @BeforeEach
    void setUp() {
        space = new Space();
        space.setIdSpace(1);
        space.setName("Gimnasio Principal");
        space.setCapacity(50);
        space.setLocation("Bloque A");
    }

    @Test
    void getSpaces_shouldReturnList() {
        when(spaceRepository.findAll()).thenReturn(List.of(space));

        assertEquals(1, spaceService.getSpaces().size());
    }

    @Test
    void getSpace_whenExists_shouldReturn() {
        when(spaceRepository.findById(1)).thenReturn(Optional.of(space));

        Space result = spaceService.getSpace(1);

        assertNotNull(result);
        assertEquals("Gimnasio Principal", result.getName());
    }

    @Test
    void getSpace_whenNotExists_shouldThrowRuntimeException() {
        when(spaceRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> spaceService.getSpace(99));
    }

    @Test
    void createSpace_shouldReturnSaved() {
        when(spaceRepository.save(space)).thenReturn(space);

        Space result = spaceService.createSpace(space);

        assertNotNull(result);
        assertEquals(50, result.getCapacity());
    }

    @Test
    void updateSpace_whenExists_shouldUpdateAndReturn() {
        Space updated = new Space();
        updated.setIdSpace(1);
        updated.setName("Sala de Yoga");
        updated.setCapacity(30);
        updated.setLocation("Bloque B");

        when(spaceRepository.findById(1)).thenReturn(Optional.of(space));
        when(spaceRepository.save(any(Space.class))).thenAnswer(x -> x.getArgument(0));

        Space result = spaceService.updateSpace(updated);

        assertEquals("Sala de Yoga", result.getName());
        assertEquals(30, result.getCapacity());
        assertEquals("Bloque B", result.getLocation());
    }

    @Test
    void updateSpace_whenNotExists_shouldThrowRuntimeException() {
        space.setIdSpace(99);
        when(spaceRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> spaceService.updateSpace(space));
    }

    @Test
    void deleteSpace_shouldCallRepository() {
        when(spaceRepository.existsById(1)).thenReturn(true);
        doNothing().when(spaceRepository).deleteById(1);
        spaceService.deleteSpace(1);
        verify(spaceRepository).deleteById(1);
    }

    @Test
    void getByMinCapacity_shouldReturnFiltered() {
        when(spaceRepository.findByCapacityGreaterThanEqual(20)).thenReturn(List.of(space));

        List<Space> result = spaceService.getByMinCapacity(20);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getCapacity() >= 20);
    }

    @Test
    void getByMinCapacity_whenNoneMatch_shouldReturnEmptyList() {
        when(spaceRepository.findByCapacityGreaterThanEqual(200)).thenReturn(List.of());

        assertTrue(spaceService.getByMinCapacity(200).isEmpty());
    }
}