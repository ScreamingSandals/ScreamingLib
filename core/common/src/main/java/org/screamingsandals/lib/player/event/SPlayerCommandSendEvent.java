package org.screamingsandals.lib.player.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerCommandSendEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final List<Component> commands;

    public SPlayerCommandSendEvent(@NotNull final PlayerWrapper player, @NotNull final Collection<String> commands) {
        this.player = player;
        this.commands = commands.stream().map(AdventureHelper::toComponent).collect(Collectors.toList());
    }
}
