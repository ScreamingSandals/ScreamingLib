package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SPlayerUpdateSignEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final Component[] lines;
    private final ImmutableObjectLink<BlockHolder> block;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public BlockHolder getBlock() {
        return block.get();
    }

    public Component line(@Range(from = 0, to = 3) int index) {
        return lines[index];
    }

    public void line(@Range(from = 0, to = 3) int index, Component component) {
        lines[index] = component;
    }
}
