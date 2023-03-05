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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.NotNull;

public interface CreatureSpawnEvent extends EntitySpawnEvent {

    @NotNull SpawnReason spawnReason();

    /**
     * An enum to specify the type of spawning
     */
    // TODO: holder?
    enum SpawnReason {

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
