package org.screamingsandals.lib.bukkit.npc;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.npc.NPCSkin;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BukkitNPC extends AbstractNPC {
    private final int id;
    private final BukkitDataWatcher dataWatcher;

    protected BukkitNPC(LocationHolder location) {
        super(location);
        id = EntityNMS.incrementAndGetId();
        dataWatcher = new BukkitDataWatcher(null);
        dataWatcher.register(DataWatcher.Item.of(SkinLayerValues.findLayerByVersion(), (byte) 127));
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
    public void onViewerAdded(PlayerWrapper player) {
        sendSpawnPackets(player);
        getHologram().addViewer(player);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player) {
        removeForPlayer(player);
        getHologram().removeViewer(player);
    }

    @Override
    public void update0() {

    }

    private void sendSpawnPackets(PlayerWrapper player) {
        PacketMapper.createPacket(SClientboundSetPlayerTeamPacket.class)
                .setTeamName(AdventureHelper.toComponent(getName()))
                .setDisplayName(AdventureHelper.toComponent(getName()))
                .setCollisionRule(SClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS)
                .setTagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .setTeamColor(TextColor.color(0, 0, 0))
                .setTeamPrefix(Component.text(" "))
                .setTeamSuffix(Component.text(" "))
                .setFlags(false, true)
                .setMode(SClientboundSetPlayerTeamPacket.Mode.CREATE)
                .setEntities(Collections.singletonList(getName()))
                .sendPacket(player);

        PacketMapper.createPacket(SClientboundPlayerInfoPacket.class)
                .setAction(SClientboundPlayerInfoPacket.Action.ADD_PLAYER)
                .setPlayersData(getNPCInfoData())
                .sendPacket(player);

        PacketMapper.createPacket(SClientboundAddPlayerPacket.class)
                .setEntityId(id)
                .setUUID(getUUID())
                .setPitch(getLocation().getPitch())
                .setYaw(getLocation().getYaw())
                .setLocation(getLocation())
                .setDataWatcher(dataWatcher)
                .sendPacket(player);

        PacketMapper.createPacket(SClientboundSetEntityDataPacket.class)
                .setMetaData(id, dataWatcher, true)
                .sendPacket(player);


        Tasker.build(() -> {
            //remove npc from TabList
            PacketMapper.createPacket(SClientboundPlayerInfoPacket.class)
                    .setAction(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                    .setPlayersData(getNPCInfoData())
                    .sendPacket(player);
        }).delay(6L, TaskerTime.SECONDS).start();
    }

    private SClientboundRemoveEntitiesPacket getFullDestroyPacket() {
        final int[] toRemove = { getEntityId() };
        return PacketMapper.createPacket(SClientboundRemoveEntitiesPacket.class)
                .setEntitiesToDestroy(toRemove);
    }


    private void removeForPlayer(PlayerWrapper player) {
        if (!player.isOnline()) {
            return;
        }

        getFullDestroyPacket().sendPacket(player);
        PacketMapper.createPacket(SClientboundPlayerInfoPacket.class)
                .setAction(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .setPlayersData(getNPCInfoData())
                .sendPacket(player);
    }

    @Override
    public NPC hide() {
        getViewers().forEach(this::removeForPlayer);
        super.hide();
        return this;
    }

    @Override
    public void lookAtPlayer(LocationHolder location, PlayerWrapper player) {
        final var bukkitNPCLocation = getLocation().as(Location.class);
        final var playerLocation = location.as(Location.class);

        Location direction = bukkitNPCLocation.clone().setDirection(playerLocation.clone().subtract(bukkitNPCLocation.clone()).toVector());
        PacketMapper.createPacket(SClientboundMoveEntityPacket.Rot.class)
                .setEntityId(getEntityId())
                .setYaw((byte) (direction.getYaw() * 256.0F / 360.0F))
                .setPitch((byte) (direction.getPitch() * 256.0F / 360.0F))
                .setOnGround(true)
                .sendPacket(player);

        PacketMapper.createPacket(SClientboundRotateHeadPacket.class)
                .setEntityId(getEntityId())
                .setRotation((byte) (direction.getYaw() * 256.0F / 360.0F))
                .sendPacket(player);
    }

    private SClientboundTeleportEntityPacket getTeleportPacket() {
        return PacketMapper.createPacket(SClientboundTeleportEntityPacket.class)
                .setEntityId(id)
                .setLocation(getLocation())
                .setIsOnGround(true);
    }

    @Override
    public NPC setSkin(@Nullable NPCSkin skin) {
        final var playerInfoPacket = PacketMapper.createPacket(SClientboundPlayerInfoPacket.class)
                .setAction(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .setPlayersData(getNPCInfoData());

        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(getFullDestroyPacket()::sendPacket);

        super.setSkin(skin);

        playerInfoPacket.setAction(SClientboundPlayerInfoPacket.Action.ADD_PLAYER);
        playerInfoPacket.setPlayersData(getNPCInfoData());
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(this::sendSpawnPackets);

        Tasker.build(() -> {
            playerInfoPacket.setAction(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER);
            playerInfoPacket.setPlayersData(getNPCInfoData());
            getViewers().forEach(playerInfoPacket::sendPacket);
        }).delay(6L, TaskerTime.SECONDS).start();

        return this;
    }

    private List<SClientboundPlayerInfoPacket.PlayerInfoData> getNPCInfoData() {
        return Collections.singletonList(new SClientboundPlayerInfoPacket.PlayerInfoData(
                1,
                GameMode.SURVIVAL,
                AdventureHelper.toComponent(getName()),
                getGameProfile()
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
