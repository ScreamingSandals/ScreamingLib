package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SCreatureSpawnEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final SpawnReason spawnReason;

    /**
     * An enum to specify the type of spawning
     */
    public enum SpawnReason {

        /**
         * When something spawns from natural means
         */
        NATURAL,
        /**
         * When an entity spawns as a jockey of another entity (mostly spider
         * jockeys)
         */
        JOCKEY,
        /**
         * When a creature spawns due to chunk generation
         *
         * @deprecated no longer called, chunks are generated with entities
         * already existing.
         */
        @Deprecated
        CHUNK_GEN,
        /**
         * When a creature spawns from a spawner
         */
        SPAWNER,
        /**
         * When a creature spawns from an egg
         */
        EGG,
        /**
         * When a creature spawns from a Spawner Egg
         */
        SPAWNER_EGG,
        /**
         * When a creature spawns because of a lightning strike
         */
        LIGHTNING,
        /**
         * When a snowman is spawned by being built
         */
        BUILD_SNOWMAN,
        /**
         * When an iron golem is spawned by being built
         */
        BUILD_IRONGOLEM,
        /**
         * When a wither boss is spawned by being built
         */
        BUILD_WITHER,
        /**
         * When an iron golem is spawned to defend a village
         */
        VILLAGE_DEFENSE,
        /**
         * When a zombie is spawned to invade a village
         */
        VILLAGE_INVASION,
        /**
         * When an animal breeds to create a child
         */
        BREEDING,
        /**
         * When a slime splits
         */
        SLIME_SPLIT,
        /**
         * When an entity calls for reinforcements
         */
        REINFORCEMENTS,
        /**
         * When a creature is spawned by nether portal
         */
        NETHER_PORTAL,
        /**
         * When a creature is spawned by a dispenser dispensing an egg
         */
        DISPENSE_EGG,
        /**
         * When a zombie infects a villager
         */
        INFECTION,
        /**
         * When a villager is cured from infection
         */
        CURED,
        /**
         * When an ocelot has a baby spawned along with them
         */
        OCELOT_BABY,
        /**
         * When a silverfish spawns from a block
         */
        SILVERFISH_BLOCK,
        /**
         * When an entity spawns as a mount of another entity (mostly chicken
         * jockeys)
         */
        MOUNT,
        /**
         * When an entity spawns as a trap for players approaching
         */
        TRAP,
        /**
         * When an entity is spawned as a result of ender pearl usage
         */
        ENDER_PEARL,
        /**
         * When an entity is spawned as a result of the entity it is being
         * perched on jumping or being damaged
         */
        SHOULDER_ENTITY,
        /**
         * When a creature is spawned by another entity drowning
         */
        DROWNED,
        /**
         * When an cow is spawned by shearing a mushroom cow
         */
        SHEARED,
        /**
         * When eg an effect cloud is spawned as a result of a creeper exploding
         */
        EXPLOSION,
        /**
         * When an entity is spawned as part of a raid
         */
        RAID,
        /**
         * When an entity is spawned as part of a patrol
         */
        PATROL,
        /**
         * When a bee is released from a beehive/bee nest
         */
        BEEHIVE,
        /**
         * When a piglin is converted to a zombified piglib.
         */
        PIGLIN_ZOMBIFIED,
        /**
         * When a creature is spawned by plugins
         */
        CUSTOM,
        /**
         * When an entity is missing a SpawnReason
         */
        DEFAULT
    }
}
