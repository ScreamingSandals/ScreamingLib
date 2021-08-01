package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
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
    private final Map<Integer, HologramPiece> entitiesOnLines = new ConcurrentHashMap<>();
    private HologramPiece itemEntity;
    private TaskerTask rotationTask;
    private LocationHolder cachedLocation;

    BukkitHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
        this.cachedLocation = location;
        log.trace("Initialized hologram of uuid: {} in location: {}", uuid, location);
    }

    @Override
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
        log.trace("Added viewer {} to hologram: {}", player.getName(), uuid);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean shouldCheckDistance) {
        removeForPlayer(player);
        log.trace("Removed viewer {} from hologram: {}", player.getName(), uuid);
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

                itemEntity.setHeadPose(checkAndAdd(itemEntity.getHeadPose()));

                final var metadataPacket = new SClientboundSetEntityDataPacket()
                        .entityId(itemEntity.getId());
                metadataPacket.metadata().addAll(itemEntity.getMetadataItems());

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

    private void update(PlayerWrapper player, List<AbstractPacket> packets, boolean checkDistance) {
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
        log.trace("Update entities for hologram of uuid: {}", uuid);
        log.trace("Visible: {}, Viewer count: {}", visible, viewers.size());

        final var packets = new LinkedList<AbstractPacket>();
        if (visible && viewers.size() > 0) {
            if (lines.size() != originalLinesSize
                    && itemEntity != null) {
                itemEntity.setLocation(cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                        ? (-lines.size() * .25 - .5)
                        : (lines.size() * .25), 0));

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

                        final var metadataPacket = new SClientboundSetEntityDataPacket()
                                .entityId(entityOnLine.getId());
                        metadataPacket.metadata().addAll(entityOnLine.getMetadataItems());
                        packets.add(metadataPacket);

                        entityOnLine.setCustomName(value.getText());
                        entityOnLine.setLocation(cachedLocation.clone().add(0, (lines.size() - key) * .25, 0));
                        packets.add(getTeleportPacket(entityOnLine));
                    } else {
                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0);
                        final var entity = new HologramPiece(newLocation);
                        log.trace("Creating new ArmorStand entity of id {} for hologram: {} of text: {}", uuid, entity.getId(), value.getText());
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
                        log.trace("Spawning Rotating Entity!");
                        final var newLocation = cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                                ? (-lines.size() * .25 - .5)
                                : (lines.size() * .25), 0);
                        final var entity = new HologramPiece(newLocation);
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

            final var destroyPacket = new SClientboundRemoveEntitiesPacket();
            int[] arr = toRemove
                    .stream()
                    .mapToInt(i -> i)
                    .toArray();

            if (arr != null && arr.length > 0) {
                destroyPacket.entityIds(toRemove
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

    private SClientboundSetEquipmentPacket getEquipmentPacket(HologramPiece entity, Item item) {
        var packet = new SClientboundSetEquipmentPacket()
                .entityId(entity.getId());
        packet.slots().put(EquipmentSlotMapping.resolve("HEAD").orElseThrow(), item);
        return packet;
    }

    private SClientboundTeleportEntityPacket getTeleportPacket(HologramPiece entity) {
        return new SClientboundTeleportEntityPacket()
                .entityId(entity.getId())
                .location(LocationMapper.wrapLocation(entity.getLocation()))
                .onGround(true);
    }

    private List<AbstractPacket> getSpawnPacket(HologramPiece entity) {
        final var toReturn = new LinkedList<AbstractPacket>();

        final var spawnPacket = new SClientboundAddMobPacket()
                .entityId(entity.getId())
                .uuid(entity.getUuid())
                .typeId(entity.getTypeId())
                .velocity(new Vector3D(0, 0, 0))
                .headYaw((byte) 3.9f)
                .location(LocationMapper.wrapLocation(entity.getLocation()));
        spawnPacket.metadata().addAll(entity.getMetadataItems());
        toReturn.add(spawnPacket);

        return toReturn;
    }

    private SClientboundRemoveEntitiesPacket getFullDestroyPacket() throws IllegalArgumentException, SecurityException {
        final var lines = entitiesOnLines.values()
                .stream()
                .map(HologramPiece::getId);

        final int[] toRemove;
        if (itemEntity != null) {
            toRemove = Stream.concat(lines, Stream.of(itemEntity.getId()))
                    .mapToInt(i -> i)
                    .toArray();
        } else {
            toRemove = lines.mapToInt(i -> i).toArray();
        }

        final var destroyPacket = new SClientboundRemoveEntitiesPacket();
        if (toRemove != null && toRemove.length > 0) {
            destroyPacket.entityIds(toRemove);
        }
        return destroyPacket;
    }

    private List<AbstractPacket> getAllSpawnPackets() {
        final var packets = new LinkedList<AbstractPacket>();
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

        final var toSend = new LinkedList<AbstractPacket>();
        if (itemEntity != null) {
            rotationTask.cancel();
        }

        if (itemEntity != null || entitiesOnLines.size() > 0) {
            toSend.add(getFullDestroyPacket());
            update(player, toSend, false);
        }
    }
}
