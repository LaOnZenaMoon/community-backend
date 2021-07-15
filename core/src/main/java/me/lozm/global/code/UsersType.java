package me.lozm.global.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UsersType {

    ALL(-0L, "전체"),
    ADMIN(-1L, "관리자"),
    USER(-2L, "사용자"),
    API_SYSTEM(-3L, "API 시스템");

    private Long code;
    private String description;

    UsersType(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UsersType findCode(Long code) {
        return Arrays.stream(UsersType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(USER);
    }

}
