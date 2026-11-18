package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Одна камера зі списку зовнішнього сервісу. device_type з JSON зіставлено з полем deviceType. */
public record CameraItemDto(
        Long id,
        String name,
        @JsonProperty("device_type") String deviceType,
        String status,
        CameraInfoDto.Location location
) {
}
