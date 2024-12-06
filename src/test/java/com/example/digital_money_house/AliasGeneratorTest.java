package com.example.digital_money_house;

import com.example.digital_money_house.Util.AliasGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AliasGeneratorTest {

    @Test
    void testGenerateAlias() {
        String alias = AliasGenerator.generateAlias();
        assertNotNull(alias, "El alias no debe ser nulo.");
        assertTrue(alias.contains("."), "El alias debe contener puntos.");
        assertEquals(3, alias.split("\\.").length, "El alias debe tener tres palabras separadas por puntos.");
    }

    @Test
    void testGenerateCvu() {
        String cvu = AliasGenerator.generateCvu();
        assertNotNull(cvu, "El CVU no debe ser nulo.");
        assertEquals(22, cvu.length(), "El CVU debe tener exactamente 22 dígitos.");
        assertTrue(cvu.matches("\\d+"), "El CVU debe contener solo números.");
    }

    @Test
    void testAliasGeneratorInitialization() {
        assertDoesNotThrow(() -> AliasGenerator.generateAlias(), "La inicialización de AliasGenerator falló.");
    }
}