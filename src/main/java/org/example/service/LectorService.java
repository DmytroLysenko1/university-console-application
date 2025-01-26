package org.example.service;

import org.example.dto.lector.LectorRequestDTO;

import java.util.List;

@FunctionalInterface
public interface LectorService {
    List<LectorRequestDTO> searchByNameContaining(String name);
}
