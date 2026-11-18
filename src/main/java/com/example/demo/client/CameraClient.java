package com.example.demo.client;

import com.example.demo.dto.CameraInfoDto;
import com.example.demo.dto.CameraListDto;
import com.example.demo.dto.SnapshotDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

/** Feign-клієнт зовнішнього сервісу DummyJSON. Реалізацію інтерфейсу створює OpenFeign. */
@FeignClient(name = "camera-client", url = "https://dummyjson.com/c")
public interface CameraClient {

    /** СР2: GET https://dummyjson.com/c/da8f-feef-4b02-9ca0 — список камер (шлях з application.properties). */
    @GetMapping("${camera.api.list-path}")
    CameraListDto getCameras();

    /** GET https://dummyjson.com/c/0716-c5cb-4391-afc3 — інформація про камеру. */
    @GetMapping("/0716-c5cb-4391-afc3")
    CameraInfoDto getCameraInfo();

    /** PUT https://dummyjson.com/c/382a-be11-44ef-8b0c — snapshot (JSON з полем image у Base64). */
    @PutMapping("/382a-be11-44ef-8b0c")
    SnapshotDto captureSnapshot();
}
