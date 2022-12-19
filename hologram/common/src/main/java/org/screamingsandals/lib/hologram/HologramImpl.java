/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.hologram;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.SClientboundRemoveEntitiesPacket;
import org.screamingsandals.lib.packet.SClientboundSetEntityDataPacket;
import org.screamingsandals.lib.packet.SClientboundSetEquipmentPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.visual.SimpleCLTextEntry;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Accessors(chain = true, fluent = true)
@Getter
@Setter
public class HologramImpl extends AbstractLinedVisual<Hologram> implements Hologram {
    @Getter(value = AccessLevel.NONE)
    private final Map<Integer, HologramPiece> entitiesOnLines;
    @Getter(value = AccessLevel.NONE)
    private volatile HologramPiece itemEntity;
    private volatile TaskerTask rotationTask;
    private LocationHolder cachedLocation;
    private LocationHolder location;
    private int viewDistance;
    private boolean touchable;
    private boolean created;
    private DataContainer data;
    private float rotationIncrement;
    private Pair<Integer, TaskerTime> rotationTime;
    private RotationMode rotationMode;
    private Item item;
    @Getter
    private ItemPosition itemPosition;
    private long clickCooldown;

    public HologramImpl(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid);
        this.location = location;
        this.touchable = touchable;

        //default values
        this.clickCooldown = DEFAULT_CLICK_COOL_DOWN;
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
    public @NotNull Hologram location(@NotNull LocationHolder location) {
        this.location = location;
        this.cachedLocation = location;
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull Hologram spawn() {
        if (created) {
            throw new UnsupportedOperationException("Hologram has already been spawned!");
        }

        show();
        created = true;
        return this;
    }

    @Override
    public boolean hasId(int entityId) {
        return entitiesOnLines.values()
                .stream()
                .anyMatch(entity -> entity.getId() == entityId);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull Hologram update(@NotNull UpdateStrategy strategy) {
        if (visible) {
            updateEntities();
        }
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull Hologram show() {
        if (destroyed()) {
            throw new UnsupportedOperationException("Cannot call Hologram#show() on destroyed holograms!");
        }

        visible = true;
        update();
        startRotationTask();
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull Hologram hide() {
        if (!shown()) {
            return this;
        }

        visible = false;
        viewers.forEach(viewer -> onViewerRemoved(viewer, false));
        cancelRotationTask();
        return this;
    }

    @Override
    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    @Override
    public Hologram rotationTime(Pair<Integer, TaskerTime> rotatingTime) {
        this.rotationTime = rotatingTime;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public Hologram rotationMode(RotationMode mode) {
        this.rotationMode = mode;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public Hologram item(Item item) {
        this.item = item;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public Hologram itemPosition(ItemPosition location) {
        this.itemPosition = location;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public void destroy() {
        if (destroyed()) {
            return;
        }

        hide();
        viewers.clear();

        visible = false;
        destroyed = true;
        data = null;

        HologramManager.removeHologram(this);
    }

    @Override
    public void onViewerAdded(@NotNull PlayerWrapper viewer, boolean checkDistance) {
        if (!viewer.isOnline()) {
            return;
        }
        if (visible
                && entitiesOnLines.size() != lines.size()) { // fix if you show hologram and then you add viewers
            updateEntities();
        }
        update(viewer, getAllSpawnPackets(viewer), checkDistance);
    }

    @Override
    public void onViewerRemoved(@NotNull PlayerWrapper viewer, boolean checkDistance) {
        if (!viewer.isOnline()) {
            return;
        }

        final var toSend = new LinkedList<AbstractPacket>();
        if (itemEntity != null || entitiesOnLines.size() > 0) {
            toSend.add(getFullDestroyPacket());
            update(viewer, toSend, false);
        }
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
        final var packets = new LinkedList<Function<PlayerWrapper, List<AbstractPacket>>>();
        if (visible && viewers.size() > 0) {
            if (lines.size() != originalLinesSize
                    && itemEntity != null) {
                itemEntity.setLocation(cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                        ? (-lines.size() * .25 - .5)
                        : (lines.size() * .25), 0));

                packets.add(p -> List.of(itemEntity.getTeleportPacket()));
            }

            lines.forEach((key, value) -> {
                try {
                    if (entitiesOnLines.containsKey(key)) {
                        final var entityOnLine = entitiesOnLines.get(key);
                        var isSenderMessage = value instanceof SimpleCLTextEntry && ((SimpleCLTextEntry) value).getComponentLike() instanceof AudienceComponentLike;
                        if (originalLinesSize == lines.size()) {
                            if (isSenderMessage) {
                                if (entityOnLine.getCustomNameSenderMessage() != null && entityOnLine.getCustomNameSenderMessage().equals(((SimpleCLTextEntry) value).getComponentLike())) {
                                    return;
                                }
                            } else if (entityOnLine.getCustomName().equals(value.getText())) {
                                return;
                            }
                        }

                        entityOnLine.setCustomName(value.getText());
                        entityOnLine.setCustomNameSenderMessage(isSenderMessage ? (AudienceComponentLike) ((SimpleCLTextEntry) value).getComponentLike() : null);
                        packets.add(p -> List.of(entityOnLine.getMetadataPacket(p)));
                        entityOnLine.setLocation(cachedLocation.clone().add(0, (lines.size() - key) * .25, 0));
                        packets.add(p -> List.of(entityOnLine.getTeleportPacket()));
                    } else {
                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0);
                        final var entity = new HologramPiece(newLocation);
                        entity.setCustomName(value.getText());
                        if (value instanceof SimpleCLTextEntry && ((SimpleCLTextEntry) value).getComponentLike() instanceof AudienceComponentLike) {
                            entity.setCustomNameSenderMessage((AudienceComponentLike) ((SimpleCLTextEntry) value).getComponentLike());
                        }
                        entity.setCustomNameVisible(true);
                        entity.setInvisible(true);
                        entity.setSmall(!touchable);
                        entity.setArms(false);
                        entity.setBasePlate(false);
                        entity.setGravity(false);
                        entity.setMarker(!touchable);
                        packets.add(entity::getSpawnPackets);
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
                        packets.add(entity::getSpawnPackets);
                        packets.add(p -> List.of(getEquipmentPacket(entity, item)));
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
                packets.add(p -> List.of(destroyPacket));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        viewers.forEach(viewer -> update(viewer, packets.stream().map(f -> f.apply(viewer)).flatMap(Collection::stream).collect(Collectors.toList()), true));
    }

    private SClientboundSetEquipmentPacket getEquipmentPacket(HologramPiece entity, Item item) {
        var packet = new SClientboundSetEquipmentPacket()
                .entityId(entity.getId());
        packet.slots().put(EquipmentSlotHolder.of("HEAD"), item);
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

    private List<AbstractPacket> getAllSpawnPackets(PlayerWrapper viewer) {
        final var packets = new LinkedList<AbstractPacket>();
        entitiesOnLines.forEach((line, entity) -> packets.addAll(entity.getSpawnPackets(viewer)));
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

    private void startRotationTask() {
        if (rotationMode != RotationMode.NONE && rotationTask == null) {
            rotationTask = Tasker.build(() -> {
                        if (itemEntity == null || !visible) {
                            return;
                        }

                        itemEntity.setHeadRotation(checkAndAdd(itemEntity.getHeadRotation()));
                        final var metadataPacket = new SClientboundSetEntityDataPacket()
                                .entityId(itemEntity.getId());
                        metadataPacket.metadata().addAll(itemEntity.getMetadataItems());
                        viewers.forEach(player -> update(player, List.of(metadataPacket), false));
                    })
                    .repeat(rotationTime.getFirst(), rotationTime.getSecond())
                    .async()
                    .start();
        }
    }

    private void cancelRotationTask() {
        if (rotationTask != null
                && rotationTask.getState() != TaskState.CANCELLED) {
            rotationTask.cancel();
            rotationTask = null;
        }
    }

    private void restartRotationTask() {
        cancelRotationTask();
        startRotationTask();
    }
}
