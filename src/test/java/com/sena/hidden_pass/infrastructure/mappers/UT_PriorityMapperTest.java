package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.PriorityDataProvider;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UT_PriorityMapperTest {

    private PriorityMapper priorityMapper;

    @BeforeEach
    void init(){
        priorityMapper = new PriorityMapper();
    }

    @Test
    void testPriorityModelToDBO(){
        // Given
        PriorityModel model = PriorityDataProvider.getPriorityModel();

        // When
        PriorityDBO result = PriorityMapper.priorityModelToDBO(model);

        // Then
        assertNotNull(result);

        assertEquals(model.getId_priority(), result.getId_priority());
        assertEquals(model.getName(), result.getName());
    }

    @Test
    void testPriorityDBOToModel(){
        // Given
        PriorityDBO dbo = PriorityDataProvider.getPriorityDBO();

        // When
        PriorityModel result = PriorityMapper.priorityDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_priority(), result.getId_priority());
        assertEquals(dbo.getName(), result.getName());
    }

    @Test
    void testPassTest(){
        // When
        boolean result = this.priorityMapper.passTest();

        // Then
        assertTrue(result);
    }
}
