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

package org.screamingsandals.lib.item.builder;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.ItemSerializer;
import org.screamingsandals.lib.item.*;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
@ProvidedService
@ServiceDependencies(dependsOn = ItemTypeMapper.class)
public abstract class ItemStackFactory {

    private static final @NotNull Function<@NotNull ConfigurationNode, @Nullable ItemStack> CONFIGURATE_RESOLVER = node -> {
        try {
            return ItemSerializer.INSTANCE.deserialize(ItemStack.class, node);
        } catch (SerializationException e) {
            return null;
        }
    };

    protected final @NotNull BidirectionalConverter<ItemStack> itemConverter = BidirectionalConverter.<ItemStack>build()
            .registerW2P(String.class, item -> item.getMaterial().platformName())
            .registerW2P(ItemTypeHolder.class, ItemStack::getMaterial)
            .registerP2W(ConfigurationNode.class, CONFIGURATE_RESOLVER)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_RESOLVER.apply(BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .registerP2W(ItemStack.class, ItemStack::clone);

    private static @Nullable ItemStackFactory factory;

    @ApiStatus.Internal
    public ItemStackFactory() {
        if (factory != null) {
            throw new UnsupportedOperationException("ItemFactory is already initialized.");
        }

        factory = this;
    }

    public static @NotNull ItemStackBuilder builder() {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.builder0();
    }

    public static @NotNull ItemStackView asView(@NotNull ItemStack item) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.asView0(item);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Contract("null -> null")
    public static @Nullable ItemStack build(@Nullable Object stack) {
        return readStack(stack);
    }

    public static @Nullable ItemStack build(@NotNull ReceiverConsumer<@NotNull ItemStackBuilder> builderConsumer) {
        var builder = builder();
        builderConsumer.accept(builder);
        return builder.build();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Contract("null,_ -> null")
    public static @Nullable ItemStack build(@Nullable Object stack, @Nullable ReceiverConsumer<@NotNull ItemStackBuilder> builderConsumer) {
        var item = readStack(stack);
        if (item == null) {
            return null;
        }

        if (builderConsumer != null) {
            var builder = item.builder();
            builderConsumer.accept(builder);
            return builder.build();
        }
        return item;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public static @Nullable ItemStack readStack(@Nullable Object stackObject) {
        return factory.itemConverter.convertOptional(stackObject).orElseGet(() -> readShortStack(builder(), stackObject));
    }

    public static @Nullable ItemStack readShortStack(@NotNull ItemStack item, @Nullable Object shortStackObject) {
        var builder = item.builder();
        ShortStackDeserializer.deserializeShortStack(builder, shortStackObject);
        return builder.build();
    }

    public static @Nullable ItemStack readShortStack(@NotNull ItemStackBuilder builder, @Nullable Object shortStackObject) {
        ShortStackDeserializer.deserializeShortStack(builder, shortStackObject);
        return builder.build();
    }

    public static @NotNull List<@NotNull ItemStack> buildAll(@NotNull List<@Nullable Object> objects) {
        return objects.stream().map(o -> build(o).orElse(ItemStackFactory.getAir())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static @Nullable ItemStack cachedAir;

    public static @NotNull ItemStack getAir() {
        if (cachedAir == null) {
            cachedAir = build("AIR");
            Preconditions.checkNotNullIllegal(cachedAir, "Could not find item: " + cachedAir);
        }
        return cachedAir.clone();
    }

    public static <T> T convertItem(@NotNull ItemStack item, @NotNull Class<T> newType) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.itemConverter.convert(item, newType);
    }

    protected abstract @NotNull ItemStackBuilder builder0();

    protected abstract @NotNull ItemStackView asView0(@NotNull ItemStack item);
}
