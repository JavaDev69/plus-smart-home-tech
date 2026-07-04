package ru.yandex.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.dto.scenario.DeviceAction;
import ru.yandex.practicum.dto.scenario.ScenarioCondition;

import java.util.List;

/**
 * Событие добавления сценария в систему. Содержит информацию о названии сценария, условиях и действиях.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    @Length(min = 3)
    private String name;
    @NotEmpty
    private List<ScenarioCondition> conditions;
    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
