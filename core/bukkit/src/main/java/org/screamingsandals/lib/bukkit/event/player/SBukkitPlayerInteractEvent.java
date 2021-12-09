package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.BlockFace;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerInteractEvent implements SPlayerInteractEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerInteractEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Item item;
    private boolean itemCached;
    private Action action;
    private BlockFace blockFace;
    private BlockHolder clickedBlock;
    private boolean clickedBlockCached;
    private EquipmentSlotHolder hand;
    private boolean handCached;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @Nullable Item getItem() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = ItemFactory.build(event.getItem()).orElseThrow();
            }
            itemCached = true;
        }
        return item;
    }

    @Override
    public Action getAction() {
        if (action == null) {
            action = Action.convert(event.getAction().name());
        }
        return action;
    }

    @Override
    public @Nullable BlockHolder getBlockClicked() {
        if (!clickedBlockCached) {
            if (event.getClickedBlock() != null) {
                clickedBlock = BlockMapper.wrapBlock(event.getClickedBlock());
            }
            clickedBlockCached = true;
        }
        return clickedBlock;
    }

    @Override
    public BlockFace getBlockFace() {
        if (blockFace == null) {
            blockFace = BlockFace.valueOf(event.getBlockFace().name());
        }
        return blockFace;
    }

    @Override
    public Result getUseClickedBlock() {
        return Result.convert(event.useInteractedBlock().name());
    }

    @Override
    public void setUseClickedBlock(Result useClickedBlock) {
        event.setUseInteractedBlock(Event.Result.valueOf(useClickedBlock.name()));
    }

    @Override
    public Result getUseItemInHand() {
        return Result.convert(event.useItemInHand().name());
    }

    @Override
    public void setUseItemInHand(Result useItemInHand) {
        event.setUseItemInHand(Event.Result.valueOf(useItemInHand.name()));
    }

    @Override
    public @Nullable EquipmentSlotHolder getHand() {
        if (!handCached) {
            if (event.getHand() != null) {
                hand = EquipmentSlotHolder.of(event.getHand());
            }
            handCached = true;
        }
        return hand;
    }
}
