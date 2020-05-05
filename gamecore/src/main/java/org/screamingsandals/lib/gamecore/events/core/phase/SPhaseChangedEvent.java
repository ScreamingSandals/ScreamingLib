package org.screamingsandals.lib.gamecore.events.core.phase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPhaseChangedEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final GameFrame gameFrame;
    private final GamePhase currentPhase;
    private final GamePhase previousPhase;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
