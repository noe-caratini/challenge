package com.tech.challenge.model.licensetype;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LicenseTypeConverter implements AttributeConverter<LicenseType, String> {

    @Override
    public String convertToDatabaseColumn(LicenseType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public LicenseType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return LicenseType.fromCode(dbData);
    }
}
