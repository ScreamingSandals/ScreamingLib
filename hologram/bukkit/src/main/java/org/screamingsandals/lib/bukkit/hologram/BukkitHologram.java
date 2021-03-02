package org.screamingsandals.lib.bukkit.hologram;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.hologram.nms.AdvancedArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

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
            removeForPlayer(player, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                try {
                    if (Version.isVersion(1, 15)) {
                        Object metadataPacket = ClassStorage.NMS.PacketPlayOutEntityMetadata
                                .getConstructor(int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                                .newInstance(itemEntity.getId(),
                                        itemEntity.getDataWatcher(), false);
                        viewers.forEach(player -> update(player.as(Player.class), List.of(metadataPacket), false));
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }).repeat(rotationTime.getFirst(), rotationTime.getSecond())
                    .async()
                    .start();
        }

        return this;
    }

    @Override
    public Hologram hide() {
        viewers.forEach(viewer -> {
            try {
                removeForPlayer(viewer, false);
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
    public Hologram item(Item item) {
        super.item(item);
        if (ready) {
            viewers.forEach(player -> {
                try {
                    update(player.as(Player.class), List.of(getEquipmentPacket(itemEntity, item)), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
            if (lines.size() != originalLinesSize
                    && itemEntity != null) {
                itemEntity.setLocation(cachedLocation.clone().add(0, lines.size() * .25, 0));

                try {
                    packets.add(getTeleportPacket(itemEntity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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

                        entityOnLine.setCustomName(value);
                        entityOnLine.setLocation(cachedLocation.clone().add(0, (lines.size() - key) * .25, 0));
                        packets.add(getTeleportPacket(entityOnLine));
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

                        packets.addAll(getSpawnPacket(entity));

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

    private Object getEquipmentPacket(AdvancedArmorStandNMS entity, Item item) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return ClassStorage.NMS.PacketPlayOutEntityEquipment
                .getConstructor(int.class, List.class)
                .newInstance(entity.getId(), List.of(Pair.of(entity.getHeadSlot(), stackAsNMS(item))));
    }

    private Object getTeleportPacket(ArmorStandNMS entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return ClassStorage.NMS.PacketPlayOutEntityTeleport
                .getConstructor(ClassStorage.NMS.Entity)
                .newInstance(entity.getHandler());
    }

    private List<Object> getSpawnPacket(ArmorStandNMS entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final var toReturn = new LinkedList<>();

        toReturn.add(ClassStorage.NMS.PacketPlayOutSpawnEntityLiving
                .getConstructor(ClassStorage.NMS.EntityLiving)
                .newInstance(entity.getHandler()));

        if (Version.isVersion(1, 15)) {
            final var metadataPacket = ClassStorage.NMS.PacketPlayOutEntityMetadata
                    .getConstructor(int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                    .newInstance(entity.getId(), entity.getDataWatcher(), true);
            toReturn.add(metadataPacket);
        }

        return toReturn;
    }

    private Object getFullDestroyPacket() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        final var lines = entitiesOnLines.values()
                .stream()
                .map(EntityNMS::getId);

        final int[] toRemove;
        if (itemEntity != null) {
            toRemove = Stream.concat(lines, Stream.of(itemEntity.getId()))
                    .mapToInt(i -> i)
                    .toArray();
        } else {
            toRemove = lines.mapToInt(i -> i).toArray();
        }

        return ClassStorage.NMS.PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(toRemove);
    }

    private List<Object> getAllSpawnPackets() {
        final var packets = new LinkedList<>();
        entitiesOnLines.forEach((line, entity) -> {
            try {
                packets.addAll(getSpawnPacket(entity));
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });

        if (itemEntity != null) {
            try {
                packets.addAll(getSpawnPacket(itemEntity));
                packets.add(getEquipmentPacket(itemEntity, item));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            return in + 10f; //TODO: fixme
        }
    }

    private Object stackAsNMS(Item item) {
        Preconditions.checkNotNull(item, "Item is null!");
        return ClassStorage.getMethod(ClassStorage.NMS.CraftItemStack, "asNMSCopy", ItemStack.class).invokeStatic(item.as(ItemStack.class));
    }

    private void removeForPlayer(PlayerWrapper player, boolean shouldCheckDistance)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!player.isOnline()) {
            return;
        }

        final var toSend = new LinkedList<>();
        if (itemEntity != null) {
            rotationTask.cancel();
            toSend.add(getEquipmentPacket(itemEntity, ItemFactory.getAir()));
        }

        toSend.add(getFullDestroyPacket());
        update(player.as(Player.class), toSend, shouldCheckDistance);
    }
}
