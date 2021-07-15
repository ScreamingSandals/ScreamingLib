package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.screamingsandals.lib.bukkit.hologram.nms.FakeArmorStandNMS;
import org.screamingsandals.lib.bukkit.hologram.nms.FakeEntityNMS;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
public class BukkitHologram extends AbstractHologram {
    private final Map<Integer, FakeArmorStandNMS> entitiesOnLines = new ConcurrentHashMap<>();
    private FakeArmorStandNMS itemEntity;
    private TaskerTask rotationTask;
    private LocationHolder cachedLocation;

    BukkitHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
        this.cachedLocation = location;
    }

    public boolean hasId(int id) {
        return entitiesOnLines.values()
                .stream()
                .anyMatch(entity -> entity.getId() == id);
    }

    @Override
    public Hologram setLocation(LocationHolder location) {
        this.location = location;
        this.cachedLocation = location;
        return this;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean shouldCheckDistance) {
        if (visible && entitiesOnLines.size() != lines.size()) { // fix if you show hologram and then you add viewers
            updateEntities();
        }
        update(player, getAllSpawnPackets(), shouldCheckDistance);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean shouldCheckDistance) {
        removeForPlayer(player);
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

                final var metadataPacket = PacketMapper.createPacket(SPacketPlayOutEntityMetadata.class)
                        .setMetaData(itemEntity.getId(), itemEntity.getDataWatcher(), false);

                viewers.forEach(player -> update(player, List.of(metadataPacket), false));

            }).repeat(rotationTime.getFirst(), rotationTime.getSecond())
                    .async()
                    .start();
        }

        return this;
    }

    @Override
    public Hologram hide() {
        viewers.forEach(this::removeForPlayer);

        if (rotationTask != null) {
            rotationTask.cancel();
            rotationTask = null;
        }
        super.hide();
        return this;
    }

    @Override
    public Hologram item(Item item) {
        super.item(item);
        if (ready) {
            viewers.forEach(player -> update(player, List.of(getEquipmentPacket(itemEntity, item)), true));
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

    @Override
    protected void update0() {
        updateEntities();
    }

    private void update(PlayerWrapper player, List<SPacket> packets, boolean checkDistance) {
        if (!player.getLocation().getWorld().equals(cachedLocation.getWorld())) {
            log.trace("World is different, doing nothing.");
            return;
        }

        if (checkDistance
                && player.getLocation().getDistanceSquared(cachedLocation) >= viewDistance) {
            log.trace("Out of view distance, doing nothing");
            return;
        }

        packets.forEach(packet -> packet.sendPacket(player));
    }

    private void updateEntities() {
        log.trace("Updating entities");
        final var packets = new LinkedList<SPacket>();
        if (visible && viewers.size() > 0) {
            if (lines.size() != originalLinesSize
                    && itemEntity != null) {
                itemEntity.setLocation(cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                        ? (-lines.size() * .25 - .5)
                        : (lines.size() * .25), 0).as(Location.class));

                 packets.add(getTeleportPacket(itemEntity));
            }

            lines.forEach((key, value) -> {
                try {
                    if (entitiesOnLines.containsKey(key)) {
                        final var entityOnLine = entitiesOnLines.get(key);
                        if (entityOnLine.getCustomName().equals(value)
                                && originalLinesSize == lines.size()) {
                            return;
                        }

                        entityOnLine.setCustomName(value.getText());

                        final var metadataPacket = PacketMapper.createPacket(SPacketPlayOutEntityMetadata.class)
                                .setMetaData(entityOnLine.getId(), entityOnLine.getDataWatcher(), false);
                        packets.add(metadataPacket);

                        entityOnLine.setCustomName(value.getText());
                        entityOnLine.setLocation(cachedLocation.clone().add(0, (lines.size() - key) * .25, 0).as(Location.class));
                        packets.add(getTeleportPacket(entityOnLine));
                    } else {
                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0).as(Location.class);
                        final var entity = new FakeArmorStandNMS(newLocation);
                        entity.setCustomName(value.getText());
                        entity.setCustomNameVisible(true);
                        entity.setInvisible(true);
                        entity.setSmall(!touchable);
                        entity.setArms(false);
                        entity.setBasePlate(false);
                        entity.setGravity(false);
                        entity.setMarker(!touchable);
                        packets.addAll(getSpawnPacket(entity));

                        entitiesOnLines.put(key, entity);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });

            try {
                if (rotationMode != RotationMode.NONE) {
                    if (itemEntity == null) {
                        final var newLocation = cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                                ? (-lines.size() * .25 - .5)
                                : (lines.size() * .25), 0).as(Location.class);
                        final var entity = new FakeArmorStandNMS(newLocation);
                        entity.setInvisible(true);
                        entity.setSmall(!touchable);
                        entity.setArms(false);
                        entity.setBasePlate(false);
                        entity.setGravity(false);
                        entity.setMarker(!touchable);

                        packets.addAll(getSpawnPacket(entity));
                        packets.add(getEquipmentPacket(entity, item));

                        this.itemEntity = entity;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
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

            final var destroyPacket = PacketMapper.createPacket(SPacketPlayOutEntityDestroy.class);
            int[] arr = toRemove
                    .stream()
                    .mapToInt(i -> i)
                    .toArray();

            if (arr != null && arr.length > 0) {
                destroyPacket.setEntitiesToDestroy(toRemove
                        .stream()
                        .mapToInt(i -> i)
                        .toArray()
                );
                packets.add(destroyPacket);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        viewers.forEach(viewer -> update(viewer, packets, true));
    }

    private SPacketPlayOutEntityEquipment getEquipmentPacket(FakeArmorStandNMS entity, Item item) {
        return PacketMapper.createPacket(SPacketPlayOutEntityEquipment.class)
                .setEntityId(entity.getId())
                .setItemAndSlot(item, SPacketPlayOutEntityEquipment.Slot.HEAD);
    }

    private SPacketPlayOutEntityTeleport getTeleportPacket(FakeArmorStandNMS entity) {
        return PacketMapper.createPacket(SPacketPlayOutEntityTeleport.class)
                .setEntityId(entity.getId())
                .setLocation(LocationMapper.wrapLocation(entity.getLocation()))
                .setIsOnGround(entity.isOnGround());
    }

    private List<SPacket> getSpawnPacket(FakeArmorStandNMS entity) {
        final var toReturn = new LinkedList<SPacket>();

        final var spawnPacket = PacketMapper.createPacket(SPacketPlayOutSpawnEntityLiving.class)
                .setEntityId(entity.getId())
                .setUUID(entity.getUuid())
                .setType(entity.getTypeId())
                .setPitch(entity.getLocation().getPitch())
                .setYaw(entity.getLocation().getYaw())
                .setVelocity(new Vector3D(0,0,0))
                .setHeadYaw(3.9f)
                .setLocation(LocationMapper.wrapLocation(entity.getLocation()))
                .setDataWatcher(entity.getDataWatcher());
        toReturn.add(spawnPacket);

        final var metadataPacket = PacketMapper.createPacket(SPacketPlayOutEntityMetadata.class);
        metadataPacket.setMetaData(entity.getId(), entity.getDataWatcher(), true);
        toReturn.add(metadataPacket);

        return toReturn;
    }

    private SPacketPlayOutEntityDestroy getFullDestroyPacket() throws IllegalArgumentException, SecurityException {
        final var lines = entitiesOnLines.values()
                .stream()
                .map(FakeEntityNMS::getId);

        final int[] toRemove;
        if (itemEntity != null) {
            toRemove = Stream.concat(lines, Stream.of(itemEntity.getId()))
                    .mapToInt(i -> i)
                    .toArray();
        } else {
            toRemove = lines.mapToInt(i -> i).toArray();
        }

        final var destroyPacket = PacketMapper.createPacket(SPacketPlayOutEntityDestroy.class);
        if (toRemove != null && toRemove.length > 0) {
            destroyPacket.setEntitiesToDestroy(toRemove);
        }
        return destroyPacket;
    }

    private List<SPacket> getAllSpawnPackets() {
        final var packets = new LinkedList<SPacket>();
        entitiesOnLines.forEach((line, entity) -> packets.addAll(getSpawnPacket(entity)));
        if (itemEntity != null) {
            packets.addAll(getSpawnPacket(itemEntity));
            packets.add(getEquipmentPacket(itemEntity, item));
        }
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
            case XY:
                toReturn.setX(checkAndIncrement(in.getX()));
                toReturn.setY(checkAndIncrement(in.getY()));
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
        if (in >= 370) {
            return 0f;
        } else {
            return in + rotationIncrement;
        }
    }

    private void removeForPlayer(PlayerWrapper player) {
        if (!player.isOnline()) {
            return;
        }

        final var toSend = new LinkedList<SPacket>();
        if (itemEntity != null) {
            rotationTask.cancel();
        }

        toSend.add(getFullDestroyPacket());
        update(player, toSend, false);
    }
}
