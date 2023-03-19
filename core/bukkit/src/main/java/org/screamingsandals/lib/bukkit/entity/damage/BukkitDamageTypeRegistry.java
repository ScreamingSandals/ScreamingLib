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

package org.screamingsandals.lib.bukkit.entity.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.damage.DamageType;
import org.screamingsandals.lib.entity.damage.DamageTypeRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitDamageTypeRegistry extends DamageTypeRegistry {

    // TODO: Implement new API when available in either Spigot or Paper; this old API no longer reflects reality

    public BukkitDamageTypeRegistry() {
        specialType(EntityDamageEvent.DamageCause.class, BukkitDamageType::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull DamageType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(EntityDamageEvent.DamageCause.values()),
                BukkitDamageType::new,
                damageCause -> ResourceLocation.of(damageCause.name()),
                (damageCause, literal) -> damageCause.name().toLowerCase(Locale.ROOT).contains(literal),
                (damageCause, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable DamageType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            // TODO: we probably need some translation here
            var value = EntityDamageEvent.DamageCause.valueOf(location.path().toUpperCase(Locale.ROOT));
            return new BukkitDamageType(value);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }
}
