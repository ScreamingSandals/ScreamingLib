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

package org.screamingsandals.lib.impl.bukkit.particle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BukkitParticleTypeRegistry1_8 extends BukkitParticleTypeRegistry {
    public static final @NotNull Map<@NotNull String, Integer> PARTICLE_NAME_TO_ORDINAL = Map.ofEntries(
        // flattening name or old enum name if not present in 1.13 -> ordinal id
        Map.entry("poof", 0),
        Map.entry("explosion", 1),
        Map.entry("explosion_emitter", 2),
        Map.entry("firework", 3),
        Map.entry("bubble", 4),
        Map.entry("splash", 5),
        Map.entry("fishing", 6),
        Map.entry("underwater", 7),
        Map.entry("suspended_depth", 8), // old name
        Map.entry("crit", 9),
        Map.entry("enchanted_hit", 10),
        Map.entry("smoke", 11),
        Map.entry("large_smoke", 12),
        Map.entry("effect", 13),
        Map.entry("instant_effect", 14),
        Map.entry("entity_effect", 15),
        Map.entry("ambient_entity_effect", 16),
        Map.entry("witch", 17),
        Map.entry("dripping_water", 18),
        Map.entry("dripping_lava", 19),
        Map.entry("angry_villager", 20),
        Map.entry("happy_villager", 21),
        Map.entry("mycelium", 22),
        Map.entry("note", 23),
        Map.entry("portal", 24),
        Map.entry("enchant", 25),
        Map.entry("flame", 26),
        Map.entry("lava", 27),
        Map.entry("footstep", 28), // old name
        Map.entry("cloud", 29),
        Map.entry("dust", 30),
        Map.entry("item_snowball", 31),
        Map.entry("snow_shovel", 32), // old name
        Map.entry("item_slime", 33),
        Map.entry("heart", 34),
        Map.entry("block_marker", 35), // barrier before 1.18+
        Map.entry("item", 36),
        Map.entry("block", 37),
        Map.entry("block_dust", 38), // old name
        Map.entry("rain", 39),
        Map.entry("take", 40), // old name
        Map.entry("elder_guardian", 41)
    );
    public static final @NotNull Map<@NotNull Integer, String> ORDINAL_TO_PARTICLE_NAME = PARTICLE_NAME_TO_ORDINAL
            .entrySet()
            .stream()
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));

    @Override
    protected @Nullable ParticleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        var id = PARTICLE_NAME_TO_ORDINAL.get(location.path());
        if (id != null) {
            return new BukkitParticleType1_8(id);
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ParticleType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> PARTICLE_NAME_TO_ORDINAL.entrySet().stream(),
                entry -> new BukkitParticleType1_8(entry.getValue()),
                entry -> ResourceLocation.of(entry.getKey()),
                (entry, literal) -> entry.getKey().contains(literal),
                (entry, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

}
