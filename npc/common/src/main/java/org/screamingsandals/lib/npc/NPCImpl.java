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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.ProxyType;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.impl.AbstractTouchableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Accessors(fluent = true)
@Getter
@Setter
public class NPCImpl extends AbstractTouchableVisual<NPC> implements NPC {
    private static final GameModeHolder GAME_MODE = GameModeHolder.of("SURVIVAL");
    private static final boolean IS_BUNGEE = Server.getProxyType() == ProxyType.BUNGEE;

    private final int entityId;
    private final Hologram hologram;
    private Component tabListName;

    @Getter(AccessLevel.NONE)
    private final List<SClientboundPlayerInfoPacket.Property> properties;

    private NPCSkin skin;
    private volatile boolean lookAtPlayer;
    private final List<MetadataItem> metadata;

    private SClientboundSetPlayerTeamPacket.CollisionRule collisionRule;

    public NPCImpl(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);

        try {
            this.entityId = EntityMapper.getNewEntityIdSynchronously().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        this.tabListName = AdventureHelper.toComponent("[NPC] " + uuid.toString().replace("-", "").substring(0, 10));
        this.hologram = HologramManager.hologram(location.clone().add(0.0D, 1.5D, 0.0D));
        this.metadata = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.collisionRule = SClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS;
        metadata.add(MetadataItem.of((byte) SkinLayerValues.findLayerByVersion(), (byte) 127));
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
    public NPC skin(NPCSkin skin) {
        if (!shown()) {
            this.skin = skin;
            properties.removeIf(property -> property.name().equals("textures"));
            if (skin == null) {
                return this;
            }
            properties.add(new SClientboundPlayerInfoPacket.Property("textures", skin.getValue(), skin.getSignature()));
            return this;
        }

        final var playerInfoPacket = new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .data(getNPCInfoData());

        playerInfoPacket.sendPacket(viewers);
        getFullDestroyPacket().sendPacket(viewers);

        this.skin = skin;
        properties.removeIf(property -> property.name().equals("textures"));
        if (skin == null) {
            return this;
        }
        properties.add(new SClientboundPlayerInfoPacket.Property("textures", skin.getValue(), skin.getSignature()));

        playerInfoPacket.action(SClientboundPlayerInfoPacket.Action.ADD_PLAYER);
        playerInfoPacket.data(getNPCInfoData());

        playerInfoPacket.sendPacket(viewers);
        getSpawnPackets().forEach(packet -> packet.sendPacket(viewers));
        scheduleTabHide(viewers);
        return this;
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

    @Override
    public boolean hasId(int entityId) {
        return this.entityId == entityId;
    }

    @Override
    public NPC update() {
        if (created()) {
            // TODO: update
        }
        return this;
    }

    @Override
    public NPC show() {
        if (visible) {
            return this;
        }

        if (destroyed()) {
            throw new UnsupportedOperationException("Cannot call NPC#show() for destroyed npcs!");
        }

        visible = true;
        hologram.show();
        viewers.forEach(viewer -> onViewerAdded(viewer, false));
        return this;
    }

    @Override
    public NPC hide() {
        if (!visible) {
            return this;
        }

        visible = false;
        hologram.hide();
        viewers.forEach(viewer -> onViewerRemoved(viewer, false));
        return this;
    }

    @Override
    public NPC title(Component title) {
        hologram.title(title);
        return this;
    }

    @Override
    public NPC title(ComponentLike title) {
        hologram.title(title);
        return null;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (!player.isOnline()) {
            return;
        }

        hologram.addViewer(player);
        getSpawnPackets().forEach(packet -> packet.sendPacket(player));
        scheduleTabHide(player);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (!player.isOnline()) {
            return;
        }

        hologram.removeViewer(player);
        getFullDestroyPacket().sendPacket(player);

        new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .data(getNPCInfoData())
                .sendPacket(player);

        new SClientboundSetPlayerTeamPacket()
                .teamKey(AdventureHelper.toLegacy(tabListName))
                .mode(SClientboundSetPlayerTeamPacket.Mode.REMOVE)
                .displayName(tabListName)
                .collisionRule(collisionRule)
                .tagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .teamColor(NamedTextColor.BLACK)
                .teamPrefix(Component.empty())
                .teamSuffix(Component.empty())
                .friendlyFire(false)
                .seeInvisible(true)
                .entities(Collections.singletonList(AdventureHelper.toLegacy(tabListName)))
                .sendPacket(player);
    }

    @Override
    public NPC spawn() {
        show();
        return this;
    }

    @Override
    public void destroy() {
        if (destroyed()) {
            return;
        }

        super.destroy();
        hide();
        viewers.clear();
        hologram.destroy();
        NPCManager.removeNPC(this);
    }

    private List<AbstractPacket> getSpawnPackets() {
        final List<AbstractPacket> spawnPackets = new ArrayList<>();

        if (IS_BUNGEE) {
           final var p1 = new SClientboundSetPlayerTeamPacket()
                    .teamKey(AdventureHelper.toLegacy(tabListName))
                    .mode(SClientboundSetPlayerTeamPacket.Mode.REMOVE)
                    .displayName(tabListName)
                    .collisionRule(collisionRule)
                    .tagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                    .teamColor(NamedTextColor.BLACK)
                    .teamPrefix(Component.empty())
                    .teamSuffix(Component.empty())
                    .friendlyFire(false)
                    .seeInvisible(true)
                    .entities(Collections.singletonList(AdventureHelper.toLegacy(tabListName)));

           spawnPackets.add(p1);
        }

        final var p2 = new SClientboundSetPlayerTeamPacket()
                .teamKey(AdventureHelper.toLegacy(tabListName))
                .mode(SClientboundSetPlayerTeamPacket.Mode.CREATE)
                .displayName(IS_BUNGEE ? Component.empty() : tabListName)
                .collisionRule(collisionRule)
                .tagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .teamColor(NamedTextColor.BLACK)
                .teamPrefix(Component.empty())
                .teamSuffix(Component.empty())
                .friendlyFire(false)
                .seeInvisible(true)
                .entities(Collections.singletonList(AdventureHelper.toLegacy(tabListName)));

        spawnPackets.add(p2);

        final var p3 = new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.ADD_PLAYER)
                .data(getNPCInfoData());

        spawnPackets.add(p3);

        // protocol < 550: sending metadata as part of AddPlayerPacket; protocol >= 550, packet lib will split this packet into to as supposed to be
        final var p4 = new SClientboundAddPlayerPacket()
                .entityId(entityId)
                .uuid(uuid())
                .location(location())
                .metadata(metadata);

        spawnPackets.add(p4);

        final var p5 = new SClientboundMoveEntityPacket.Rot()
                .entityId(entityId())
                .yaw((byte) (location().getYaw() * 256.0F / 360.0F))
                .pitch((byte) (location().getPitch() * 256.0F / 360.0F))
                .onGround(true);

        spawnPackets.add(p5);

        final var p6 = new SClientboundRotateHeadPacket()
                .entityId(entityId())
                .headYaw(location().getYaw());

        spawnPackets.add(p6);

        return spawnPackets;
    }

    private void scheduleTabHide(Collection<PlayerWrapper> viewers) {
        Tasker.build(() -> {
            //remove npc from TabList
            new SClientboundPlayerInfoPacket()
                    .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                    .data(getNPCInfoData())
                    .sendPacket(viewers);
        }).delay(6L, TaskerTime.SECONDS).start();
    }

    private void scheduleTabHide(PlayerWrapper viewer) {
        scheduleTabHide(List.of(viewer));
    }

    private SClientboundRemoveEntitiesPacket getFullDestroyPacket() {
        return new SClientboundRemoveEntitiesPacket()
                .entityIds(new int[] { entityId() });
    }

    private List<SClientboundPlayerInfoPacket.PlayerInfoData> getNPCInfoData() {
        return Collections.singletonList(new SClientboundPlayerInfoPacket.PlayerInfoData(
                uuid(),
                AdventureHelper.toLegacy(tabListName),
                1,
                GAME_MODE,
                tabListName,
                List.copyOf(properties)
        ));
    }
}
