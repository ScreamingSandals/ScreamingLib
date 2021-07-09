package org.screamingsandals.lib.bukkit.npc;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.npc.NPCSkin;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

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

        int index = 13;
        if (Version.isVersion(1, 17)) {
            index = 16;
        } else if (Version.isVersion(1, 14)) {
            index = 15;
        } else if (Version.isVersion(1, 13)) {
            index = 13;
        }

        dataWatcher = new BukkitDataWatcher(null);
        dataWatcher.register(DataWatcher.Item.of(index, (byte) 128));
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

        final var playerInfoPacket = PacketMapper.createPacket(SPacketPlayOutPlayerInfo.class);
        playerInfoPacket.setAction(SPacketPlayOutPlayerInfo.Action.ADD_PLAYER);
        playerInfoPacket.setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                1,
                GameMode.SURVIVAL,
                AdventureHelper.toComponent(getName()),
                getGameProfile()
        )));
        toReturn.add(playerInfoPacket);

        final var spawnPacket = PacketMapper.createPacket(SPacketPlayOutNamedEntitySpawn.class);
        spawnPacket.setEntityId(id);
        spawnPacket.setUUID(getUUID());
        spawnPacket.setPitch(getLocation().getPitch());
        spawnPacket.setYaw(getLocation().getYaw());
        spawnPacket.setLocation(getLocation());
        spawnPacket.setDataWatcher(dataWatcher);
        toReturn.add(spawnPacket);

        final var metadataPacket = PacketMapper.createPacket(SPacketPlayOutEntityMetadata.class);
        metadataPacket.setMetaData(id, dataWatcher, true);
        toReturn.add(metadataPacket);

        final var scoreboardTeamPacket = PacketMapper.createPacket(SPacketPlayOutScoreboardTeam.class);
        scoreboardTeamPacket.setTeamName(AdventureHelper.toComponent(getName()));
        scoreboardTeamPacket.setDisplayName(AdventureHelper.toComponent(getName()));
        scoreboardTeamPacket.setCollisionRule(SPacketPlayOutScoreboardTeam.CollisionRule.NEVER);
        scoreboardTeamPacket.setTagVisibility(SPacketPlayOutScoreboardTeam.TagVisibility.NEVER);
        scoreboardTeamPacket.setTeamColor(TextColor.color(0,0,0));
        scoreboardTeamPacket.setTeamPrefix(Component.text(" "));
        scoreboardTeamPacket.setTeamSuffix(Component.text(" "));
        scoreboardTeamPacket.setFlags(false, false);
        scoreboardTeamPacket.setMode(SPacketPlayOutScoreboardTeam.Mode.CREATE);
        scoreboardTeamPacket.setEntities(Collections.singletonList(getName()));
        toReturn.add(scoreboardTeamPacket);

        return toReturn;
    }

    private SPacketPlayOutEntityDestroy getFullDestroyPacket() {
        final int[] toRemove = { getEntityId() };
        final var destroyPacket = PacketMapper.createPacket(SPacketPlayOutEntityDestroy.class);
        destroyPacket.setEntitiesToDestroy(toRemove);
        return destroyPacket;
    }


    private void removeForPlayer(PlayerWrapper player) {
        if (!player.isOnline()) {
            return;
        }
        final var toSend = new LinkedList<SPacket>();
        toSend.add(getFullDestroyPacket());
        final var removeInfoPacket = PacketMapper.createPacket(SPacketPlayOutPlayerInfo.class);
        removeInfoPacket.setAction(SPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER);
        removeInfoPacket.setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
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
    public void rotateHead(LocationHolder location) {

    }

    private SPacketPlayOutEntityTeleport getTeleportPacket() {
        final var packet = PacketMapper.createPacket(SPacketPlayOutEntityTeleport.class);
        packet.setEntityId(id);
        packet.setLocation(getLocation());
        packet.setIsOnGround(true);
        return packet;
    }

    @Override
    public NPC setSkin(@Nullable NPCSkin skin) {
        super.setSkin(skin);
        final var playerInfoPacket = PacketMapper.createPacket(SPacketPlayOutPlayerInfo.class);
        playerInfoPacket.setAction(SPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER);
        playerInfoPacket.setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
            1,
                GameMode.SURVIVAL,
                AdventureHelper.toComponent(getName()),
                getGameProfile()
        )));
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(getFullDestroyPacket()::sendPacket);

        playerInfoPacket.setAction(SPacketPlayOutPlayerInfo.Action.ADD_PLAYER);
        playerInfoPacket.setPlayersData(Collections.singletonList(new SPacketPlayOutPlayerInfo.PlayerInfoData(
                1,
                GameMode.SURVIVAL,
                AdventureHelper.toComponent(getName()),
                getGameProfile()
        )));
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(viewer -> getSpawnPackets().forEach(sPacket -> sPacket.sendPacket(viewer)));
        return this;
    }
}
