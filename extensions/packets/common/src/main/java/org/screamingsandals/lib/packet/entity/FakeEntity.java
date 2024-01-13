/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.packet.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.Location;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Represents entity class that is manipulated via DataWatchers and packets without actually registering the entity onto the server.
 */
@Getter
public class FakeEntity {
    private final int id;
    @Setter
    private @NotNull Location location;
    private final int typeId;
    private final @NotNull UUID uuid;
    private final @NotNull List<@NotNull MetadataItem> metadataItems;
    private byte entityFlags;
    private @NotNull Component customName;
    @Getter
    @Setter
    private @Nullable AudienceComponentLike customNameSenderMessage;
    @Setter
    private boolean isOnGround;

    public FakeEntity(@NotNull Location location, int typeId) {
        // default values
        if (!Server.isServerThread()) {
            try {
                this.id = Entities.getNewEntityIdSynchronously().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.id = Entities.getNewEntityId();
        }
        this.typeId = typeId;
        this.location = location;
        this.uuid = UUID.randomUUID();
        this.metadataItems = Collections.synchronizedList(new ArrayList<>());
        this.entityFlags = 0;
        this.customName = Component.empty();
        this.isOnGround = true;

        setEntityFlags();
    }

    public void setCustomName(@NotNull Component name) {
        if (Server.isVersion(1, 13)) {
            put(MetadataItem.ofOpt(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), name));
        } else {
            var str = name.toLegacy();
            if (str.length() > 256) {
                str = str.substring(0, 256);
            }
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), str));
        }
        customName = name;
    }

    public void put(@NotNull MetadataItem metadataItem) {
        metadataItems.removeIf(metadataItem1 -> metadataItem1.getIndex() == metadataItem.getIndex());
        metadataItems.add(metadataItem);
    }

    public void setEntityFlags() {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ENTITY_FLAGS), entityFlags));
    }

    public void setAirTicks(int airTicks) {
        if (Server.isVersion(1, 9)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.AIR_TICKS), airTicks));
        } else {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.AIR_TICKS), (short) airTicks));
        }
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME_VISIBLE), customNameVisible));
    }

    public void setSilent(boolean silent) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.IS_SILENT), silent));
    }

    // armor stand must override this method for legacy
    public void setGravity(boolean gravity) {
        if (Server.isVersion(1, 10)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HAS_NO_GRAVITY), !gravity));
        }
    }

    // TODO: maybe introduce EntityPose enum
    public void setPose(int poseOrdinal) {
        if (Server.isVersion(1, 14)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.POSE), poseOrdinal));
        }
    }

    public void setTicksFrozen(int ticksFrozen) {
        if (Server.isVersion(1, 17)) {
            MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.TICKS_FROZEN), ticksFrozen);
        }
    }

    public void setInvisible(boolean invisible) {
        setEntityFlagsFromValue(5, invisible);
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ENTITY_FLAGS), entityFlags));
    }

    public void setEntityFlagsFromValue(int index, boolean value) {
        if (value) {
            entityFlags = (byte) (entityFlags | 1 << index);
        } else {
            entityFlags = (byte) (entityFlags & (~(1 << index)));
        }
    }

    public void spawn(@NotNull Player player) {
        getSpawnPackets().forEach(packet -> packet.sendPacket(player));
    }

    public void teleport(@NotNull Player player) {
        getTeleportPacket().sendPacket(player);
    }

    public @NotNull List<@NotNull AbstractPacket> getSpawnPackets() {
        return getSpawnPackets(List.of());
    }

    public @NotNull List<@NotNull AbstractPacket> getSpawnPackets(@NotNull Player viewer) {
        if (customNameSenderMessage != null) {
            if (Server.isVersion(1, 13)) {
                return getSpawnPackets(List.of(MetadataItem.ofOpt(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), customNameSenderMessage.asComponent(viewer))));
            } else {
                var str = customNameSenderMessage.asComponent(viewer).toLegacy();
                if (str.length() > 256) {
                    str = str.substring(0, 256);
                }
                return getSpawnPackets(List.of(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), str)));
            }
        }
        return getSpawnPackets(List.of());
    }

    public @NotNull List<@NotNull AbstractPacket> getSpawnPackets(@NotNull List<@NotNull MetadataItem> additionalMetadata) {
        final var toReturn = new LinkedList<AbstractPacket>();
        final var spawnPacket = ClientboundAddMobPacket.builder()
                .entityId(id)
                .uuid(uuid)
                .typeId(typeId)
                .velocity(new Vector3D(0, 0, 0))
                .headYaw((byte) 3.9f)
                .location(location)
                .metadata(metadataItems)
                .build();
        additionalMetadata.forEach(metadataItem -> {
            spawnPacket.metadata().removeIf(metadataItem1 -> metadataItem1.getIndex() == metadataItem.getIndex());
            spawnPacket.metadata().add(metadataItem);
        });
        toReturn.add(spawnPacket);
        return toReturn;
    }

    public @NotNull ClientboundTeleportEntityPacket getTeleportPacket() {
        return ClientboundTeleportEntityPacket.builder()
                .entityId(id)
                .location(location)
                .onGround(isOnGround)
                .build();
    }

    public @NotNull ClientboundSetEntityDataPacket getMetadataPacket() {
        return getMetadataPacket(List.of());
    }

    public @NotNull ClientboundSetEntityDataPacket getMetadataPacket(@NotNull Player viewer) {
        if (customNameSenderMessage != null) {
            if (Server.isVersion(1, 13)) {
                return getMetadataPacket(List.of(MetadataItem.ofOpt(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), customNameSenderMessage.asComponent(viewer))));
            } else {
                var str = customNameSenderMessage.asComponent(viewer).toLegacy();
                if (str.length() > 256) {
                    str = str.substring(0, 256);
                }
                return getMetadataPacket(List.of(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), str)));
            }
        }
        return getMetadataPacket(List.of());
    }

    public @NotNull ClientboundSetEntityDataPacket getMetadataPacket(@NotNull List<@NotNull MetadataItem> additionalMetadata) {
        var newMetadataItems = new ArrayList<>(metadataItems);
        additionalMetadata.forEach(metadataItem -> {
            newMetadataItems.removeIf(metadataItem1 -> metadataItem1.getIndex() == metadataItem.getIndex());
            newMetadataItems.add(metadataItem);
        });
        return ClientboundSetEntityDataPacket.builder()
                .entityId(id)
                .metadata(newMetadataItems)
                .build();
    }

    // TODO: Make this into a separate class
    @RequiredArgsConstructor
    protected enum EntityMetadata {
        // ENTITY

        /**
         * Flag denoting if entity is: on fire, crouching, riding (deprecated), sprinting, swimming, invisible, has glowing effect, or is flying with elytra
         */
        ENTITY_FLAGS,

        AIR_TICKS,

        CUSTOM_NAME,

        CUSTOM_NAME_VISIBLE,

        IS_SILENT,

        /**
         * <b>Note: for legacy versions this id has been apart of MASKED_INDEX_1</b>
         */
        HAS_NO_GRAVITY,

        POSE,

        TICKS_FROZEN,

        // ENTITY LIVING

        HAND_STATES,

        HEALTH,

        POTION_EFFECT_COLOR,

        POTION_AMBIENCY,

        BODY_ARROW_COUNT,

        BEE_STINGER_COUNT,

        BED_BLOCK_POSITION,

        // ENTITY ARMOR STAND

        ARMOR_STAND_FLAGS,

        HEAD_ROTATION,

        BODY_ROTATION,

        LEFT_ARM_ROTATION,

        RIGHT_ARM_ROTATION,

        LEFT_LEG_ROTATION,

        RIGHT_LEG_ROTATION;

        @UtilityClass
        public static class Registry {
            private static final @NotNull Map<@NotNull EntityMetadata, Byte> idMap = new HashMap<>();
            private static byte SEQUENTIAL_INDEXING = -1;

            static {
                register(EntityMetadata.ENTITY_FLAGS);
                register(EntityMetadata.AIR_TICKS);
                register(EntityMetadata.CUSTOM_NAME);
                register(EntityMetadata.CUSTOM_NAME_VISIBLE);
                register(EntityMetadata.IS_SILENT);
                if (Server.isVersion(1, 10)) {
                    register(EntityMetadata.HAS_NO_GRAVITY);
                }
                if (Server.isVersion(1, 14)) {
                    register(EntityMetadata.POSE);
                }
                if (Server.isVersion(1, 17)) {
                    register(EntityMetadata.TICKS_FROZEN);
                }

                if (Server.isVersion(1, 9)) {
                    register(EntityMetadata.HAND_STATES);
                } else {
                    SEQUENTIAL_INDEXING++; // skipped in 1.8.8
                }
                register(EntityMetadata.HEALTH);
                register(EntityMetadata.POTION_EFFECT_COLOR);
                register(EntityMetadata.POTION_AMBIENCY);
                register(EntityMetadata.BODY_ARROW_COUNT);
                if (Server.isVersion(1, 15)) {
                    register(EntityMetadata.BEE_STINGER_COUNT);
                }
                if (Server.isVersion(1, 14)) {
                    register(EntityMetadata.BED_BLOCK_POSITION);
                }

                register(EntityMetadata.ARMOR_STAND_FLAGS);
                register(EntityMetadata.HEAD_ROTATION);
                register(EntityMetadata.BODY_ROTATION);
                register(EntityMetadata.LEFT_ARM_ROTATION);
                register(EntityMetadata.RIGHT_ARM_ROTATION);
                register(EntityMetadata.LEFT_LEG_ROTATION);
                register(EntityMetadata.RIGHT_LEG_ROTATION);
            }

            public static Optional<EntityMetadata> fromId(byte id) {
                for (var entry : idMap.entrySet()) {
                    final var entryKey = entry.getKey();
                    final var entryId = entry.getValue();
                    if (entryId == id) {
                        return Optional.of(entryKey);
                    }
                }
                return Optional.empty();
            }

            public static byte getId(@NotNull EntityMetadata metadata) {
                return Objects.requireNonNull(idMap.get(metadata), "Has: " + metadata.name() + " not been registered into registry?");
            }

            public static void register(@NotNull EntityMetadata indices) {
                if (idMap.containsKey(indices)) {
                    return;
                }
                SEQUENTIAL_INDEXING = (byte) (SEQUENTIAL_INDEXING + 1);
                idMap.put(indices, SEQUENTIAL_INDEXING);
            }
        }
    }
}

