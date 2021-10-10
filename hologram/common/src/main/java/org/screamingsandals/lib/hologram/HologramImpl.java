package org.screamingsandals.lib.hologram;

import lombok.Getter;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.SClientboundRemoveEntitiesPacket;
import org.screamingsandals.lib.packet.SClientboundSetEntityDataPacket;
import org.screamingsandals.lib.packet.SClientboundSetEquipmentPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class HologramImpl extends AbstractLinedVisual<Hologram> implements Hologram {
    private final Map<Integer, HologramPiece> entitiesOnLines;
    private HologramPiece itemEntity;
    private TaskerTask rotationTask;
    private LocationHolder cachedLocation;
    private LocationHolder location;
    private int viewDistance;
    private boolean touchable;
    private boolean destroyed;
    private boolean created;
    private DataContainer data;
    private float rotationIncrement;
    private Pair<Integer, TaskerTime> rotationTime;
    private RotationMode rotationMode;
    private Item item;
    @Getter
    private ItemPosition itemPosition;
    private long clickCoolDown;

    public HologramImpl(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid);
        this.location = location;
        this.touchable = touchable;

        //default values
        this.destroyed = false;
        this.created = false;
        this.clickCoolDown = DEFAULT_CLICK_COOL_DOWN;
        this.viewDistance = DEFAULT_VIEW_DISTANCE;
        this.rotationIncrement = DEFAULT_ROTATION_INCREMENT;
        this.data = DataContainer.get();
        this.rotationTime = Pair.of(2, TaskerTime.TICKS);
        this.rotationMode = RotationMode.NONE;
        this.itemPosition = ItemPosition.ABOVE;
        this.entitiesOnLines = new ConcurrentHashMap<>();
        this.cachedLocation = location;
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @Override
    public Hologram setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    @Override
    public LocationHolder getLocation() {
        return location;
    }

    @Override
    public Hologram setLocation(LocationHolder location) {
        this.location = location;
        this.cachedLocation = location;
        return this;
    }

    @Override
    public Hologram spawn() {
        return show();
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }

    @Override
    public Hologram setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }

    @Override
    public boolean hasId(int entityId) {
        return entitiesOnLines.values()
                .stream()
                .anyMatch(entity -> entity.getId() == entityId);
    }

    @Override
    public Hologram update() {
        if (visible) {
            update0();
        }
        return this;
    }

    @Override
    public Hologram show() {
        if (isShown()) {
            return this;
        }
        if (isDestroyed()) {
            throw new UnsupportedOperationException("Cannot call Hologram#show() for destroyed holograms!");
        }
        created = true;
        visible = true;
        update();

        if (rotationMode != RotationMode.NONE) {
            if (rotationTask == null) {
                rotationTask = Tasker.build(() -> {
                            if (itemEntity == null) {
                                return;
                            }

                            itemEntity.setHeadRotation(checkAndAdd(itemEntity.getHeadRotation()));

                            final var metadataPacket = new SClientboundSetEntityDataPacket()
                                    .entityId(itemEntity.getId());
                            metadataPacket.metadata().addAll(itemEntity.getMetadataItems());

                            viewers.forEach(player -> update(player, List.of(metadataPacket), false));
                        }).repeat(rotationTime.getFirst(), rotationTime.getSecond())
                        .async()
                        .start();
            }
        }
        return this;
    }

    @Override
    public Hologram hide() {
        if (!isShown()) {
            return this;
        }

        viewers.forEach(this::removeForPlayer);
        if (rotationTask != null) {
            rotationTask.cancel();
            rotationTask = null;
        }

        visible = false;
        update();
        return this;
    }

    @Override
    public DataContainer getData() {
        return data;
    }

    @Override
    public void setData(DataContainer data) {
        this.data = data;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }
        return !data.isEmpty();
    }

    @Override
    public Pair<Integer, TaskerTime> getRotationTime() {
        return rotationTime;
    }

    @Override
    public Hologram rotationTime(Pair<Integer, TaskerTime> rotatingTime) {
        this.rotationTime = rotatingTime;
        update();
        return this;
    }

    @Override
    public Hologram rotationMode(RotationMode mode) {
        this.rotationMode = mode;
        update();
        return this;
    }

    @Override
    public RotationMode getRotationMode() {
        return rotationMode;
    }

    @Override
    public Hologram item(Item item) {
        this.item = item;
        update();
        return this;
    }

    @Override
    public Hologram rotationIncrement(float toIncrement) {
        this.rotationIncrement = toIncrement;
        return this;
    }

    @Override
    public Hologram itemPosition(ItemPosition location) {
        this.itemPosition = location;
        update();
        return this;
    }

    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }

        if (rotationTask != null) {
            rotationTask.cancel();
            rotationTask = null;
        }
        destroyed = true;
        data = null;
        hide();
        viewers.clear();
        HologramManager.removeHologram(this);
    }

    @Override
    public boolean isCreated() {
        return created;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void onViewerAdded(PlayerWrapper viewer, boolean checkDistance) {
        if (visible && entitiesOnLines.size() != lines.size()) { // fix if you show hologram and then you add viewers
            updateEntities();
        }
        update(viewer, getAllSpawnPackets(), checkDistance);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper viewer, boolean checkDistance) {
        removeForPlayer(viewer);
    }

    @Override
    public long getClickCoolDown() {
        return clickCoolDown;
    }

    @Override
    public Hologram setClickCoolDown(long clickCoolDown) {
        this.clickCoolDown = clickCoolDown;
        return this;
    }

    private void update(PlayerWrapper player, List<AbstractPacket> packets, boolean checkDistance) {
        if (!player.getLocation().getWorld().equals(cachedLocation.getWorld())) {
            return;
        }

        if (checkDistance
                && player.getLocation().getDistanceSquared(cachedLocation) >= viewDistance) {
            return;
        }

        packets.forEach(packet -> packet.sendPacket(player));
    }

    private void updateEntities() {
        final var packets = new LinkedList<AbstractPacket>();
        if (visible && viewers.size() > 0) {
            if (lines.size() != originalLinesSize
                    && itemEntity != null) {
                itemEntity.setLocation(cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                        ? (-lines.size() * .25 - .5)
                        : (lines.size() * .25), 0));

                packets.add(itemEntity.getTeleportPacket());
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
                        packets.add(entityOnLine.getMetadataPacket());
                        entityOnLine.setLocation(cachedLocation.clone().add(0, (lines.size() - key) * .25, 0));
                        packets.add(entityOnLine.getTeleportPacket());
                    } else {
                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0);
                        final var entity = new HologramPiece(newLocation);
                        entity.setCustomName(value.getText());
                        entity.setCustomNameVisible(true);
                        entity.setInvisible(true);
                        entity.setSmall(!touchable);
                        entity.setArms(false);
                        entity.setBasePlate(false);
                        entity.setGravity(false);
                        entity.setMarker(!touchable);
                        packets.addAll(entity.getSpawnPackets());
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
                                : (lines.size() * .25), 0);
                        final var entity = new HologramPiece(newLocation);
                        entity.setInvisible(true);
                        entity.setSmall(!touchable);
                        entity.setArms(false);
                        entity.setBasePlate(false);
                        entity.setGravity(false);
                        entity.setMarker(!touchable);
                        packets.addAll(entity.getSpawnPackets());
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

    private SClientboundRemoveEntitiesPacket getFullDestroyPacket() {
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
        entitiesOnLines.forEach((line, entity) -> packets.addAll(entity.getSpawnPackets()));
        if (itemEntity != null) {
            packets.addAll(itemEntity.getSpawnPackets());
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
        if (itemEntity != null && rotationTask != null) {
            rotationTask.cancel();
            rotationTask = null;
        }

        if (itemEntity != null || entitiesOnLines.size() > 0) {
            toSend.add(getFullDestroyPacket());
            update(player, toSend, false);
        }
    }

    @Override
    protected void update0() {
        updateEntities();
    }
}