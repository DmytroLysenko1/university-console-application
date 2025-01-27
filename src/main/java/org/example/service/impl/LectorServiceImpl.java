package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.entity.Lector;
import org.example.repositrory.LectorRepository;
import org.example.service.LectorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the LectorService interface.
 * Provides methods for interacting with lector data, including searching by name.
 */
@Service
@RequiredArgsConstructor
public class LectorServiceImpl implements LectorService {
    private final LectorRepository lectorRepository;

    /**
     * Searches for lectors by name containing the specified string.
     *
     * @param name the string to search for in lector names
     * @return a list of lector names containing the specified string
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public List<String> searchByNameContaining(String name) {
        List<Lector> lectors = this.lectorRepository.findByNameContaining(name);
        return lectors
                .stream()
                .map(Lector::getName)
                .toList();
    }
}
