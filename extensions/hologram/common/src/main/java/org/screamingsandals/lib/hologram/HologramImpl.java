/*
 * Copyright 2023 ScreamingSandals
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
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.hologram.piece.ArmorStandHologramPiece;
import org.screamingsandals.lib.hologram.piece.HologramPiece;
import org.screamingsandals.lib.hologram.piece.TextDisplayHologramPiece;
import org.screamingsandals.lib.impl.utils.feature.PlatformFeature;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.ClientboundRemoveEntitiesPacket;
import org.screamingsandals.lib.packet.ClientboundSetEntityDataPacket;
import org.screamingsandals.lib.packet.ClientboundSetEquipmentPacket;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskState;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.impl.utils.visual.SimpleCLTextEntry;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;
import org.screamingsandals.lib.world.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Accessors(chain = true, fluent = true)
@Getter
@Setter
public class HologramImpl extends AbstractLinedVisual<Hologram> implements Hologram {
    private static final @NotNull PlatformFeature DISPLAY_ENTITIES_AVAILABLE = PlatformFeature.of(() -> Server.isVersion(1, 19, 4));

    @Getter(AccessLevel.NONE)
    private final @NotNull Map<@NotNull Integer, HologramPiece> entitiesOnLines;
    @Getter(AccessLevel.NONE)
    private volatile @Nullable ArmorStandHologramPiece itemEntity;
    private volatile @Nullable Task rotationTask;
    private @NotNull Location cachedLocation;
    private @NotNull Location location;
    private int viewDistance;
    private boolean touchable;
    private boolean created;
    private @Nullable DataContainer data;
    private float rotationIncrement;
    private @NotNull Pair<@NotNull Integer, @NotNull TaskerTime> rotationTime;
    private @NotNull RotationMode rotationMode;
    private @Nullable ItemStack item;
    private @NotNull ItemPosition itemPosition;
    private long clickCooldown;

    public HologramImpl(@NotNull UUID uuid, @NotNull Location location, boolean touchable) {
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
    public @NotNull Hologram location(@NotNull Location location) {
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
    public @NotNull Hologram rotationTime(@NotNull Pair<@NotNull Integer, @NotNull TaskerTime> rotationTime) {
        this.rotationTime = rotationTime;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public @NotNull Hologram rotationMode(@NotNull RotationMode mode) {
        this.rotationMode = mode;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public @NotNull Hologram item(@Nullable ItemStack item) {
        this.item = item;
        update();
        restartRotationTask();
        return this;
    }

    @Override
    public @NotNull Hologram itemPosition(@NotNull ItemPosition location) {
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
    public void onViewerAdded(@NotNull Player viewer, boolean checkDistance) {
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
    public void onViewerRemoved(@NotNull Player viewer, boolean checkDistance) {
        if (!viewer.isOnline()) {
            return;
        }

        final var toSend = new ArrayList<AbstractPacket>();
        if (itemEntity != null || !entitiesOnLines.isEmpty()) {
            toSend.add(getFullDestroyPacket());
            update(viewer, toSend, false);
        }
    }

    private void update(Player player, @NotNull List<@NotNull AbstractPacket> packets, boolean checkDistance) {
        if (!player.getLocation().getWorld().equals(cachedLocation.getWorld())) {
            return;
        }

        if (checkDistance && player.getLocation().getDistanceSquared(cachedLocation) >= viewDistance) {
            return;
        }

        packets.forEach(packet -> packet.sendPacket(player));
    }

    private void updateEntities() {
        final var packets = new LinkedList<Function<Player, List<AbstractPacket>>>();
        if (visible && !viewers.isEmpty()) {
            if (lines.size() != originalLinesSize && itemEntity != null) {
                itemEntity.setLocation(cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                        ? (-lines.size() * .25 - .5)
                        : (lines.size() * .25), 0));

                packets.add(p -> List.of(itemEntity.getTeleportPacket()));
            }

            lines.forEach((key, value) -> {
                try {
                    var isSenderMessage = value instanceof SimpleCLTextEntry && ((SimpleCLTextEntry) value).getComponentLike() instanceof AudienceComponentLike;
                    if (entitiesOnLines.containsKey(key)) {
                        final var entityOnLine = entitiesOnLines.get(key);
                        if (originalLinesSize == lines.size()) {
                            if (isSenderMessage) {
                                if (entityOnLine.getAudienceComponent() != null && entityOnLine.getAudienceComponent().equals(((SimpleCLTextEntry) value).getComponentLike())) {
                                    return;
                                }
                            } else if (entityOnLine.getText() != null && entityOnLine.getText().equals(value.getText())) {
                                return;
                            }
                        }

                        entityOnLine.setText(value.getText(), isSenderMessage ? (AudienceComponentLike) ((SimpleCLTextEntry) value).getComponentLike() : null);
                        packets.add(p -> List.of(entityOnLine.getMetadataPacket(p)));
                        entityOnLine.setLocation(cachedLocation.clone().add(0, (lines.size() - key) * .25, 0));
                        packets.add(p -> List.of(entityOnLine.getTeleportPacket()));
                    } else {
                        final var newLocation = cachedLocation.clone().add(0, (lines.size() - key) * .25, 0);
                        HologramPiece piece;
                        if (!touchable && HologramManager.isPreferDisplayEntities() && DISPLAY_ENTITIES_AVAILABLE.isSupported()) {
                            piece = new TextDisplayHologramPiece(newLocation);
                            // TODO: 1.19.4 touchable hologram: text display + interaction entity
                        } else {
                            final var entity = new ArmorStandHologramPiece(newLocation);
                            entity.setCustomNameVisible(true);
                            entity.setInvisible(true);
                            entity.setSmall(!touchable);
                            entity.setArms(false);
                            entity.setBasePlate(false);
                            entity.setGravity(false);
                            entity.setMarker(!touchable);
                            piece = entity;
                        }
                        piece.setText(value.getText(), isSenderMessage ? (AudienceComponentLike) ((SimpleCLTextEntry) value).getComponentLike() : null);
                        packets.add(piece::getSpawnPackets);
                        entitiesOnLines.put(key, piece);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });

            try {
                if (rotationMode != RotationMode.NONE) {
                    if (itemEntity == null && item != null) {
                        // TODO: 1.19.4 hologram: item entity + interaction entity if touchable
                        final var newLocation = cachedLocation.clone().add(0, itemPosition == ItemPosition.BELOW
                                ? (-lines.size() * .25 - .5)
                                : (lines.size() * .25), 0);
                        final var entity = new ArmorStandHologramPiece(newLocation);
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

            int[] arr = toRemove
                    .stream()
                    .mapToInt(i -> i)
                    .toArray();

            if (arr != null && arr.length > 0) {
                final var destroyPacket = ClientboundRemoveEntitiesPacket.builder();
                destroyPacket.entityIds(toRemove
                        .stream()
                        .mapToInt(i -> i)
                        .toArray()
                );
                packets.add(p -> List.of(destroyPacket.build()));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        viewers.forEach(viewer -> update(viewer, packets.stream().map(f -> f.apply(viewer)).flatMap(Collection::stream).collect(Collectors.toList()), true));
    }

    private ClientboundSetEquipmentPacket getEquipmentPacket(@NotNull ArmorStandHologramPiece entity, @NotNull ItemStack item) {
        return ClientboundSetEquipmentPacket.builder()
                .entityId(entity.getId())
                .slots(Map.of(EquipmentSlot.of("HEAD"), item))
                .build();
    }

    private ClientboundRemoveEntitiesPacket getFullDestroyPacket() {
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

        return ClientboundRemoveEntitiesPacket.builder().entityIds(toRemove).build();
    }

    private @NotNull List<@NotNull AbstractPacket> getAllSpawnPackets(@NotNull Player viewer) {
        final var packets = new ArrayList<AbstractPacket>();
        entitiesOnLines.forEach((line, entity) -> packets.addAll(entity.getSpawnPackets(viewer)));
        if (itemEntity != null && item != null) {
            packets.addAll(itemEntity.getSpawnPackets());
            packets.add(getEquipmentPacket(itemEntity, item));
        }
        return packets;
    }

    private @NotNull Vector3Df checkAndAdd(@NotNull Vector3Df in) {
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
            rotationTask = Tasker.runAsyncRepeatedly(() -> {
                if (itemEntity == null || !visible) {
                    return;
                }

                itemEntity.setHeadRotation(checkAndAdd(itemEntity.getHeadRotation()));
                final var metadataPacket = ClientboundSetEntityDataPacket.builder()
                        .entityId(itemEntity.getId())
                        .metadata(itemEntity.getMetadataItems())
                        .build();
                viewers.forEach(player -> update(player, List.of(metadataPacket), false));
            }, rotationTime.getFirst(), rotationTime.getSecond());
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
