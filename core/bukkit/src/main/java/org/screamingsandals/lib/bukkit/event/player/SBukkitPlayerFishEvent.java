package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.player.SPlayerFishEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerFishEvent implements SPlayerFishEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerFishEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityBasic entity;
    private boolean entityCached;
    private State state;
    private EntityBasic hookEntity;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    @Nullable
    public EntityBasic getEntity() {
        if (!entityCached) {
            if (event.getCaught() != null) {
                entity = EntityMapper.wrapEntity(event.getCaught()).orElseThrow();
            }
            entityCached = true;
        }
        return entity;
    }

    @Override
    public int getExp() {
        return event.getExpToDrop();
    }

    @Override
    public void setExp(int exp) {
        event.setExpToDrop(exp);
    }

    @Override
    public State getState() {
        if (state == null) {
            state = State.convert(event.getState().name());
        }
        return state;
    }

    @Override
    public EntityBasic getHookEntity() {
        if (hookEntity == null) {
            hookEntity = EntityMapper.wrapEntity(event.getHook()).orElseThrow();
        }
        return hookEntity;
    }
}
