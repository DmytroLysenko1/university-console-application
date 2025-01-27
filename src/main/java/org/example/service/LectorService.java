package org.example.service;

import org.example.dto.lector.LectorResponseDTO;

import java.util.List;

@FunctionalInterface
public interface LectorService {
    List<LectorResponseDTO> searchByNameContaining(String name);
}
