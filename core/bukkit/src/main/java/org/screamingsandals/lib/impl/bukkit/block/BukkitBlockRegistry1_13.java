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

package org.screamingsandals.lib.impl.bukkit.block;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.block.tags.ModernBlockTagBackPorts;
import org.screamingsandals.lib.impl.bukkit.item.MaterialRegistry1_13_X;
import org.screamingsandals.lib.impl.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.impl.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.*;

public class BukkitBlockRegistry1_13 extends BukkitBlockRegistry {
    private static final @NotNull Map<@NotNull Material, List<String>> tagBackPorts = new HashMap<>();

    public BukkitBlockRegistry1_13() {
        specialType(Material.class, mat -> mat.isBlock() ? new BukkitBlock1_13(mat) : null);
        specialType(BlockData.class, BukkitBlock1_13::new);

        // Tag backports

        Arrays.stream(Material.values())
                .filter(t -> !t.isLegacy() && t.isBlock())
                .forEach(material -> {
                    var holder = new BukkitBlock1_13(material);
                    var namespaced = material.getKey();
                    /* we are probably not able to backport non-minecraft block tags (probably except mineable/* and similar, but we are not able to backport them yet */
                    if (NamespacedKey.MINECRAFT.equals(namespaced.getNamespace())) {
                        var backPorts = ModernBlockTagBackPorts.getPortedTags(holder, s ->
                            KeyedUtils.isTagged(Tag.REGISTRY_BLOCKS, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), Material.class, material)
                        );
                        if (backPorts != null && !backPorts.isEmpty()) {
                            tagBackPorts.put(material, backPorts);
                        }
                    }
                });
    }

    @Override
    protected @Nullable Block resolveMappingPlatform(@NotNull ResourceLocation location, @Nullable String blockState) {
        var material = MaterialRegistry1_13_X.getInstance().get(new NamespacedKey(location.namespace(), location.path()));
        if (material != null && material.isBlock()) {
            if (blockState != null) {
                try {
                    return new BukkitBlock1_13(material.createBlockData(blockState));
                } catch (IllegalArgumentException ignored) {
                    return null; // can't read the block state
                }
            } else {
                return new BukkitBlock1_13(material);
            }
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Block> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () ->  Arrays.stream(Material.values()).filter(t -> !t.isLegacy() && t.isBlock()),
                BukkitBlock1_13::new,
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
