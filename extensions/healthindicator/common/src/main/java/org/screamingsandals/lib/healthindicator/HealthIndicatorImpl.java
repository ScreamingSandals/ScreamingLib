/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.healthindicator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.HumanEntity;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.ClientboundSetDisplayObjectivePacket;
import org.screamingsandals.lib.packet.ClientboundSetObjectivePacket;
import org.screamingsandals.lib.packet.ClientboundSetScorePacket;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractVisual;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class HealthIndicatorImpl extends AbstractVisual<HealthIndicator> implements HealthIndicator {
    private final @NotNull String underNameTagKey;
    private final @NotNull String tabListKey;
    private final @NotNull ConcurrentSkipListMap<@NotNull String, Integer> values = new ConcurrentSkipListMap<>();
    protected final @NotNull List<@NotNull Player> trackedPlayers = new ArrayList<>();
    @Accessors(chain = true, fluent = true)
    @Getter
    @Setter
    protected @Nullable DataContainer data;
    protected volatile boolean ready;
    protected volatile boolean healthInTabList;
    protected volatile @NotNull Component symbol = Component.empty();
    protected @Nullable Task task;

    public HealthIndicatorImpl(@NotNull UUID uuid) {
        super(uuid);
        this.underNameTagKey = generateObjectiveKey();
        this.tabListKey = generateObjectiveKey();
    }

    @Override
    public @NotNull HealthIndicator addTrackedPlayer(@NotNull Player player) {
        if (!trackedPlayers.contains(player)) {
            trackedPlayers.add(player);
            if (visible && ready && task == null) {
                update();
            }
        }
        return this;
    }

    @Override
    public @NotNull HealthIndicator removeTrackedPlayer(@NotNull Player player) {
        if (trackedPlayers.contains(player)) {
            trackedPlayers.remove(player);
            if (visible && ready && task == null) {
                update();
            }
        }
        return this;
    }

    @Override
    public @NotNull HealthIndicator symbol(@NotNull ComponentLike symbol) {
        return symbol(symbol.asComponent());
    }

    @Override
    public @NotNull HealthIndicator showHealthInTabList(boolean flag) {
        this.healthInTabList = flag;
        return this;
    }

    @Override
    public @NotNull HealthIndicator symbol(@NotNull Component symbol) {
        this.symbol = symbol;
        if (visible && ready) {
            updateSymbol0();
        }
        return this;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }

        return !data.isEmpty();
    }

    @Contract("_ -> this")
    @Override
    public @NotNull HealthIndicator update(@NotNull UpdateStrategy strategy) {
        if (ready) {
            var packets = new ArrayList<AbstractPacket>();

            var trackedPlayers = List.copyOf(this.trackedPlayers);

            List.copyOf(values.keySet()).stream().filter(s -> trackedPlayers.stream().noneMatch(p -> p.getName().equals(s))).forEach(s -> {
                values.remove(s);
                packets.add(getDestroyScorePacket(s).objectiveKey(underNameTagKey).build());
                if (healthInTabList) {
                    packets.add(getDestroyScorePacket(s).objectiveKey(tabListKey).build());
                }
            });

            trackedPlayers.forEach(playerWrapper -> {
                if (!playerWrapper.isOnline()) {
                    removeViewer(playerWrapper);
                    return;
                }

                var health = (int) Math.round(playerWrapper.as(HumanEntity.class).getHealth());
                var key = playerWrapper.getName();
                if (!values.containsKey(key) || values.get(key) != health) {
                    values.put(key, health);
                    packets.add(createScorePacket(key, health).objectiveKey(underNameTagKey).build());
                    if (healthInTabList) {
                        packets.add(createScorePacket(key, health).objectiveKey(tabListKey).build());
                    }
                }
            });

            packets.forEach(packet -> packet.sendPacket(viewers));
        } else {
            viewers.forEach(viewer -> onViewerRemoved(viewer, false));
        }
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull HealthIndicator show() {
        ready = true;
        visible = true;
        viewers.forEach(a -> onViewerAdded(a, false));
        update();
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull HealthIndicator hide() {
        visible = false;
        ready = false;
        update();
        return this;
    }

    @Override
    public void destroy() {
        data = null;
        if (task != null) {
            task.cancel();
            task = null;
        }
        hide();
        viewers.clear();
        trackedPlayers.clear();
        values.clear();
        HealthIndicatorManager.removeHealthIndicator(this);
    }

    @Override
    public @NotNull HealthIndicator startUpdateTask(long time, @NotNull TaskerTime unit) {
        if (task != null) {
            task.cancel();
        }

        task = Tasker.runAsyncRepeatedly(() -> update(), time, unit);
        return this;
    }

    protected void updateSymbol0() {
        if (visible) {
            getUpdateObjectivePacket()
                    .objectiveKey(underNameTagKey)
                    .build()
                    .sendPacket(viewers);

            if (healthInTabList) {
                getUpdateObjectivePacket()
                        .objectiveKey(tabListKey)
                        .build()
                        .sendPacket(viewers);
            }
        }
    }

    @Override
    public void onViewerAdded(@NotNull Player player, boolean checkDistance) {
        if (visible) {
            getCreateObjectivePacket()
                    .objectiveKey(underNameTagKey)
                    .build()
                    .sendPacket(player);

            ClientboundSetDisplayObjectivePacket.builder()
                    .objectiveKey(underNameTagKey)
                    .slot(ClientboundSetDisplayObjectivePacket.DisplaySlot.BELOW_NAME)
                    .build()
                    .sendPacket(player);

            values.forEach((s, integer) -> createScorePacket(s, integer).objectiveKey(underNameTagKey).build().sendPacket(player));

            if (healthInTabList) {
                getCreateObjectivePacket()
                        .objectiveKey(tabListKey)
                        .build()
                        .sendPacket(player);

                ClientboundSetDisplayObjectivePacket.builder()
                        .objectiveKey(tabListKey)
                        .slot(ClientboundSetDisplayObjectivePacket.DisplaySlot.PLAYER_LIST)
                        .build()
                        .sendPacket(player);

                values.forEach((s, integer) -> createScorePacket(s, integer).objectiveKey(tabListKey).build().sendPacket(player));
            }
        }
    }

    @Override
    public void onViewerRemoved(@NotNull Player player, boolean checkDistance) {
        getDestroyObjectivePacket()
                .objectiveKey(underNameTagKey)
                .build()
                .sendPacket(player);

        if (healthInTabList) {
            getDestroyObjectivePacket()
                    .objectiveKey(tabListKey)
                    .build()
                    .sendPacket(player);
        }
    }

    private @NotNull ClientboundSetObjectivePacket.ClientboundSetObjectivePacketBuilder getNotFinalObjectivePacket() {
        return ClientboundSetObjectivePacket.builder()
                .title(symbol.asComponent())
                .criteriaType(ClientboundSetObjectivePacket.Type.INTEGER);
    }

    private @NotNull ClientboundSetObjectivePacket.ClientboundSetObjectivePacketBuilder getCreateObjectivePacket() {
        return getNotFinalObjectivePacket().mode(ClientboundSetObjectivePacket.Mode.CREATE);
    }

    private @NotNull ClientboundSetObjectivePacket.ClientboundSetObjectivePacketBuilder getUpdateObjectivePacket() {
        return getNotFinalObjectivePacket().mode(ClientboundSetObjectivePacket.Mode.UPDATE);
    }

    private @NotNull ClientboundSetObjectivePacket.ClientboundSetObjectivePacketBuilder getDestroyObjectivePacket() {
        return ClientboundSetObjectivePacket.builder()
                .mode(ClientboundSetObjectivePacket.Mode.DESTROY);
    }

    private @NotNull ClientboundSetScorePacket.ClientboundSetScorePacketBuilder createScorePacket(@NotNull String key, int score) {
        return ClientboundSetScorePacket.builder()
                .entityName(key)
                .score(score)
                .action(ClientboundSetScorePacket.ScoreboardAction.CHANGE);
    }

    private @NotNull ClientboundSetScorePacket.ClientboundSetScorePacketBuilder getDestroyScorePacket(@NotNull String key) {
        return ClientboundSetScorePacket.builder()
                .entityName(key)
                .action(ClientboundSetScorePacket.ScoreboardAction.REMOVE);
    }

    private static String generateObjectiveKey() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(16)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
