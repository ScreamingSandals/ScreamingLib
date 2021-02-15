package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.hologram.AbstractTextHologram;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class BukkitTextHologram extends AbstractTextHologram {
    private final Map<Integer, ArmorStandNMS> entitiesOnLines = new HashMap<>();
    private Location cachedLocation;

    public BukkitTextHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
        this.cachedLocation = location.as(Location.class);
    }

    public boolean hasId(Integer id) {
        return entitiesOnLines.values()
                .stream()
                .anyMatch(entity -> entity.getId() == id);
    }

    @Override
    public Hologram setLocation(LocationHolder locationHolder) {
        this.location = locationHolder;
        this.cachedLocation = locationHolder.as(Location.class);
        return this;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        try {
            update(player.as(Player.class), getAllSpawnPackets(), checkDistance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        try {
            update(player.as(Player.class), List.of(getFullDestroyPacket()), checkDistance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        updateEntities();
    }

    private void update(Player player, List<Object> packets, boolean checkDistance) {
        if (!player.getLocation().getWorld().equals(cachedLocation.getWorld())) {
            log.trace("World is different, doing nothing.");
            return;
        }

        if (checkDistance
                && player.getLocation().distanceSquared(cachedLocation) >= viewDistance) {
            log.trace("Out of view distance, doing nothing");
            return;
        }

        try {
            final var handler = ClassStorage.getMethod(player, "getHandle").invoke();
            final var connection = ClassStorage.getField(handler, "playerConnection,field_71135_a");
            final var sendPacket = ClassStorage.getMethod(connection, "sendPacket,func_147359_a", ClassStorage.NMS.Packet);
            packets.forEach(sendPacket::invoke);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void updateEntities() {
        log.trace("Updating entities");
        final var packets = new LinkedList<>();
        if (visible) {
            lines.forEach((key, value) -> {
                try {
                    if (entitiesOnLines.containsKey(key)) {
                        final var entityOnLine = entitiesOnLines.get(key);
                        if (entityOnLine.getCustomName().equals(value)) {
                            return;
                        }

                        entityOnLine.setCustomName(value);
                        final var metadataPacket = ClassStorage.NMS.PacketPlayOutEntityMetadata
                                .getConstructor(int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                                .newInstance(entityOnLine.getId(),
                                        entityOnLine.getDataWatcher(), false);
                        packets.add(metadataPacket);

                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .30, 0);
                        entityOnLine.setCustomName(value);
                        entityOnLine.setLocation(newLocation);

                        final var teleportPacket = ClassStorage.NMS.PacketPlayOutEntityTeleport
                                .getConstructor(ClassStorage.NMS.Entity)
                                .newInstance(entityOnLine.getHandler());
                        packets.add(teleportPacket);
                        return;
                    }

                    final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .30, 0);
                    final var entity = new ArmorStandNMS(newLocation);
                    entity.setCustomName(value);
                    entity.setCustomNameVisible(true);
                    entity.setInvisible(true);
                    entity.setSmall(!touchable);
                    entity.setArms(false);
                    entity.setBasePlate(false);
                    entity.setGravity(false);
                    entity.setMarker(!touchable);

                    final var spawnLivingPacket = ClassStorage.NMS.PacketPlayOutSpawnEntityLiving
                            .getConstructor(ClassStorage.NMS.EntityLiving)
                            .newInstance(entity.getHandler());
                    packets.add(spawnLivingPacket);

                    if (Version.isVersion(1, 15)) {
                        Object metadataPacket = ClassStorage.NMS.PacketPlayOutEntityMetadata
                                .getConstructor(int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                                .newInstance(entity.getId(),
                                        entity.getDataWatcher(), false);
                        packets.add(metadataPacket);
                    }

                    entitiesOnLines.put(key, entity);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }

        try {
            final var toRemove = new LinkedList<Integer>();
            if (entitiesOnLines.size() > lines.size()) {
                entitiesOnLines.forEach((key, value) -> {
                    if (!lines.containsKey(key)) {
                        toRemove.add(value.getId());
                    }
                });
            }

            final var destroyPacket = ClassStorage.NMS.PacketPlayOutEntityDestroy
                    .getConstructor(int[].class)
                    .newInstance(toRemove.stream()
                            .mapToInt(i -> i)
                            .toArray());
            packets.add(destroyPacket);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        viewers.forEach(viewer -> update(viewer.as(Player.class), packets, true));
    }

    private Object getFullDestroyPacket() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        final var toRemove = entitiesOnLines.values()
                .stream()
                .map(EntityNMS::getId)
                .mapToInt(i -> i)
                .toArray();
        return ClassStorage.NMS.PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(toRemove);
    }

    private List<Object> getAllSpawnPackets() {
        final var packets = new LinkedList<>();
        entitiesOnLines.forEach((line, entity) -> {
            try {
                packets.add(ClassStorage.NMS.PacketPlayOutSpawnEntityLiving.getConstructor(ClassStorage.NMS.EntityLiving).newInstance(entity.getHandler()));
                if (Version.isVersion(1, 15)) {
                    final var metadataPacket = ClassStorage.NMS.PacketPlayOutEntityMetadata
                            .getConstructor(int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                            .newInstance(entity.getId(), entity.getDataWatcher(), true);
                    packets.add(metadataPacket);
                }
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
        return packets;
    }
}
