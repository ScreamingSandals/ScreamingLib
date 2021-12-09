package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.event.player.SPlayerEggThrowEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerEggThrowEvent implements SPlayerEggThrowEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerEggThrowEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityBasic egg;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public EntityBasic getEgg() {
        if (egg == null) {
            egg = EntityMapper.wrapEntity(event.getEgg()).orElseThrow();
        }
        return egg;
    }

    @Override
    public boolean isHatching() {
        return event.isHatching();
    }

    @Override
    public void setHatching(boolean hatching) {
        event.setHatching(hatching);
    }

    @Override
    public EntityTypeHolder getHatchType() {
        return EntityTypeHolder.of(event.getHatchingType());
    }

    @Override
    public void setHatchType(EntityTypeHolder hatchType) {
        event.setHatchingType(hatchType.as(EntityType.class));
    }

    @Override
    public byte getNumHatches() {
        return event.getNumHatches();
    }

    @Override
    public void setNumHatches(byte numHatches) {
        event.setNumHatches(numHatches);
    }
}
