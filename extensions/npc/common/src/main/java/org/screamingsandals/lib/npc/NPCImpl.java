/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.npc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.npc.skin.NPCSkin;
import org.screamingsandals.lib.npc.skin.SkinLayerValues;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.player.gamemode.GameMode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.tasker.DefaultThreads;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.ProxyType;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractTouchableVisual;
import org.screamingsandals.lib.world.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * The standard implementation of the {@link  NPC} interface.
 */
@Accessors(fluent = true)
@Getter
@Setter
public class NPCImpl extends AbstractTouchableVisual<NPC> implements NPC {
    private static final @NotNull GameMode GAME_MODE = GameMode.of("SURVIVAL");
    private static final boolean IS_BUNGEE = Server.getProxyType() == ProxyType.BUNGEE;

    private final int entityId;
    private final @NotNull Hologram hologram;
    private final @NotNull String tabName16;
    private @NotNull Component tabListName;
    @Getter(AccessLevel.NONE)
    private final @NotNull List<ClientboundPlayerInfoPacket. @NotNull Property> properties;
    private @Nullable NPCSkin skin;
    private volatile boolean lookAtPlayer;
    private final @NotNull List<@NotNull MetadataItem> metadata;
    private ClientboundSetPlayerTeamPacket.@NotNull CollisionRule collisionRule;
    private final @NotNull Map<@NotNull UUID, Task> hiderTask;
    private double hologramElevation;

    public NPCImpl(@NotNull UUID uuid, @NotNull Location location, boolean touchable) {
        super(uuid, location, touchable);

        try {
            this.entityId = Entities.getNewEntityIdSynchronously().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        this.hologramElevation = 1.5D;
        this.tabName16 = "[NPC] " + uuid.toString().replace("-", "").substring(0, 10);
        this.tabListName = Component.fromLegacy(tabName16);
        this.hologram = HologramManager.hologram(location.add(0.0D, hologramElevation, 0.0D));
        this.metadata = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.hiderTask = new ConcurrentHashMap<>();
        this.collisionRule = ClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS;
        metadata.add(MetadataItem.of((byte) SkinLayerValues.findLayerByVersion(), (byte) 127));
    }

    @Override
    public @NotNull NPC hologramElevation(double hologramElevation) {
        this.hologramElevation = hologramElevation;
        update(UpdateStrategy.POSITION);
        return this;
    }

    @Override
    public @NotNull NPC skin(@Nullable NPCSkin skin) {
        this.skin = skin;
        properties.removeIf(property -> "textures".equals(property.name()));
        if (skin != null) {
            properties.add(
                    new ClientboundPlayerInfoPacket.Property(
                            "textures",
                            skin.getValue(),
                            skin.getSignature()
                    )
            );
        }

        if (shown()) {
            // recreate the NPC to show it's newly set skin.
            viewers.forEach(this::onViewerRemoved);
            viewers.forEach(this::onViewerAdded);
        }
        return this;
    }


    @Override
    public void onViewerAdded(@NotNull Player viewer, boolean checkDistance) {
        if (shown() && viewer.isOnline()) {
            hologram.addViewer(viewer);
            createSpawnPackets().forEach(packet -> packet.sendPacket(viewer));
            if (!Server.isVersion(1, 19, 3) || viewer.getProtocolVersion() < 761) {
                scheduleTabHide(viewer);
            }
        }
    }

    @Override
    public void onViewerRemoved(@NotNull Player viewer, boolean checkDistance) {
        if (viewer.isOnline()) {
            hologram.removeViewer(viewer);
            createPlayerTeamPacket(ClientboundSetPlayerTeamPacket.Mode.REMOVE).sendPacket(viewer);
            createPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER).sendPacket(viewer);
            removeEntityPacket().sendPacket(viewer);
            cancelTabHide(viewer);
        }
    }

    @Override
    public void lookAtLocation(@NotNull Location location, @NotNull Player player) {
        final var direction = location().setDirection(player.getLocation().subtract(location()).asVector());
        ClientboundMoveEntityPacket.Rot.builder()
                .entityId(entityId())
                .yaw((byte) (direction.getYaw() * 256.0F / 360.0F))
                .pitch((byte) (direction.getPitch() * 256.0F / 360.0F))
                .onGround(true)
                .build()
                .sendPacket(player);

        ClientboundRotateHeadPacket.builder()
                .entityId(entityId())
                .headYaw(direction.getYaw())
                .build()
                .sendPacket(player);
    }

    @Override
    public @Unmodifiable @Nullable List<@NotNull TextEntry> displayName() {
        return List.copyOf(hologram.lines().values());
    }

    @Override
    public @NotNull NPC displayName(@NotNull List<@NotNull Component> name) {
        hologram.setLines(name);
        return this;
    }

