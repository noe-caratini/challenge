package com.tech.challenge.model.licensetype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum LicenseType {
    MATCH("M"),
    TOURNAMENT("T");

    private final String code;

    public static LicenseType fromCode(String code) {
        return Stream.of(LicenseType.values())
                .filter(l -> l.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
