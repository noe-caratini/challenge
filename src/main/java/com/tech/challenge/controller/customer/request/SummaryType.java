package com.tech.challenge.controller.customer.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum SummaryType {
    AVB("AvB"),
    AVB_TIME("AvBTime");

    private final String typeString;

    public static SummaryType fromString(String string) {
        return Stream.of(SummaryType.values())
                .filter(l -> l.getTypeString().equals(string))
                .findFirst()
                .orElse(null);
    }
}
