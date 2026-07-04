package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.scenario.DeviceActionType;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeviceActionType type;

    private Integer value;
}
