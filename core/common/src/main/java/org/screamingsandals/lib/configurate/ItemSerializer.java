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

package org.screamingsandals.lib.configurate;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.item.builder.ShortStackDeserializer;
import org.screamingsandals.lib.item.meta.*;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.nbt.configurate.TagSerializer;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.ConfigurateUtils;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class ItemSerializer extends AbstractScreamingSerializer implements TypeSerializer<ItemStack> {
    public static final @NotNull ItemSerializer INSTANCE = new ItemSerializer();

    private static final @NotNull SNBTSerializer internalSNBTSerializer = SNBTSerializer.builder().shouldSaveLongArraysDirectly(true).build();
    private static final @NotNull String TYPE_KEY = "type";
    private static final @NotNull String ID_KEY = "id"; // type alternative
    private static final @NotNull String META_KEY = "meta";
    private static final @NotNull String TAG_KEY = "tag";
    private static final @NotNull String AMOUNT_KEY = "amount";
    private static final @NotNull String COUNT_KEY = "count"; // amount alternative
    private static final @NotNull String DAMAGE_KEY = "damage";
    private static final @NotNull String DURABILITY_KEY = "durability";
    private static final @NotNull String DISPLAY_NAME_KEY = "display-name";
    private static final @NotNull String LOC_NAME_KEY = "loc-name";
    private static final @NotNull String CUSTOM_MODEL_DATA_KEY = "custom-model-data";
    private static final @NotNull String REPAIR_COST_KEY = "repair-cost";
    private static final @NotNull String ITEM_FLAGS_KEY = "ItemFlags";
    private static final @NotNull String UNBREAKABLE_KEY = "Unbreakable";
    private static final @NotNull String LORE_KEY = "lore";
    private static final @NotNull String ENCHANTS_KEY = "enchants";
    private static final @NotNull String POTION_TYPE_KEY = "potion-type";
    private static final @NotNull String EFFECTS_KEY = "effects";
    private static final @NotNull String ATTRIBUTES_KEY = "attributes";
    private static final @NotNull String RECIPES_KEY = "recipes";
    private static final @NotNull String COLOR_KEY = "color";
    private static final @NotNull String FIREWORK_EFFECTS_KEY = "firework-effects";
    private static final @NotNull String POWER_KEY = "power";

    @Override
    public @NotNull ItemStack deserialize(@NotNull Type classType, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            if (!node.isMap()) {
                var builder = ItemStackFactory.builder();

                ShortStackDeserializer.deserializeShortStack(builder, node.getString());

                return builder.build().orElseThrow();
            }

            var builder = ItemStackFactory.builder();

            var type = node.node(TYPE_KEY);

            if (type.empty()) {
                type = node.node(ID_KEY);
            }

            ShortStackDeserializer.deserializeShortStack(builder, type.getString());

            var tag = node.node(TAG_KEY);
            if (!tag.empty()) {
                if (tag.isMap()) {
                    var nbtTag = TagSerializer.INSTANCE.deserialize(Tag.class, tag);
                    if (!(nbtTag instanceof CompoundTag)) {
                        throw new IllegalArgumentException(TAG_KEY + " should be a compound tag, got " + nbtTag);
                    }
                    builder.tag((CompoundTag) nbtTag);
                } else {
                    var snbtTag = internalSNBTSerializer.deserialize(tag.getString(""));
                    if (!(snbtTag instanceof CompoundTag)) {
                        throw new IllegalArgumentException(TAG_KEY + " should be a compound tag, got " + snbtTag);
                    }
                    builder.tag((CompoundTag) snbtTag);
                }
            }

            var meta = node.node(META_KEY);
            if (!meta.empty()) {
                builder.platformMeta(ConfigurateUtils.toMap(meta));
            }

            var amount = node.node(AMOUNT_KEY);
            if (!amount.empty()) {
                builder.amount(amount.getInt(1));
            } else {
                var count = node.node(COUNT_KEY);
                if (!count.empty()) {
                    builder.amount(count.getInt(1));
                }
            }

            var damage = node.node(DAMAGE_KEY);
            if (!damage.empty()) {
                builder.durability(damage.getInt(0));
            }
            var durability = node.node(DURABILITY_KEY);
            if (!durability.empty()) {
                builder.durability(durability.getInt(0));
            }

            var displayName = node.node(DISPLAY_NAME_KEY);
            if (!displayName.empty()) {
                builder.displayName(Component.fromLegacy(displayName.getString()));
            }
            var locName = node.node(LOC_NAME_KEY);
            if (!locName.empty()) {
                builder.localizedName(locName.getString());
            }
            var customModelData = node.node(CUSTOM_MODEL_DATA_KEY);
            if (!customModelData.empty()) {
                try {
                    builder.customModelData(locName.get(Integer.class));
                } catch (SerializationException ignored) {
                }
            }
            var repairCost = node.node(REPAIR_COST_KEY);
            if (!repairCost.empty()) {
                builder.repairCost(repairCost.getInt());
            }
            var itemFlags = node.node(ITEM_FLAGS_KEY);
            if (!itemFlags.empty()) {
                if (itemFlags.isList()) {
                    try {
                        builder.flags(itemFlags.getList(String.class));
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.hideFlag(HideFlags.convert(itemFlags.getString("")));
                }
            }
            var unbreakable = node.node(UNBREAKABLE_KEY);
            if (!unbreakable.empty()) {
                builder.unbreakable(unbreakable.getBoolean(false));
            }
            var lore = node.node(LORE_KEY);
            if (!lore.empty()) {
                if (lore.isList()) {
                    try {
                        final var list = Objects.requireNonNull(lore.getList(String.class));
                        builder.lore(list);
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.lore(lore.getString());
                }
            }
            var enchants = node.node(ENCHANTS_KEY);
            if (!enchants.empty()) {
                if (enchants.isMap()) {
                    enchants.childrenMap().entrySet().stream()
                            .map(Enchantment::ofNullable)
                            .filter(Objects::nonNull)
                            .forEach(builder::enchantment);
                } else if (enchants.isList()) {
                    try {
                        //noinspection ConstantConditions
                        enchants.getList(Object.class).stream()
                                .map(Enchantment::ofNullable)
                                .filter(Objects::nonNull)
                                .forEach(builder::enchantment);
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Enchantment.ofNullable(enchants.get(Object.class)).ifNotNull(builder::enchantment);
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                }
            }
            var potionType = node.node(POTION_TYPE_KEY);
            if (!potionType.empty()) {
                try {
                    Potion.ofNullable(potionType.get(Object.class)).ifNotNull(builder::potion);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            }
            var potionEffects = node.node(EFFECTS_KEY);
            if (!potionEffects.empty()) {
                if (potionEffects.isList()) {
                    builder.effect(potionEffects.childrenList().stream()
                            .map(PotionEffect::ofNullable)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                } else {
                    PotionEffect.ofNullable(potionEffects).ifNotNull(builder::effect);
                }
            }

            var attributes = node.node(ATTRIBUTES_KEY);
            if (!attributes.empty()) {
                if (attributes.isList()) {
                    attributes.childrenList().stream()
                            .map(AttributeMapping::wrapItemAttribute)
                            .filter(Objects::nonNull)
                            .forEach(builder::attributeModifier);
                } else {
                    AttributeMapping.wrapItemAttribute(attributes).ifNotNull(builder::attributeModifier);
                }
            }

            var recipes = node.node(RECIPES_KEY);
            if (!recipes.empty()) {
                if (recipes.isList()) {
                    attributes.childrenList().stream()
                            .map(ConfigurationNode::getString)
                            .map(ResourceLocation::ofNullable)
                            .filter(Objects::nonNull)
                            .forEach(builder::recipe);
                } else {
                    var res = ResourceLocation.ofNullable(recipes.getString());
                    if (res != null) {
                        builder.recipe(res);
                    }
                }
            }

            var color = node.node(COLOR_KEY);
            if (!color.empty()) {
                var c = Color.hexOrName(color.getString(""));
                if (c != null) {
                    builder.color(c);
                }
            }

            var fireworkEffects = node.node(FIREWORK_EFFECTS_KEY);
            if (!fireworkEffects.empty()) {
                if (fireworkEffects.isList()) {
                    builder.fireworkEffect(fireworkEffects.childrenList().stream()
                            .map(FireworkEffectMapping::resolve)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                } else {
                    FireworkEffectHolder.ofNullable(fireworkEffects).ifNotNull(builder::fireworkEffect);
                }
            }

            var power = node.node(POWER_KEY);
            if (!power.empty()) {
                builder.power(power.getInt());
            }

            return builder.build().orElseThrow();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ItemStack obj, @NotNull ConfigurationNode node) throws SerializationException {
        node.set(null);

        if (obj != null) {
            ItemTypeHolderSerializer.INSTANCE.serialize(ItemTypeHolder.class, obj.getType(), node.node(TYPE_KEY));
            node.node(AMOUNT_KEY).set(obj.getAmount());
            var tag = obj.getTag();
            if (!tag.isEmpty()) {
                TagSerializer.INSTANCE.serialize(Tag.class, tag, node.node(TAG_KEY));
            }
        }
    }
}
