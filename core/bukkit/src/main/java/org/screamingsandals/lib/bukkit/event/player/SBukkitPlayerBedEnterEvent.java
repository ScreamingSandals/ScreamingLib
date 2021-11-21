package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.event.player.SPlayerBedEnterEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerBedEnterEvent implements SPlayerBedEnterEvent, NoAutoCancellable {
    private final PlayerBedEnterEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder bed;
    private BedEnterResult bedEnterResult;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder getBed() {
        if (bed == null) {
            bed = BlockMapper.wrapBlock(event.getBed());
        }
        return bed;
    }

    @Override
    public BedEnterResult getBedEnterResult() {
        if (bedEnterResult == null) {
            bedEnterResult = BedEnterResult.convert(event.getBedEnterResult().name());
        }
        return bedEnterResult;
    }

    @Override
    public Result getUseBed() {
        return Result.convert(event.useBed().name());
    }

    @Override
    public void setUseBed(Result useBed) {
        event.setUseBed(Event.Result.valueOf(useBed.name()));
    }
}
