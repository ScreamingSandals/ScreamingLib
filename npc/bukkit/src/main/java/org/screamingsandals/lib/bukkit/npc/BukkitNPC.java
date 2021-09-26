package org.screamingsandals.lib.bukkit.npc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.npc.NPCSkin;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
public class BukkitNPC extends AbstractNPC {
    private final int id;
    private final List<MetadataItem> metadata;

    protected BukkitNPC(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
        if (!Server.isServerThread()) {
            try {
                this.id = EntityMapper.getNewEntityIdSynchronously().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.id = EntityMapper.getNewEntityId();
        }
        this.metadata = new ArrayList<>();
        log.trace("Initialized BukkitNPC of id: {}", id);
        metadata.add(MetadataItem.of((byte) SkinLayerValues.findLayerByVersion(), (byte) 127));
        log.trace("Added Second Skin MetaData value {}", SkinLayerValues.findLayerByVersion());
    }

    @Override
    public NPC setLocation(LocationHolder location) {
        super.setLocation(location);
        getViewers().forEach(viewer -> getTeleportPacket().sendPacket(viewer));
        return this;
    }

    @Override
    public int getEntityId() {
        return id;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        sendSpawnPackets(player);
        getHologram().onViewerAdded(player, checkDistance);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        removeForPlayer(player);
        getHologram().onViewerRemoved(player, checkDistance);
    }

    private void sendSpawnPackets(PlayerWrapper player) {
        new SClientboundSetPlayerTeamPacket()
                .teamKey(getName())
                .mode(SClientboundSetPlayerTeamPacket.Mode.CREATE)
                .displayName(AdventureHelper.toComponent(getName()))
                .collisionRule(SClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS)
                .tagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .teamColor(NamedTextColor.BLACK)
                .teamPrefix(Component.empty())
                .teamSuffix(Component.empty())
                .friendlyFire(false)
                .seeInvisible(true)
                .entities(Collections.singletonList(getName()))
                .sendPacket(player);

        new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.ADD_PLAYER)
                .data(getNPCInfoData())
                .sendPacket(player);

        // protocol < 550: sending metadata as part of AddPlayerPacket; protocol >= 550, packet lib will split this packet into to as supposed to be
        new SClientboundAddPlayerPacket()
                .entityId(id)
                .uuid(getUUID())
                .location(getLocation())
                .metadata(metadata)
                .sendPacket(player);

        Tasker.build(() -> {
            //remove npc from TabList
            new SClientboundPlayerInfoPacket()
                    .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                    .data(getNPCInfoData())
                    .sendPacket(player);
        }).delay(6L, TaskerTime.SECONDS).start();

        new SClientboundMoveEntityPacket.Rot()
                .entityId(getEntityId())
                .yaw((byte) (getLocation().getYaw() * 256.0F / 360.0F))
                .pitch((byte) (getLocation().getPitch() * 256.0F / 360.0F))
                .onGround(true)
                .sendPacket(player);

        new SClientboundRotateHeadPacket()
                .entityId(getEntityId())
                .headYaw(getLocation().getYaw())
                .sendPacket(player);
    }

    private SClientboundRemoveEntitiesPacket getFullDestroyPacket() {
        return new SClientboundRemoveEntitiesPacket()
                .entityIds(new int[] {getEntityId()});
    }


    private void removeForPlayer(PlayerWrapper player) {
        if (!player.isOnline()) {
            return;
        }

        getFullDestroyPacket().sendPacket(player);
        new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .data(getNPCInfoData())
                .sendPacket(player);
    }

    @Override
    public NPC hide() {
        getViewers().forEach(this::removeForPlayer);
        super.hide();
        return this;
    }

    @Override
    public NPC clearViewers() {
        hide();
        getViewers().forEach(this::removeViewer);
        return this;
    }

    @Override
    public NPC title(Component title) {
        getHologram().title(title);
        return this;
    }

    @Override
    public NPC title(ComponentLike title) {
        getHologram().title(title);
        return null;
    }

    @Override
    public void lookAtPlayer(LocationHolder location, PlayerWrapper player) {
        final var bukkitNPCLocation = getLocation().as(Location.class);
        final var playerLocation = location.as(Location.class);

        Location direction = bukkitNPCLocation.clone().setDirection(playerLocation.clone().subtract(bukkitNPCLocation.clone()).toVector());
        new SClientboundMoveEntityPacket.Rot()
                .entityId(getEntityId())
                .yaw((byte) (direction.getYaw() * 256.0F / 360.0F))
                .pitch((byte) (direction.getPitch() * 256.0F / 360.0F))
                .onGround(true)
                .sendPacket(player);

        new SClientboundRotateHeadPacket()
                .entityId(getEntityId())
                .headYaw(direction.getYaw())
                .sendPacket(player);
    }

    private SClientboundTeleportEntityPacket getTeleportPacket() {
        return new SClientboundTeleportEntityPacket()
                .entityId(id)
                .location(getLocation())
                .onGround(true);
    }

    @Override
    public NPC setSkin(@Nullable NPCSkin skin) {
        log.trace("Updating NPC skin to: {}", skin);
        final var playerInfoPacket = new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .data(getNPCInfoData());

        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(getFullDestroyPacket()::sendPacket);

        super.setSkin(skin);

        playerInfoPacket.action(SClientboundPlayerInfoPacket.Action.ADD_PLAYER);
        playerInfoPacket.data(getNPCInfoData());
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(this::sendSpawnPackets);

        Tasker.build(() -> {
            playerInfoPacket.action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER);
            playerInfoPacket.data(getNPCInfoData());
            getViewers().forEach(playerInfoPacket::sendPacket);
        }).delay(6L, TaskerTime.SECONDS).start();

        return this;
    }

    private List<SClientboundPlayerInfoPacket.PlayerInfoData> getNPCInfoData() {
        return Collections.singletonList(new SClientboundPlayerInfoPacket.PlayerInfoData(
                getUUID(),
                getName(),
                1,
                GameModeHolder.of("SURVIVAL"),
                AdventureHelper.toComponent(getName()),
                List.copyOf(properties)
        ));
    }

    @RequiredArgsConstructor
    public enum SkinLayerValues {
        V9(12, 8),
        V13(13, 13),
        V14(15, 14),
        V16(16, 15),
        V17(17, 17);

        private final int layerValue;
        private final int minVersion;

        public static int findLayerByVersion() {
            return Arrays.stream(values())
                    .sorted(Collections.reverseOrder())
                    .filter(value -> Version.isVersion(1, value.minVersion))
                    .map(value -> value.layerValue)
                    .findAny()
                    .orElse(V9.layerValue);
        }
    }
}
