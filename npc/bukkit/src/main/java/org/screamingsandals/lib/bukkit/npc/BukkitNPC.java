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
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.npc.NPCSkin;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BukkitNPC extends AbstractNPC {
    private final int id;
    private final BukkitDataWatcher dataWatcher;
    /**
     * hologram that displays the name of the entity
     */
    private final Hologram hologram;

    protected BukkitNPC(LocationHolder location) {
        super(location);
        id = EntityNMS.incrementAndGetId();

        hologram = Hologram.of(location.clone().add(0, 1.50, 0));
        if (isVisible()) {
            hologram.show();
        } else {
            hologram.hide();
        }

        dataWatcher = new BukkitDataWatcher(null);
        dataWatcher.register(DataWatcher.Item.of(SkinLayerValues.findLayerByVersion(), (byte) 127));
    }

    @Override
    public NPC setLocation(LocationHolder location) {
        super.setLocation(location);
        hologram.setLocation(location.clone().add(0, 1.5D, 0));
        getViewers().forEach(viewer -> getTeleportPacket().sendPacket(viewer));
        return this;
    }

    @Override
    public NPC setDisplayName(List<TextEntry> name) {
        super.setDisplayName(name);
        for (int i = 0; i < name.size(); i++) {
            hologram.replaceLine(i, name.get(i));
        }
        return this;
    }

    @Override
    public int getEntityId() {
        return id;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player) {
        getSpawnPackets().forEach(sPacket -> sPacket.sendPacket(player));
        hologram.addViewer(player);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player) {
        removeForPlayer(player);
        hologram.removeViewer(player);
    }

    @Override
    public void update0() {

    }

    private List<SPacket> getSpawnPackets() {
        final var toReturn = new LinkedList<SPacket>();

        final var playerInfoPacket = PacketMapper.createPacket(SPacketPlayOutPlayerInfo.class)
                .setAction(SPacketPlayOutPlayerInfo.Action.ADD_PLAYER)
                .setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                        1,
                        GameMode.SURVIVAL,
                        AdventureHelper.toComponent(getName()),
                        getGameProfile()
                )));
        toReturn.add(playerInfoPacket);

        final var spawnPacket = PacketMapper.createPacket(SPacketPlayOutNamedEntitySpawn.class)
                .setEntityId(id)
                .setUUID(getUUID())
                .setPitch(getLocation().getPitch())
                .setYaw(getLocation().getYaw())
                .setLocation(getLocation())
                .setDataWatcher(dataWatcher);
        toReturn.add(spawnPacket);

        final var metadataPacket = PacketMapper.createPacket(SPacketPlayOutEntityMetadata.class);
        metadataPacket.setMetaData(id, dataWatcher, true);
        toReturn.add(metadataPacket);

        final var scoreboardTeamPacket = PacketMapper.createPacket(SPacketPlayOutScoreboardTeam.class)
                .setTeamName(AdventureHelper.toComponent(getName()))
                .setDisplayName(AdventureHelper.toComponent(getName()))
                .setCollisionRule(SPacketPlayOutScoreboardTeam.CollisionRule.ALWAYS)
                .setTagVisibility(SPacketPlayOutScoreboardTeam.TagVisibility.NEVER)
                .setTeamColor(TextColor.color(0, 0, 0))
                .setTeamPrefix(Component.text(" "))
                .setTeamSuffix(Component.text(" "))
                .setFlags(false, false)
                .setMode(SPacketPlayOutScoreboardTeam.Mode.CREATE)
                .setEntities(Collections.singletonList(getName()));
        toReturn.add(scoreboardTeamPacket);

        Tasker.build(() -> {
            //remove npc from tablist
            playerInfoPacket
                    .setAction(SPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER)
                    .setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                            1,
                            GameMode.SURVIVAL,
                            AdventureHelper.toComponent(getName()),
                            getGameProfile()
                    )));
            getViewers().forEach(playerInfoPacket::sendPacket);
        }).delay(5L, TaskerTime.SECONDS).start();

        return toReturn;
    }

    private SPacketPlayOutEntityDestroy getFullDestroyPacket() {
        final int[] toRemove = {getEntityId()};
        return PacketMapper.createPacket(SPacketPlayOutEntityDestroy.class)
                .setEntitiesToDestroy(toRemove);
    }


    private void removeForPlayer(PlayerWrapper player) {
        if (!player.isOnline()) {
            return;
        }
        final var toSend = new LinkedList<SPacket>();
        toSend.add(getFullDestroyPacket());
        final var removeInfoPacket = PacketMapper.createPacket(SPacketPlayOutPlayerInfo.class)
                .setAction(SPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER)
                .setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                        1,
                        GameMode.SURVIVAL,
                        AdventureHelper.toComponent(getName()),
                        getGameProfile()
                )));
        toSend.add(removeInfoPacket);
        toSend.forEach(sPacket -> sPacket.sendPacket(player));
    }

    @Override
    public NPC show() {
        super.show();
        hologram.show();
        return this;
    }

    @Override
    public NPC hide() {
        getViewers().forEach(this::removeForPlayer);
        hologram.hide();
        super.hide();
        return this;
    }

    @Override
    public void lookAtPlayer(LocationHolder location, PlayerWrapper player) {
        final var bukkitNPCLocation = getLocation().as(Location.class);
        final var playerLocation = location.as(Location.class);

        Location direction = bukkitNPCLocation.clone().setDirection(playerLocation.clone().subtract(bukkitNPCLocation.clone()).toVector());
        final var lookPacket = PacketMapper.createPacket(SPacketPlayOutEntityLook.class)
                .setEntityId(getEntityId())
                .setYaw((byte) (direction.getYaw() * 256.0F / 360.0F))
                .setPitch((byte) (direction.getPitch() * 256.0F / 360.0F))
                .setOnGround(true);

        final var headRotationPacket = PacketMapper.createPacket(SPacketPlayOutEntityHeadRotation.class)
                .setEntityId(getEntityId())
                .setRotation((byte) (direction.getYaw() * 256.0F / 360.0F));

        lookPacket.sendPacket(player);
        headRotationPacket.sendPacket(player);
    }

    private SPacketPlayOutEntityTeleport getTeleportPacket() {
        return PacketMapper.createPacket(SPacketPlayOutEntityTeleport.class)
                .setEntityId(id)
                .setLocation(getLocation())
                .setIsOnGround(true);
    }

    @Override
    public NPC setSkin(@Nullable NPCSkin skin) {
        final var playerInfoPacket = PacketMapper.createPacket(SPacketPlayOutPlayerInfo.class)
                .setAction(SPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER)
                .setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                        1,
                        GameMode.SURVIVAL,
                        AdventureHelper.toComponent(getName()),
                        getGameProfile()
                )));
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(getFullDestroyPacket()::sendPacket);

        super.setSkin(skin);

        playerInfoPacket.setAction(SPacketPlayOutPlayerInfo.Action.ADD_PLAYER);
        playerInfoPacket.setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                1,
                GameMode.SURVIVAL,
                AdventureHelper.toComponent(getName()),
                getGameProfile()
        )));
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(viewer -> getSpawnPackets().forEach(sPacket -> sPacket.sendPacket(viewer)));

        Tasker.build(() -> {
            playerInfoPacket.setAction(SPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER);
            playerInfoPacket.setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                    1,
                    GameMode.SURVIVAL,
                    AdventureHelper.toComponent(getName()),
                    getGameProfile()
            )));
            getViewers().forEach(playerInfoPacket::sendPacket);
        }).delay(5L, TaskerTime.SECONDS).start();

        return this;
    }

    @RequiredArgsConstructor
    public enum SkinLayerValues {
        V9(13, 13),
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
