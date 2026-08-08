package edu.co.icesi.proyectofinal.services;

import edu.co.icesi.proyectofinal.entity.Space;

import java.util.List;

public interface SpaceService {

    List<Space> getSpaces();

    Space getSpace(Integer id);

    Space createSpace(Space space);

    Space updateSpace(Space space);

    void deleteSpace(Integer id);

    List<Space> getByMinCapacity(int capacity);
}
