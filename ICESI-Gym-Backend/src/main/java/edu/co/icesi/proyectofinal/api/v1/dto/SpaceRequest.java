package edu.co.icesi.proyectofinal.api.v1.dto;

import lombok.Data;

@Data
public class SpaceRequest {
    private String name;
    private Integer capacity;
    private String location;
}
