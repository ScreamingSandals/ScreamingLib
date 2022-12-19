/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.npc.skin.NPCSkin;
import org.screamingsandals.lib.npc.skin.SkinLayerValues;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.ProxyType;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractTouchableVisual;
import org.screamingsandals.lib.world.LocationHolder;

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
    private static final GameModeHolder GAME_MODE = GameModeHolder.of("SURVIVAL");
    private static final boolean IS_BUNGEE = Server.getProxyType() == ProxyType.BUNGEE;

    private final int entityId;
    private final Hologram hologram;
    private final String tabName16;
    private Component tabListName;
    @Getter(AccessLevel.NONE)
    private final List<SClientboundPlayerInfoPacket.Property> properties;
    private NPCSkin skin;
    private volatile boolean lookAtPlayer;
    private final List<MetadataItem> metadata;
    private SClientboundSetPlayerTeamPacket.CollisionRule collisionRule;
    private final Map<UUID, TaskerTask> hiderTask;
    private double hologramElevation;

    public NPCImpl(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);

        try {
            this.entityId = EntityMapper.getNewEntityIdSynchronously().get();
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
        this.collisionRule = SClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS;
        metadata.add(MetadataItem.of((byte) SkinLayerValues.findLayerByVersion(), (byte) 127));
    }

    @Override
    public NPC hologramElevation(double hologramElevation) {
        this.hologramElevation = hologramElevation;
        update(UpdateStrategy.POSITION);
        return this;
    }

    @Override
    public NPC skin(NPCSkin skin) {
        this.skin = skin;
        properties.removeIf(property -> property.name().equals("textures"));
        if (skin != null) {
            properties.add(
                    new SClientboundPlayerInfoPacket.Property(
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
    public void onViewerAdded(@NotNull PlayerWrapper viewer, boolean checkDistance) {
        if (shown() && viewer.isOnline()) {
            hologram.addViewer(viewer);
            createSpawnPackets().forEach(packet -> packet.sendPacket(viewer));
            if (!Server.isVersion(1, 19, 3) || viewer.getProtocolVersion() < 761) {
                scheduleTabHide(viewer);
            }
        }
    }

    @Override
    public void onViewerRemoved(@NotNull PlayerWrapper viewer, boolean checkDistance) {
        if (viewer.isOnline()) {
            hologram.removeViewer(viewer);
            createPlayerTeamPacket(SClientboundSetPlayerTeamPacket.Mode.REMOVE).sendPacket(viewer);
            createPlayerInfoPacket(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER).sendPacket(viewer);
            removeEntityPacket().sendPacket(viewer);
            cancelTabHide(viewer);
        }
    }

    @Override
    public void lookAtLocation(LocationHolder location, PlayerWrapper player) {
        final var direction = location().setDirection(player.getLocation().subtract(location()).asVector());
        new SClientboundMoveEntityPacket.Rot()
                .entityId(entityId())
                .yaw((byte) (direction.getYaw() * 256.0F / 360.0F))
                .pitch((byte) (direction.getPitch() * 256.0F / 360.0F))
                .onGround(true)
                .sendPacket(player);

        new SClientboundRotateHeadPacket()
                .entityId(entityId())
                .headYaw(direction.getYaw())
                .sendPacket(player);
    }

    @Nullable
    @Override
    public List<TextEntry> displayName() {
        return List.copyOf(hologram.lines().values());
    }

    @Override
    public NPC displayName(List<Component> name) {
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
                    new SClientboundTeleportEntityPacket()
                            .location(location())
                            .entityId(entityId)
                            .onGround(true)
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
        return null;
    }
    private void cancelTabHide(PlayerWrapper viewer) {
        hiderTask.computeIfPresent(viewer.getUuid(), (uuid, task) -> {
            if (task.getState() == TaskState.RUNNING
                    || task.getState() == TaskState.SCHEDULED) {
                task.cancel();
            }
            return null;
        });
    }

    private void scheduleTabHide(PlayerWrapper viewer) {
        cancelTabHide(viewer);
        Tasker.build(() -> {
            if (!viewer.isOnline()) {
                return;
            }
            //remove npc from TabList
            new SClientboundPlayerInfoPacket()
                    .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                    .data(getNPCInfoData())
                    .sendPacket(viewer);
        }).delay(6L, TaskerTime.SECONDS).start();
    }

    private List<AbstractPacket> createSpawnPackets() {
        final List<AbstractPacket> spawnPackets = new ArrayList<>();

        if (IS_BUNGEE) {
            spawnPackets.add(
                    createPlayerTeamPacket(SClientboundSetPlayerTeamPacket.Mode.REMOVE)
            );
        }

        spawnPackets.add(createPlayerTeamPacket(SClientboundSetPlayerTeamPacket.Mode.CREATE));
        spawnPackets.add(createPlayerInfoPacket(SClientboundPlayerInfoPacket.Action.ADD_PLAYER));

        // protocol < 550: sending metadata as part of AddPlayerPacket; protocol >= 550, packet lib will split this packet into to as supposed to be
        spawnPackets.add(
                new SClientboundAddPlayerPacket()
                        .entityId(entityId)
                        .uuid(uuid())
                        .location(location())
                        .metadata(metadata)
        );

        spawnPackets.add(
                new SClientboundMoveEntityPacket.Rot()
                        .entityId(entityId())
                        .yaw((byte) (location().getYaw() * 256.0F / 360.0F))
                        .pitch((byte) (location().getPitch() * 256.0F / 360.0F))
                        .onGround(true)
        );

        spawnPackets.add(
                new SClientboundRotateHeadPacket()
                        .entityId(entityId())
                        .headYaw(location().getYaw())
        );

        return spawnPackets;
    }

    private SClientboundRemoveEntitiesPacket removeEntityPacket() {
        return new SClientboundRemoveEntitiesPacket()
                .entityIds(new int[]{entityId()});
    }

    private SClientboundPlayerInfoPacket createPlayerInfoPacket(
            SClientboundPlayerInfoPacket.Action action
    ) {
        return new SClientboundPlayerInfoPacket()
                .action(action)
                .data(getNPCInfoData());
    }

    private SClientboundSetPlayerTeamPacket createPlayerTeamPacket(
            SClientboundSetPlayerTeamPacket.Mode mode
    ) {
        return new SClientboundSetPlayerTeamPacket()
                .teamKey(tabName16)
                .mode(mode)
                .displayName(tabListName)
                .collisionRule(collisionRule)
                .tagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .teamColor(SClientboundSetPlayerTeamPacket.TeamColor.BLACK)
                .teamPrefix(Component.empty())
                .teamSuffix(Component.empty())
                .friendlyFire(false)
                .seeInvisible(true)
                .entities(Collections.singletonList(tabName16));
    }

    private List<SClientboundPlayerInfoPacket.PlayerInfoData> getNPCInfoData() {
        return Collections.singletonList(new SClientboundPlayerInfoPacket.PlayerInfoData(
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
