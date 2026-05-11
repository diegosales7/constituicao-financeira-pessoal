package com.financas.pessoais.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role == null ? null : role.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.toUpperCase()) {
            case "CLIENT":
                return Role.CLIENT;
            case "ADMIN":
                return Role.ADMIN;
            case "USER":      // mapeia USER antigo para CLIENT
                return Role.CLIENT;
            default:
                throw new IllegalArgumentException("Unknown role: " + dbData);
        }
    }
}

