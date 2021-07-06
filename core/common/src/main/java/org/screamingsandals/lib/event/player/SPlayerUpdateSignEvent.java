package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SPlayerUpdateSignEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final Component[] lines;
    private final BlockHolder block;

    public Component line(@Range(from = 0, to = 3) int index) {
        return lines[index];
    }

    public void line(@Range(from = 0, to = 3) int index, Component component) {
        lines[index] = component;
    }
}
