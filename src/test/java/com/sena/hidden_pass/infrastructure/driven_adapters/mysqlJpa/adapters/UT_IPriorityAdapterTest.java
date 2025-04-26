package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

public class UT_IPriorityAdapterTest {

    @Mock
    private IPriorityRepository priorityRepository;

    @InjectMocks
    private IPriorityAdapter priorityAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldInsertDefaultPrioritiesOnStartup() throws Exception {
        // When
        priorityAdapter.run(null); // ApplicationArguments no se usa

        // Then
        ArgumentCaptor<List<PriorityDBO>> captor = ArgumentCaptor.forClass(List.class);
        verify(priorityRepository).saveAll(captor.capture());

        List<PriorityDBO> saved = captor.getValue();

        assertEquals(4, saved.size());
        assertTrue(saved.stream().anyMatch(p -> p.getName().equals(PriorityNames.BAJA)));
        assertTrue(saved.stream().anyMatch(p -> p.getName().equals(PriorityNames.MEDIA)));
        assertTrue(saved.stream().anyMatch(p -> p.getName().equals(PriorityNames.ALTA)));
        assertTrue(saved.stream().anyMatch(p -> p.getName().equals(PriorityNames.CRITICA)));
    }
}
