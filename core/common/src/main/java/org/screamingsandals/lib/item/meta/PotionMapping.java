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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.PotionHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class PotionMapping extends AbstractTypeMapper<PotionHolder> {
    private static PotionMapping potionMapping;
    protected BidirectionalConverter<PotionHolder> potionConverter = BidirectionalConverter.<PotionHolder>build()
            .registerP2W(PotionHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return PotionHolderSerializer.INSTANCE.deserialize(PotionHolder.class, node);
                } catch (SerializationException e) {
                    e.printStackTrace();
                    return null;
                }
            });

    protected PotionMapping() {
        if (potionMapping != null) {
            throw new UnsupportedOperationException("Potion mapping is already initialized.");
        }

        potionMapping = this;
    }

    @OnPostConstruct
    public void bukkit2minecraftMapping() {
        mapAlias("EMPTY", "UNCRAFTABLE");
        mapAlias("LEAPING", "JUMP");
        mapAlias("SWIFTNESS", "SPEED");
        mapAlias("HEALING", "INSTANT_HEAL");
        mapAlias("HARMING", "INSTANT_DAMAGE");
        mapAlias("REGENERATION", "REGEN");
    }

    protected void mapAlias(String potion, String potionBukkit) {
        if (potion == null || potionBukkit == null) {
            throw new IllegalArgumentException("Both potions mustn't be null!");
        }

        super.mapAlias(potion, potionBukkit);
        super.mapAlias("long_" + potion, "long_" + potionBukkit);
        super.mapAlias("strong_" + potion, "strong_" + potionBukkit);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @OfMethodAlternative(value = PotionHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable PotionHolder resolve(@Nullable Object potionObject) {
        if (potionMapping == null) {
            throw new UnsupportedOperationException("Potion mapping is not initialized yet.");
        }
        if (potionObject == null) {
            return null;
        }

        return potionMapping.potionConverter.convertOptional(potionObject).or(() -> potionMapping.resolveFromMapping(potionObject)).orElse(null);
    }

    @OfMethodAlternative(value = PotionHolder.class, methodName = "all")
    public static @NotNull List<@NotNull PotionHolder> getValues() {
        if (potionMapping == null) {
            throw new UnsupportedOperationException("PotionMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(potionMapping.values);
    }
}
