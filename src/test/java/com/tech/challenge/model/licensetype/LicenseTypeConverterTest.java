package com.tech.challenge.model.licensetype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LicenseTypeConverterTest {

    private LicenseTypeConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new LicenseTypeConverter();
    }

    @Test
    public void shouldConvertToDatabaseColumn() {
        String result = converter.convertToDatabaseColumn(LicenseType.MATCH);

        assertThat(result).isEqualTo(LicenseType.MATCH.getCode());
    }

    @Test
    public void shouldReturnNullWhenConvertingNullTypeToDatabaseColumn() {
        String result = converter.convertToDatabaseColumn(null);

        assertThat(result).isNull();
    }

    @Test
    public void shoulConvertToEntityAttribute() {
        LicenseType result = converter.convertToEntityAttribute(LicenseType.MATCH.getCode());

        assertThat(result).isEqualTo(LicenseType.MATCH);
    }

    @Test
    public void shouldReturnNullWhenConvertingNullTypeToEntityAttribute() {
        LicenseType result = converter.convertToEntityAttribute(null);

        assertThat(result).isNull();
    }

    @Test
    public void shouldThrowExceptionIfInvalidDataWhenConvertingToEntityAttribute() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute("gibberish"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}