    @Override
    public boolean hasId(int entityId) {
        return this.entityId == entityId;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull NPC update(@NotNull UpdateStrategy strategy) {
        if (shown()) {
            switch (strategy) {
                case POSITION:
                    hologram.location(location().add(0.0D, 1.5D, 0.0D));
                    hologram.update(UpdateStrategy.POSITION);
                    ClientboundTeleportEntityPacket.builder()
                            .location(location())
                            .entityId(entityId)
                            .onGround(true)
                            .build()
                            .sendPacket(viewers());
                    break;
                case ALL:
                    viewers.forEach(this::onViewerRemoved);
                    viewers.forEach(this::onViewerAdded);
            }
        }
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull NPC show() {
        if (visible) {
            return this;
        }
        visible = true;
        hologram.show();
        viewers.forEach(this::onViewerAdded);
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull NPC hide() {
        if (!visible) {
            return this;
        }
        visible = false;
        hologram.hide();
        viewers.forEach(this::onViewerRemoved);
        return this;
    }

    @Override
    public void destroy() {
        super.destroy();
        hologram.destroy();
        viewers.forEach(this::cancelTabHide);
        viewers.clear();
        NPCManager.removeNPC(this);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull NPC title(@NotNull Component title) {
        hologram.title(title);
        return this;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull NPC title(@NotNull ComponentLike title) {
        hologram.title(title);
        return this;
    }
    private void cancelTabHide(@NotNull Player viewer) {
        hiderTask.computeIfPresent(viewer.getUuid(), (uuid, task) -> {
            if (task.getState() == TaskState.RUNNING
                    || task.getState() == TaskState.SCHEDULED) {
                task.cancel();
            }
            return null;
        });
    }

    private void scheduleTabHide(@NotNull Player viewer) {
        cancelTabHide(viewer);
        Tasker.runDelayed(DefaultThreads.GLOBAL_THREAD, () -> { // TODO: does this have to run on main thread?
            if (!viewer.isOnline()) {
                return;
            }
            //remove npc from TabList
            ClientboundPlayerInfoPacket.builder()
                    .action(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                    .data(getNPCInfoData())
                    .build()
                    .sendPacket(viewer);
        }, 6L, TaskerTime.SECONDS);
    }

    private @NotNull List<@NotNull AbstractPacket> createSpawnPackets() {
        final List<AbstractPacket> spawnPackets = new ArrayList<>();

        if (IS_BUNGEE) {
            spawnPackets.add(
                    createPlayerTeamPacket(ClientboundSetPlayerTeamPacket.Mode.REMOVE)
            );
        }

        spawnPackets.add(createPlayerTeamPacket(ClientboundSetPlayerTeamPacket.Mode.CREATE));
        spawnPackets.add(createPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER));

        // protocol < 550: sending metadata as part of AddPlayerPacket; protocol >= 550, packet lib will split this packet into to as supposed to be
        spawnPackets.add(
                ClientboundAddPlayerPacket.builder()
                        .entityId(entityId)
                        .uuid(uuid())
                        .location(location())
                        .metadata(metadata)
                        .build()
        );

        spawnPackets.add(
                ClientboundMoveEntityPacket.Rot.builder()
                        .entityId(entityId())
                        .yaw((byte) (location().getYaw() * 256.0F / 360.0F))
                        .pitch((byte) (location().getPitch() * 256.0F / 360.0F))
                        .onGround(true)
                        .build()
        );

        spawnPackets.add(
                ClientboundRotateHeadPacket.builder()
                        .entityId(entityId())
                        .headYaw(location().getYaw())
                        .build()
        );

        return spawnPackets;
    }

    private @NotNull ClientboundRemoveEntitiesPacket removeEntityPacket() {
        return ClientboundRemoveEntitiesPacket.builder()
                .entityIds(new int[]{entityId()})
                .build();
    }

    private @NotNull ClientboundPlayerInfoPacket createPlayerInfoPacket(
            ClientboundPlayerInfoPacket.@NotNull Action action
    ) {
        return ClientboundPlayerInfoPacket.builder()
                .action(action)
                .data(getNPCInfoData())
                .build();
    }

    private @NotNull ClientboundSetPlayerTeamPacket createPlayerTeamPacket(
            ClientboundSetPlayerTeamPacket.@NotNull Mode mode
    ) {
        return ClientboundSetPlayerTeamPacket
                .builder()
                .teamKey(tabName16)
                .mode(mode)
                .displayName(tabListName)
                .collisionRule(collisionRule)
                .tagVisibility(ClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .teamColor(ClientboundSetPlayerTeamPacket.TeamColor.BLACK)
                .teamPrefix(Component.empty())
                .teamSuffix(Component.empty())
                .friendlyFire(false)
                .seeInvisible(true)
                .entities(Collections.singletonList(tabName16))
                .build();
    }

    private @NotNull List<ClientboundPlayerInfoPacket.@NotNull PlayerInfoData> getNPCInfoData() {
        return Collections.singletonList(new ClientboundPlayerInfoPacket.PlayerInfoData(
                uuid(),
                tabName16,
                1,
                GAME_MODE,
                tabListName,
                List.copyOf(properties),
                false
        ));
    }
}
