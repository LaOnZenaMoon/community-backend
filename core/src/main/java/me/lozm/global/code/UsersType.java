package me.lozm.global.code;

import lombok.Getter;

@Getter
public enum UsersType {
    ADMIN(-1L, "관리자"),
    USER(-2L, "사용자"),
    API_SYSTEM(-3L, "API 시스템");

    private Long code;
    private String description;

    UsersType(Long code, String description) {
        this.code = code;
        this.description = description;
    }

}
