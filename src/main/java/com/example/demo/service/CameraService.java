package com.example.demo.service;

import com.example.demo.client.CameraClient;
import com.example.demo.dto.CameraInfoDto;
import com.example.demo.dto.CameraItemDto;
import com.example.demo.dto.CameraListDto;
import com.example.demo.dto.SnapshotDto;
import com.example.demo.entity.CameraEntity;
import com.example.demo.repository.CameraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Сервіс викликає Feign-клієнт, логує запити й працює з базою H2 через CameraRepository. */
@Service
@RequiredArgsConstructor
@Slf4j
public class CameraService {

    private final CameraClient cameraClient;
    private final CameraRepository cameraRepository;

    public CameraInfoDto getCameraInfo() {
        log.info("Getting camera info");
        CameraInfoDto response = cameraClient.getCameraInfo();
        log.info("Camera info received: {}", response);
        return response;
    }

    public SnapshotDto captureSnapshot() {
        log.info("Capturing snapshot");
        SnapshotDto response = cameraClient.captureSnapshot();
        log.info("Snapshot received");
        return response;
    }

    // ---------------- СР2: імпорт і робота з базою ----------------

    /** Отримує список через Feign, перевіряє його, перетворює DTO → Entity і зберігає через Hibernate. */
    @Transactional
    public List<CameraEntity> importCameras() {
        log.info("Importing cameras from external API");
        CameraListDto response = cameraClient.getCameras();
        if (response == null || response.cameras() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "External API returned no cameras");
        }
        for (CameraItemDto dto : response.cameras()) {
            if (dto == null || dto.id() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "External API returned a camera without id");
            }
        }
        List<CameraEntity> entities = response.cameras().stream().map(this::toEntity).toList();
        List<CameraEntity> saved = cameraRepository.saveAll(entities);
        log.info("Imported {} cameras, total in database: {}", saved.size(), cameraRepository.count());
        return saved;
    }

    private CameraEntity toEntity(CameraItemDto dto) {
        CameraEntity entity = new CameraEntity();
        entity.setId(dto.id());
        entity.setName(dto.name());
        entity.setDeviceType(dto.deviceType());
        entity.setStatus(dto.status());
        if (dto.location() != null) {
            entity.setRoom(dto.location().room());
            entity.setFloor(dto.location().floor());
        }
        return entity;
    }

    @Transactional(readOnly = true)
    public List<CameraEntity> getSavedCameras() {
        return cameraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CameraEntity getSavedCamera(Long id) {
        return cameraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camera not found"));
    }

    @Transactional
    public void deleteSavedCamera(Long id) {
        cameraRepository.delete(getSavedCamera(id));
        log.info("Deleted camera {}", id);
    }
}
