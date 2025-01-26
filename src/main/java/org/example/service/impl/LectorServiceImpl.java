package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.dto.lector.LectorRequestDTO;
import org.example.entity.Lector;
import org.example.repositrory.LectorRepository;
import org.example.service.LectorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectorServiceImpl implements LectorService {
    private final LectorRepository lectorRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public List<LectorRequestDTO> searchByNameContaining(String name) {
        List<Lector> lectors = this.lectorRepository.findByNameContaining(name);
        return lectors
                .stream()
                .map(lector ->
                        this.modelMapper.map(lector, LectorRequestDTO.class))
                .toList();
    }
}
