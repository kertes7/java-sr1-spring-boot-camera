package com.example.demo.dto;

import java.util.List;

/**
 * Відповідь зовнішнього сервісу з інформацією про камеру.
 * Назва device_type збігається з полем JSON, тому додаткове відображення не потрібне.
 */
public record CameraInfoDto(
        Integer id,
        String device_type,
        String status,
        List<String> features,
        Location location
) {

    /** Вкладений об'єкт location: кімната та поверх. */
    public record Location(String room, Integer floor) {
    }
}
