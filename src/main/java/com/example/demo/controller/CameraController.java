package com.example.demo.controller;

import com.example.demo.dto.CameraInfoDto;
import com.example.demo.dto.SnapshotDto;
import com.example.demo.entity.CameraEntity;
import com.example.demo.service.CameraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

/** REST API камери: інформація, snapshot (JPEG) і збережені в H2 камери (СР2). */
@RestController
@RequestMapping("/api/v1/camera")
@RequiredArgsConstructor
@Tag(name = "Camera")
public class CameraController {

    private final CameraService cameraService;

    @GetMapping("/info")
    public CameraInfoDto info() {
        return cameraService.getCameraInfo();
    }

    /** Snapshot: декодування Base64 з поля image і повернення байтів JPEG (фінал СР1). */
    @PostMapping(
            value = "/snapshot",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> captureSnapshot() {
        SnapshotDto snapshot = cameraService.captureSnapshot();
        byte[] image = Base64.getDecoder().decode(snapshot.image());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    // ---------------- СР2 ----------------

    /** Викликає зовнішній сервіс і записує камери в базу. */
    @PostMapping("/import")
    public List<CameraEntity> importCameras() {
        return cameraService.importCameras();
    }

    /** Читає всі записи лише з H2 (без звернення до зовнішнього сервісу). */
    @GetMapping("/saved")
    public List<CameraEntity> getSavedCameras() {
        return cameraService.getSavedCameras();
    }

    /** Одна камера або HTTP 404. */
    @GetMapping("/saved/{id}")
    public CameraEntity getSavedCamera(@PathVariable Long id) {
        return cameraService.getSavedCamera(id);
    }

    /** Видаляє запис і повертає HTTP 204 No Content (або 404, якщо запису немає). */
    @DeleteMapping("/saved/{id}")
    public ResponseEntity<Void> deleteSavedCamera(@PathVariable Long id) {
        cameraService.deleteSavedCamera(id);
        return ResponseEntity.noContent().build();
    }
}
