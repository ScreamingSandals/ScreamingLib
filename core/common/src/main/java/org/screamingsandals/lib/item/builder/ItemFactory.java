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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.ItemSerializer;
import org.screamingsandals.lib.item.*;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
@ServiceDependencies(dependsOn = {
        ItemTypeMapper.class
})
public abstract class ItemFactory {

    private static final @NotNull Function<@NotNull ConfigurationNode, @Nullable Item> CONFIGURATE_RESOLVER = node -> {
        try {
            return ItemSerializer.INSTANCE.deserialize(Item.class, node);
        } catch (SerializationException e) {
            return null;
        }
    };

    protected @NotNull BidirectionalConverter<Item> itemConverter = BidirectionalConverter.<Item>build()
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

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public static Optional<Item> build(Object stack) {
        return readStack(stack);
    }

    public static Optional<Item> build(ReceiverConsumer<ItemBuilder> builderConsumer) {
        var builder = builder();
        builderConsumer.accept(builder);
        return builder.build();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public static Optional<Item> build(Object stack, ReceiverConsumer<ItemBuilder> builderConsumer) {
        var item = readStack(stack);
        if (item.isEmpty()) {
            return Optional.empty();
        }

        if (builderConsumer != null) {
            var builder = item.get().builder();
            builderConsumer.accept(builder);
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
