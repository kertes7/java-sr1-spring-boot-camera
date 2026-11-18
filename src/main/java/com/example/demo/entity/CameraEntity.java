package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Рядок таблиці cameras. ID приходить із зовнішнього сервісу, тому @GeneratedValue не потрібна:
 * повторний імпорт тієї самої камери оновлює запис, а не створює дубль.
 * Вкладений location зберігається двома окремими стовпцями room і floor.
 */
@Entity
@Table(name = "cameras")
@Getter
@Setter
public class CameraEntity {

    @Id
    private Long id;
    private String name;
    private String deviceType;
    private String status;
    private String room;
    private Integer floor;
}
