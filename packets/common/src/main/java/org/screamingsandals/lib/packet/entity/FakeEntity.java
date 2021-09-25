package org.screamingsandals.lib.packet.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.*;

/**
 * Represents entity class that is manipulated via DataWatchers and packets without actually registering the entity onto the server.
 */
@Getter
public class FakeEntity {
    private final int id;
    @Setter
    private LocationHolder location;
    private final int typeId;
    private final UUID uuid;
    private final List<MetadataItem> metadataItems;
    private byte entityFlags;
    private Component customName;
    @Setter
    private boolean isOnGround;

    FakeEntity(LocationHolder location, int typeId) {
        // default values
        this.id = EntityMapper.getNewEntityId();
        this.typeId = typeId;
        this.location = location;
        this.uuid = UUID.randomUUID();
        this.metadataItems = Collections.synchronizedList(new ArrayList<>());
        this.entityFlags = 0;
        this.customName = Component.empty();
        this.isOnGround = true;

        setEntityFlags();
        setAirTicks(300);
        setCustomName(Component.empty());
        setCustomNameVisible(false);
        setSilent(false);
        setGravity(true);
        setPose(0);
        setTicksFrozen(0);
    }

    public void setCustomName(Component name) {
        if (Core.isVersion(1, 13)) {
            put(MetadataItem.ofOpt(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), name));
        } else {
            var str = AdventureHelper.toLegacy(name);
            if (str.length() > 256) {
                str = str.substring(0, 256);
            }
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME), str));
        }
        customName = name;
    }

    public void put(MetadataItem metadataItem) {
        metadataItems.removeIf(metadataItem1 -> metadataItem1.getIndex() == metadataItem.getIndex());
        metadataItems.add(metadataItem);
    }

    public void setEntityFlags() {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ENTITY_FLAGS), entityFlags));
    }

    public void setAirTicks(int airTicks) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.AIR_TICKS), airTicks));
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.CUSTOM_NAME_VISIBLE), customNameVisible));
    }

    public void setSilent(boolean silent) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.IS_SILENT), silent));
    }

    // armor stand must override this method for legacy
    public void setGravity(boolean gravity) {
        if (Core.isVersion(1, 10)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HAS_NO_GRAVITY), !gravity));
        }
    }

    // TODO: maybe introduce EntityPose enum
    public void setPose(int poseOrdinal) {
        if (Core.isVersion(1, 14)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.POSE), poseOrdinal));
        }
    }

    public void setTicksFrozen(int ticksFrozen) {
        // TODO: seems like this was either added in 1.16.4 or 1.17, wiki.vg isn't really clear about this, let's find it out ourselves :>
        if (Core.isVersion(1, 16, 4)) {
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

    public void spawn(PlayerWrapper player) {
        getSpawnPackets().forEach(packet -> packet.sendPacket(player));
    }

    public void teleport(PlayerWrapper player) {
        getTeleportPacket().sendPacket(player);
    }

    public List<AbstractPacket> getSpawnPackets() {
        final var toReturn = new LinkedList<AbstractPacket>();
        final var spawnPacket = new SClientboundAddMobPacket()
                .entityId(id)
                .uuid(uuid)
                .typeId(typeId)
                .velocity(new Vector3D(0, 0, 0))
                .headYaw((byte) 3.9f)
                .location(location);
        spawnPacket.metadata().addAll(metadataItems);
        toReturn.add(spawnPacket);
        return toReturn;
    }

    public SClientboundTeleportEntityPacket getTeleportPacket() {
        return new SClientboundTeleportEntityPacket()
                .entityId(id)
                .location(location)
                .onGround(isOnGround);
    }

    public SClientboundSetEntityDataPacket getMetadataPacket() {
        final var metadataPacket = new SClientboundSetEntityDataPacket()
                .entityId(id);
        metadataPacket.metadata().addAll(metadataItems);
        return metadataPacket;
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

        public static class Registry {
            private static final Map<EntityMetadata, Byte> idMap = new HashMap<>();
            private static byte SEQUENTIAL_INDEXING = -1;

            static {
                register(EntityMetadata.ENTITY_FLAGS);
                register(EntityMetadata.AIR_TICKS);
                register(EntityMetadata.CUSTOM_NAME);
                register(EntityMetadata.CUSTOM_NAME_VISIBLE);
                register(EntityMetadata.IS_SILENT);
                if (Core.isVersion(1, 10)) {
                    register(EntityMetadata.HAS_NO_GRAVITY);
                }
                if (Core.isVersion(1, 14)) {
                    register(EntityMetadata.POSE);
                }
                // TODO: not sure if this is 1.16.4 or 1.17 since wiki.vg history seems like it's wrong, let's find it ourselves.
                if (Core.isVersion(1, 16, 4)) {
                    register(EntityMetadata.TICKS_FROZEN);
                }

                register(EntityMetadata.HAND_STATES);
                register(EntityMetadata.HEALTH);
                register(EntityMetadata.POTION_EFFECT_COLOR);
                register(EntityMetadata.POTION_AMBIENCY);
                register(EntityMetadata.BODY_ARROW_COUNT);
                if (Core.isVersion(1, 15)) {
                    register(EntityMetadata.BEE_STINGER_COUNT);
                }
                if (Core.isVersion(1, 14)) {
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

            public static byte getId(EntityMetadata metadata) {
                return Objects.requireNonNull(idMap.get(metadata), "Has: " + metadata.name() + " not been registered into registry?");
            }

            public static void register(EntityMetadata indices) {
                if (idMap.containsKey(indices)) {
                    return;
                }
                SEQUENTIAL_INDEXING = (byte) (SEQUENTIAL_INDEXING + 1);
                idMap.put(indices, SEQUENTIAL_INDEXING);
            }
        }
    }
}

