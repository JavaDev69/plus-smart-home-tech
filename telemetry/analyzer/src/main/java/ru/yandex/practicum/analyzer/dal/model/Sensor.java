package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@Entity
@Table(name = "sensors")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sensor {
    @Id
    private String id;

    private String hubId;
}
