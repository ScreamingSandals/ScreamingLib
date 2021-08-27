package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SPlayerUpdateSignEvent extends SPlayerCancellableEvent {
    private final Component[] lines;
    private final ImmutableObjectLink<BlockHolder> block;

    public SPlayerUpdateSignEvent(ImmutableObjectLink<PlayerWrapper> player,
                                  Component[] lines,
                                  ImmutableObjectLink<BlockHolder> block) {
        super(player);
        this.lines = lines;
        this.block = block;
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
