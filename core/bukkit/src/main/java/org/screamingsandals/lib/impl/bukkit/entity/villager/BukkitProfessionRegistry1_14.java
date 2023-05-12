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

package org.screamingsandals.lib.impl.bukkit.entity.villager;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.Arrays;
import java.util.List;

public class BukkitProfessionRegistry1_14 extends BukkitProfessionRegistry {
    public BukkitProfessionRegistry1_14() {
        specialType(Villager.Profession.class, BukkitProfession1_8::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Profession> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(Villager.Profession.values()),
                BukkitProfession1_8::new,
                profession -> {
                    var bukkitKey = profession.getKey();
                    return ResourceLocation.of(bukkitKey.getNamespace(), bukkitKey.getKey());
                },
                (profession, literal) -> profession.getKey().getKey().contains(literal),
                (profession, namespace) -> profession.getKey().getNamespace().equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable Profession resolveMappingPlatform(@NotNull ResourceLocation location) {
        var value = Registry.VILLAGER_PROFESSION.get(new NamespacedKey(location.namespace(), location.path()));
        if (value != null) {
            return new BukkitProfession1_8(value);
        }
        return null;
    }
}
