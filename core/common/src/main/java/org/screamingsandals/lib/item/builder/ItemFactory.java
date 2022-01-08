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

package org.screamingsandals.lib.item.builder;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.item.*;
import org.screamingsandals.lib.item.meta.*;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.ConfigurateUtils;
import org.screamingsandals.lib.utils.ConsumerExecutor;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
@ServiceDependencies(dependsOn = {
        ItemTypeMapper.class
})
public abstract class ItemFactory {

    private static final Function<ConfigurationNode, Item> CONFIGURATE_RESOLVER = node -> {
        if (!node.isMap()) {
            return readStack(node.getString()).orElse(null);
        }

        var builder = builder();

        var type = node.node("type");

        ShortStackDeserializer.deserializeShortStack(builder, type.getString());

        var meta = node.node("meta");
        if (!meta.empty()) {
            builder.platformMeta(ConfigurateUtils.toMap(meta));
        }

        var amount = node.node("amount");
        if (!amount.empty()) {
            builder.amount(amount.getInt(1));
        }

        var damage = node.node("damage");
        if (!damage.empty()) {
            builder.durability(damage.getInt(0));
        }
        var durability = node.node("durability");
        if (!durability.empty()) {
            builder.durability(durability.getInt(0));
        }

        var displayName = node.node("display-name");
        if (!displayName.empty()) {
            builder.displayName(AdventureHelper.toComponent(Objects.requireNonNull(displayName.getString())));
        }
        var locName = node.node("loc-name");
        if (!locName.empty()) {
            builder.localizedName(locName.getString());
        }
        var customModelData = node.node("custom-model-data");
        if (!customModelData.empty()) {
            try {
                builder.customModelData(locName.get(Integer.class));
            } catch (SerializationException ignored) {
            }
        }
        var repairCost = node.node("repair-cost");
        if (!repairCost.empty()) {
            builder.repairCost(repairCost.getInt());
        }
        var itemFlags = node.node("ItemFlags");
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
        var unbreakable = node.node("Unbreakable");
        if (!unbreakable.empty()) {
            builder.unbreakable(unbreakable.getBoolean(false));
        }
        var lore = node.node("lore");
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
        var enchants = node.node("enchants");
        if (!enchants.empty()) {
            if (enchants.isMap()) {
                enchants.childrenMap().entrySet().stream()
                        .map(EnchantmentMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(builder::enchantment);
            } else if (enchants.isList()) {
                try {
                    //noinspection ConstantConditions
                    enchants.getList(Object.class).stream()
                            .map(EnchantmentMapping::resolve)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(builder::enchantment);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    EnchantmentHolder.ofOptional(enchants.get(Object.class)).ifPresent(builder::enchantment);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            }
        }
        var potionType = node.node("potion-type");
        if (!potionType.empty()) {
            try {
                PotionHolder.ofOptional(potionType.get(Object.class)).ifPresent(builder::potion);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }
        var potionEffects = node.node("effects");
        if (!potionEffects.empty()) {
            if (potionEffects.isList()) {
                builder.effect(potionEffects.childrenList().stream()
                        .map(PotionEffectMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
            } else {
                PotionEffectHolder.ofOptional(potionEffects).ifPresent(builder::effect);
            }
        }

        var attributes = node.node("attributes");
        if (!attributes.empty()) {
            if (attributes.isList()) {
                attributes.childrenList().stream()
                        .map(AttributeMapping::wrapItemAttribute)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(builder::attributeModifier);
            } else {
                AttributeMapping.wrapItemAttribute(attributes).ifPresent(builder::attributeModifier);
            }
        }

        var recipes = node.node("recipes");
        if (!recipes.empty()) {
            if (recipes.isList()) {
                attributes.childrenList().stream()
                        .map(ConfigurationNode::getString)
                        .map(NamespacedMappingKey::ofOptional)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(builder::recipe);
            } else {
                NamespacedMappingKey.ofOptional(recipes.getString()).ifPresent(builder::recipe);
            }
        }

        var color = node.node("color");
        if (!color.empty()) {
            var c = TextColor.fromCSSHexString(color.getString(""));
            if (c != null) {
                builder.color(c);
            } else {
                var c2 = NamedTextColor.NAMES.value(color.getString("").trim().toLowerCase());
                if (c2 != null) {
                    builder.color(c2);
                }
            }
        }

        var fireworkEffects = node.node("firework-effects");
        if (!fireworkEffects.empty()) {
            if (fireworkEffects.isList()) {
                builder.fireworkEffect(fireworkEffects.childrenList().stream()
                        .map(FireworkEffectMapping::resolve)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
            } else {
                FireworkEffectHolder.ofOptional(fireworkEffects).ifPresent(builder::fireworkEffect);
            }
        }

        var power = node.node("power");
        if (!power.empty()) {
            builder.power(power.getInt());
        }

        return builder.build().orElse(null);
    };

    protected BidirectionalConverter<Item> itemConverter = BidirectionalConverter.<Item>build()
            .registerW2P(String.class, item -> item.getMaterial().platformName())
            .registerW2P(ItemTypeHolder.class, Item::getMaterial)
            .registerP2W(ConfigurationNode.class, CONFIGURATE_RESOLVER)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_RESOLVER.apply(BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .registerP2W(Item.class, Item::clone);

    private static ItemFactory factory;

    @ApiStatus.Internal
    public ItemFactory() {
        if (factory != null) {
            throw new UnsupportedOperationException("ItemFactory is already initialized.");
        }

        factory = this;
    }

    public static ItemBuilder builder() {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.builder0();
    }

    public static ItemView asView(Item item) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.asView0(item);
    }

    public static Optional<Item> build(Object stack) {
        return readStack(stack);
    }

    public static Optional<Item> build(Consumer<ItemBuilder> builderConsumer) {
        var builder = builder();
        ConsumerExecutor.execute(builderConsumer, builder);
        return builder.build();
    }

    public static Optional<Item> build(Object stack, Consumer<ItemBuilder> builderConsumer) {
        var item = readStack(stack);
        if (item.isEmpty()) {
            return Optional.empty();
        }

        if (builderConsumer != null) {
            var builder = item.get().builder();
            ConsumerExecutor.execute(builderConsumer, builder);
            return builder.build();
        }
        return item;
    }

    public static Optional<Item> readStack(Object stackObject) {
        var it = factory.itemConverter.convertOptional(stackObject);
        if (it.isPresent()) {
            return it;
        }
        return readShortStack(builder(), stackObject);
    }

    public static Optional<Item> readShortStack(Item item, Object shortStackObject) {
        var builder = item.builder();
        ShortStackDeserializer.deserializeShortStack(builder, shortStackObject);
        return builder.build();
    }

    public static Optional<Item> readShortStack(ItemBuilder builder, Object shortStackObject) {
        ShortStackDeserializer.deserializeShortStack(builder, shortStackObject);
        return builder.build();
    }

    public static List<Item> buildAll(List<Object> objects) {
        return objects.stream().map(o -> build(o).orElse(ItemFactory.getAir())).collect(Collectors.toList());
    }

    private static Item cachedAir;

    public static Item getAir() {
        if (cachedAir == null) {
            cachedAir = build("AIR").orElseThrow();
        }
        return cachedAir.clone();
    }

    public static <T> T convertItem(Item item, Class<T> newType) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.itemConverter.convert(item, newType);
    }

    protected abstract ItemBuilder builder0();

    protected abstract ItemView asView0(Item item);
}
