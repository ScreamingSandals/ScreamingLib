package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.hologram.nms.AdvancedArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class BukkitHologram extends AbstractHologram {
    private final Map<Integer, ArmorStandNMS> entitiesOnLines = new HashMap<>();
    private AdvancedArmorStandNMS itemEntity;
    private TaskerTask rotationTask;
    private Location cachedLocation;

    public BukkitHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
        this.cachedLocation = location.as(Location.class);
    }

    public boolean hasId(Integer id) {
        return entitiesOnLines.values()
                .stream()
                .anyMatch(entity -> entity.getId() == id);
    }

    @Override
    public Hologram setLocation(LocationHolder location) {
        this.location = location;
        this.cachedLocation = location.as(Location.class);
        return this;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean shouldCheckDistance) {
        try {
            update(player.as(Player.class), getAllSpawnPackets(), shouldCheckDistance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean shouldCheckDistance) {
        try {
            update(player.as(Player.class), List.of(getFullDestroyPacket()), shouldCheckDistance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        updateEntities();
    }

    @Override
    public Hologram show() {
        super.show();
        if (rotationMode != RotationMode.NONE) {
            rotationTask = Tasker.build(() -> {
                if (itemEntity == null) {
                    log.trace("Item entity is null");
                    return;
                }

                itemEntity.setRotation(checkAndAdd(itemEntity.getRotation()));
            }).repeat(rotationTime.getFirst(), rotationTime.getSecond()).start();
        }

        return this;
    }

    @Override
    public Hologram hide() {
        viewers.forEach(viewer -> {
            try {
                update(viewer.as(Player.class), List.of(getFullDestroyPacket()), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (rotationTask != null) {
            rotationTask.cancel();
        }
        return this;
    }

    @Override
    public void destroy() {
        if (rotationTask != null) {
            log.trace("Cancelling!");
            rotationTask.cancel();
        }
        super.destroy();
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
        if (visible && viewers.size() > 0) {
            lines.forEach((key, value) -> {
                try {
                    if (entitiesOnLines.containsKey(key)) {
                        final var entityOnLine = entitiesOnLines.get(key);
                        if (entityOnLine.getCustomName().equals(value)
                                && originalLinesSize == lines.size()) {
                            return;
                        }

                        entityOnLine.setCustomName(value);
                        final var metadataPacket = ClassStorage.NMS.PacketPlayOutEntityMetadata
                                .getConstructor(int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                                .newInstance(entityOnLine.getId(),
                                        entityOnLine.getDataWatcher(), false);
                        packets.add(metadataPacket);

                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0);
                        entityOnLine.setCustomName(value);
                        entityOnLine.setLocation(newLocation);

                        final var teleportPacket = ClassStorage.NMS.PacketPlayOutEntityTeleport
                                .getConstructor(ClassStorage.NMS.Entity)
                                .newInstance(entityOnLine.getHandler());
                        packets.add(teleportPacket);
                    } else {
                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0);
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
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }

        try {
            if (rotationMode != RotationMode.NONE) {
               if (itemEntity == null) {
                   final var newLocation = cachedLocation.clone().add(0, lines.size() * .25, 0);
                   final var entity = new AdvancedArmorStandNMS(newLocation);
                   entity.setInvisible(true);
                   entity.setArms(false);
                   entity.setBasePlate(false);
                   entity.setGravity(false);
                   entity.setMarker(false);
                   entity.setItem(item);

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

                   this.itemEntity = entity;
               }
            }

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

    private Vector3Df checkAndAdd(Vector3Df in) {
        final var toReturn = new Vector3Df();
        switch (rotationMode) {
            case X:
                toReturn.setX(checkAndIncrement(in.getX()));
                break;
            case Y:
                toReturn.setY(checkAndIncrement(in.getY()));
                break;
            case Z:
                toReturn.setZ(checkAndIncrement(in.getZ()));
                break;
            case ALL:
                toReturn.setX(checkAndIncrement(in.getX()));
                toReturn.setY(checkAndIncrement(in.getY()));
                toReturn.setZ(checkAndIncrement(in.getZ()));
                break;
        }

        return toReturn;
    }

    private float checkAndIncrement(float in) {
        if (in >= 360) {
            return 0f;
        } else {
            return in + 10f;
        }
    }
}
