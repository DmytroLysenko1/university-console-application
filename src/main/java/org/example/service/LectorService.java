package org.example.service;

import java.util.List;

@FunctionalInterface
public interface LectorService {
    List<String> searchByNameContaining(String name);
}
