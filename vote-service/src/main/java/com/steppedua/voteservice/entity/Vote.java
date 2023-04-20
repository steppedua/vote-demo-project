package com.steppedua.voteservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Vote {
    YES("YES"), NO("NO");

    private final String name;

    Vote(String name) {
        this.name = name;
    }
    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static Vote fromCode(String name) {
        return Arrays.stream(values()).filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

}
