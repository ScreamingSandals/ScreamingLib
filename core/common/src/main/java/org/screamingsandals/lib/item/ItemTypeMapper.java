/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.item;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.configurate.ItemTypeHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.key.ComplexMappingKey;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService
public abstract class ItemTypeMapper extends AbstractTypeMapper<ItemTypeHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?<namespaced>(?:([A-Za-z][A-Za-z0-9_.\\-]*):)?[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?::)?(?<durability>\\d+)?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");
    protected BidirectionalConverter<ItemTypeHolder> itemTypeConverter = BidirectionalConverter.<ItemTypeHolder>build()
            .registerP2W(ItemTypeHolder.class, i -> i)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return ItemTypeHolderSerializer.INSTANCE.deserialize(ItemTypeHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    private static ItemTypeMapper itemTypeMapper;
    private static ItemTypeHolder cachedAir;

    @ApiStatus.Internal
    public ItemTypeMapper() {
        if (itemTypeMapper != null) {
            throw new UnsupportedOperationException("ItemTypeMapper is already initialized.");
        }

        itemTypeMapper = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @OfMethodAlternative(value = ItemTypeHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable ItemTypeHolder resolve(@Nullable Object materialObject) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        if (materialObject == null) {
            return null;
        }

        return itemTypeMapper.itemTypeConverter.convertOptional(materialObject).or(() -> {
            var material = materialObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(material);

            if (matcher.matches()) {
                if (matcher.group("namespaced") != null) {

                    var namespaced = NamespacedMappingKey.of(matcher.group("namespaced"));

                    Integer data = null;
                    try {
                        data = Integer.parseInt(matcher.group("durability"));
                    } catch (NumberFormatException ignored) {
                    }

                    if (data != null) {
                        var namespacedDurability = ComplexMappingKey.of(namespaced, NumericMappingKey.of(data));

                        if (itemTypeMapper.mapping.containsKey(namespacedDurability)) {
                            return Optional.of(itemTypeMapper.mapping.get(namespacedDurability));
                        } else if (itemTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = itemTypeMapper.mapping.get(namespaced);
                            return Optional.of(holder.withForcedDurability(data.shortValue()));
                        }
                    } else if (itemTypeMapper.mapping.containsKey(namespaced)) {
                        return Optional.of(itemTypeMapper.mapping.get(namespaced));
                    }
                } else if (matcher.group("id") != null) {
                    try {
                        var id = Integer.parseInt(matcher.group("id"));
                        int data = 0;
                        try {
                            data = Integer.parseInt(matcher.group("data"));
                        } catch (NumberFormatException ignored) {
                        }

                        var keyWithData = ComplexMappingKey.of(NumericMappingKey.of(id), NumericMappingKey.of(data));
                        var key = NumericMappingKey.of(id);

                        if (itemTypeMapper.mapping.containsKey(keyWithData)) {
                            return Optional.of(itemTypeMapper.mapping.get(keyWithData));
                        } else if (itemTypeMapper.mapping.containsKey(key)) {
                            var holder = itemTypeMapper.mapping.get(key);
                            return Optional.of(holder.withForcedDurability((short) data));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return Optional.empty();
        }).orElse(null);
    }

    @OfMethodAlternative(value = ItemTypeHolder.class, methodName = "all")
    public static @NotNull List<@NotNull ItemTypeHolder> getValues() {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return Collections.unmodifiableList(itemTypeMapper.values);
    }

    public static ItemTypeHolder colorize(ItemTypeHolder holder, String color) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return ItemBlockIdsRemapper.colorableItems.entrySet().stream()
                .filter(c -> c.getKey().test(holder))
                .map(Map.Entry::getValue)
                .findFirst()
                .flatMap(fun -> fun.apply(color))
                .orElse(holder);
    }

    public Map<MappingKey, ItemTypeHolder> getUNSAFE_mapping() {
        return mapping;
    }

    @Override
    public void mapAlias(String mappingKey, String alias) {
        super.mapAlias(mappingKey, alias);
    }

    @OfMethodAlternative(value = ItemTypeHolder.class, methodName = "air")
    public static ItemTypeHolder getCachedAir() {
        if (cachedAir == null) {
            cachedAir = resolve("minecraft:air");
            Preconditions.checkNotNullIllegal(cachedAir, "Could not find item type: minecraft:air");
        }
        return cachedAir;
    }
}
