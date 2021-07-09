package org.screamingsandals.lib.bukkit.npc;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.LinkedList;
import java.util.List;

public class BukkitNPC extends AbstractNPC {
    private final EntityNMS entity;
    /**
     * hologram that displays the name of the entity
     */
    private final Hologram hologram;

    protected BukkitNPC(LocationHolder location) {
        super(location);
        entity = new EntityNMS(location);
        hologram = Hologram.of(LocationMapper.wrapLocation(entity.getLocation().clone().add(0, 1.25, 0)));
        if (isVisible()) {
            hologram.show();
        } else {
            hologram.hide();
        }
    }

    @Override
    public NPC setLocation(@NotNull LocationHolder location) {
        super.setLocation(location);
        entity.setLocation(location.as(Location.class));
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
        return entity.getId();
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
        final var spawnPacket = PacketMapper.createPacket(SPacketPlayOutNamedEntitySpawn.class);
        spawnPacket.setEntityId(entity.getId());
        spawnPacket.setUUID(entity.getUniqueId());
        spawnPacket.setPitch(entity.getLocation().getPitch());
        spawnPacket.setYaw(entity.getLocation().getYaw());
        spawnPacket.setLocation(LocationMapper.wrapLocation(entity.getLocation()));
        spawnPacket.setDataWatcher(new BukkitDataWatcher(entity.getDataWatcher()));
        toReturn.add(spawnPacket);

        if (Version.isVersion(1, 15)) {
            final var metadataPacket = PacketMapper.createPacket(SPacketPlayOutEntityMetadata.class);
            metadataPacket.setMetaData(entity.getId(), new BukkitDataWatcher(entity.getDataWatcher()), true);
            toReturn.add(metadataPacket);
        }

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

    private SPacketPlayOutEntityTeleport getTeleportPacket() {
        final var packet = PacketMapper.createPacket(SPacketPlayOutEntityTeleport.class);
        packet.setEntityId(entity.getId());
        packet.setLocation(LocationMapper.wrapLocation(entity.getLocation()));
        packet.setIsOnGround(entity.isOnGround());
        return packet;
    }
}
