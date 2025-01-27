package org.example.service;

import org.example.entity.Lector;
import org.example.repositrory.LectorRepository;
import org.example.service.impl.LectorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectorServiceImplTest {

    @Mock
    private LectorRepository lectorRepository;

    @InjectMocks
    private LectorServiceImpl lectorService;

    private Lector lector;
    private Lector lector2;

    @BeforeEach
    void setUp() {
        this.lector = new Lector();
        this.lector.setName("Alice Johnson");

        this.lector2 = new Lector();
        this.lector2.setName("Bob Smith");
    }

    @Test
    void searchByNameContaining_validName_lectorsList() {
        when(this.lectorRepository.findByNameContaining("Alice"))
                .thenReturn(List.of(this.lector));

        List<String> result = this.lectorService.searchByNameContaining("Alice");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("Alice");
    }

    @Test
    void searchByNameContaining_noLectors_emptyList() {
        when(this.lectorRepository.findByNameContaining("NonExistent"))
                .thenReturn(Collections.emptyList());

        List<String> result = this.lectorService.searchByNameContaining("NonExistent");

        assertEquals(0, result.size());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("NonExistent");
    }

    @Test
    void searchByNameContaining_multipleLectors_lectorsList() {
        when(this.lectorRepository.findByNameContaining("Smith"))
                .thenReturn(List.of(this.lector, this.lector2));

        List<String> result = this.lectorService.searchByNameContaining("Smith");

        assertEquals(2, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        assertEquals("Bob Smith", result.get(1));
        verify(this.lectorRepository, times(1))
                .findByNameContaining("Smith");
    }

    @Test
    void searchByNameContaining_emptyName_emptyList() {
        when(this.lectorRepository.findByNameContaining(""))
                .thenReturn(Collections.emptyList());

        List<String> result = this.lectorService.searchByNameContaining("");

        assertEquals(0, result.size());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("");
    }

    @Test
    void searchByNameContaining_nullName_emptyList() {
        when(this.lectorRepository.findByNameContaining(null))
                .thenReturn(Collections.emptyList());

        List<String> result = this.lectorService.searchByNameContaining(null);

        assertEquals(0, result.size());
        verify(this.lectorRepository, times(1))
                .findByNameContaining(null);
    }

    @Test
    void searchByNameContaining_singleCharacter_lectorsList() {
        when(this.lectorRepository.findByNameContaining("A"))
                .thenReturn(List.of(this.lector));

        List<String> result = this.lectorService.searchByNameContaining("A");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("A");
    }

    @Test
    void searchByNameContaining_caseInsensitive_lectorsList() {
        Lector lector3 = new Lector();
        lector3.setName("alice johnson");

        when(this.lectorRepository.findByNameContaining("alice"))
                .thenReturn(List.of(this.lector, lector3));

        List<String> result = this.lectorService.searchByNameContaining("alice");

        assertEquals(2, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        assertEquals("alice johnson", result.get(1));
        verify(this.lectorRepository, times(1))
                .findByNameContaining("alice");
    }
    @Test
    void searchByNameContaining_substring_lectorsList() {
        when(this.lectorRepository.findByNameContaining("John"))
                .thenReturn(List.of(this.lector));

        List<String> result = this.lectorService.searchByNameContaining("John");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("John");
    }

    @Test
    void searchByNameContaining_exactMatch_lectorsList() {
        when(this.lectorRepository.findByNameContaining("Alice Johnson"))
                .thenReturn(List.of(this.lector));

        List<String> result = this.lectorService.searchByNameContaining("Alice Johnson");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("Alice Johnson");
    }

    @Test
    void searchByNameContaining_differentCaseMatch_lectorsList() {
        Lector lector3 = new Lector();
        lector3.setName("ALICE JOHNSON");

        when(this.lectorRepository.findByNameContaining("alice johnson"))
                .thenReturn(List.of(this.lector, lector3));

        List<String> result = lectorService.searchByNameContaining("alice johnson");

        assertEquals(2, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        assertEquals("ALICE JOHNSON", result.get(1));
        verify(lectorRepository, times(1))
                .findByNameContaining("alice johnson");
    }
    @Test
    void searchByNameContaining_multipleMatches_lectorsList() {
        Lector lector3 = new Lector();
        lector3.setName("Alice Smith");

        when(this.lectorRepository.findByNameContaining("Alice"))
                .thenReturn(List.of(this.lector, lector3));

        List<String> result = this.lectorService.searchByNameContaining("Alice");

        assertEquals(2, result.size());
        assertEquals("Alice Johnson", result.getFirst());
        assertEquals("Alice Smith", result.get(1));
        verify(this.lectorRepository, times(1))
                .findByNameContaining("Alice");
    }

    @Test
    void searchByNameContaining_specialCharacters_lectorsList() {
        Lector lector3 = new Lector();
        lector3.setName("Alice-Johnson");

        when(this.lectorRepository.findByNameContaining("Alice-Johnson"))
                .thenReturn(List.of(lector3));

        List<String> result = this.lectorService.searchByNameContaining("Alice-Johnson");

        assertEquals(1, result.size());
        assertEquals("Alice-Johnson", result.getFirst());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("Alice-Johnson");
    }

    @Test
    void searchByNameContaining_whitespace_lectorsList() {
        Lector lector3 = new Lector();
        lector3.setName(" Alice Johnson ");

        when(this.lectorRepository.findByNameContaining(" Alice Johnson "))
                .thenReturn(List.of(lector3));

        List<String> result = this.lectorService.searchByNameContaining(" Alice Johnson ");

        assertEquals(1, result.size());
        assertEquals(" Alice Johnson ", result.getFirst());
        verify(this.lectorRepository, times(1))
                .findByNameContaining(" Alice Johnson ");
    }

    @Test
    void searchByNameContaining_emptyResult_emptyList() {
        when(this.lectorRepository.findByNameContaining("NonExistent"))
                .thenReturn(Collections.emptyList());

        List<String> result = this.lectorService.searchByNameContaining("NonExistent");

        assertEquals(0, result.size());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("NonExistent");
    }

    @Test
    void searchByNameContaining_nullResult_emptyList() {
        when(this.lectorRepository.findByNameContaining(null))
                .thenReturn(Collections.emptyList());

        List<String> result = this.lectorService.searchByNameContaining(null);

        assertEquals(0, result.size());
        verify(this.lectorRepository, times(1))
                .findByNameContaining(null);
    }
}