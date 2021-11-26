package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.event.block.SignChangeEvent;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerUpdateSignEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.Arrays;

@Data
public class SBukkitPlayerUpdateSignEvent implements SPlayerUpdateSignEvent, BukkitCancellable {
    private final SignChangeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder block;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    // TODO: IMPLEMENT PLATFORM ADVENTURE
    @Override
    public Component[] lines() {
        return Arrays.stream(event.getLines()).map(AdventureHelper::toComponent).toArray(Component[]::new);
    }

    @Override
    public Component line(@Range(from = 0, to = 3) int index) {
        return lines()[index];
    }

    @Override
    public void line(@Range(from = 0, to = 3) int index, Component component) {
        event.setLine(index, AdventureHelper.toLegacy(component));
    }
}
