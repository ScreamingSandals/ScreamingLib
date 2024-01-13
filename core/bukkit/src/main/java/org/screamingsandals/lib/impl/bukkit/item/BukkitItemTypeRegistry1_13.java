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

package org.screamingsandals.lib.impl.bukkit.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.impl.item.tags.ModernItemTagsBackPorts;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BukkitItemTypeRegistry1_13 extends BukkitItemTypeRegistry {
    private static final @NotNull Map<@NotNull Material, List<String>> tagBackPorts = new HashMap<>();

    public BukkitItemTypeRegistry1_13() {
        specialType(Material.class, mat -> mat.isItem() ? new BukkitItemType1_13(mat) : null);
        specialType(ItemStack.class, stack -> new BukkitItemType1_13(stack.getType()));

        // Tag backports

        tagBackPorts.clear();

        Arrays.stream(Material.values())
                .filter(t -> !t.isLegacy() && t.isItem())
                .forEach(material -> {
                    /* we are probably not able to backport non-minecraft item tags */
                    if (NamespacedKey.MINECRAFT.equals(material.getKey().getNamespace())) {
                        var backPorts = ModernItemTagsBackPorts.getPortedTags(new BukkitItemType1_13(material), s ->
                                KeyedUtils.isTagged(Tag.REGISTRY_ITEMS, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), Material.class, material)
                        );
                        if (backPorts != null && !backPorts.isEmpty()) {
                            tagBackPorts.put(material, backPorts);
                        }
                    }
                });
    }

    @Override
    protected @Nullable ItemType resolveMappingPlatform(@NotNull ResourceLocation location) {
        var material = MaterialRegistry1_13_X.getInstance().get(new NamespacedKey(location.namespace(), location.path()));
        if (material != null && material.isItem()) {
            return new BukkitItemType1_13(material);
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ItemType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () ->  Arrays.stream(Material.values()).filter(t -> !t.isLegacy() && t.isItem()),
                BukkitItemType1_13::new,
                material -> ResourceLocation.of(material.getKey().getNamespace(), material.getKey().getKey()),
                (material, literal) -> material.getKey().getKey().contains(literal),
                (material, namespace) -> material.getKey().getNamespace().equals(namespace),
                List.of()
        );
    }

    public static boolean hasTagInBackPorts(@NotNull Material material, @NotNull String tag) {
        return tagBackPorts.containsKey(material) && tagBackPorts.get(material).contains(tag);
    }
}
