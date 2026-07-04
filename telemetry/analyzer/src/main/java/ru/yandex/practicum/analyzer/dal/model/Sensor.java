package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "sensors")
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    @Id
    private String id;

    private String hubId;
}
