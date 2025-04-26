package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.SecurityCodeDataProvider;
import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UT_SecurityCodeMapperTest {

    private SecurityCodeMapper securityCodeMapper;

    @BeforeEach
    void init(){
        securityCodeMapper = new SecurityCodeMapper();
    }

    @Test
    void testSecurityCodeModelToDBO(){
        // Given
        SecurityCodesModel model = SecurityCodeDataProvider.getSecurityCodesModel();

        // When
        SecurityCodesDBO result = SecurityCodeMapper.securityCodeModelToDBO(model);

        // then
        assertNotNull(result);

        assertEquals(model.getSecurity_code(), result.getSecurity_code());
    }

    @Test
    void testSecurityCodesDBOToModel(){
        // Given
        SecurityCodesDBO dbo = SecurityCodeDataProvider.getSecurityCodesDBO();

        // When
        SecurityCodesModel result = SecurityCodeMapper.securityCodesDBOToModel(dbo);

        // then
        assertNotNull(result);

        assertEquals(dbo.getSecurity_code(), result.getSecurity_code());
    }

    @Test
    void testPassTest(){
        // When
        boolean result = this.securityCodeMapper.passTest();

        // Then
        assertTrue(result);
    }
}
