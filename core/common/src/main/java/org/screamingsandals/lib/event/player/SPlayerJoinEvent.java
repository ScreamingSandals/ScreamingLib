package org.screamingsandals.lib.event.player;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerJoinEvent extends AbstractEvent {
    private final PlayerWrapper player;
    @Nullable
    private Component joinMessage;
}
