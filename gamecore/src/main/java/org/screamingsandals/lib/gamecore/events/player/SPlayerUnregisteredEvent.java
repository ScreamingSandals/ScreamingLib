package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerUnregisteredEvent extends BaseEvent {
    private final UUID uuid;
}