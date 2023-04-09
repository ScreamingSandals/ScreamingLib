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

package org.screamingsandals.lib.attribute;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface AttributeType extends RegistryItem, RawValueHolder {

    @ApiStatus.Experimental
    @NotNull String platformName();

    static @NotNull AttributeType of(@MinecraftType(MinecraftType.Type.ATTRIBUTE_TYPE) @NotNull Object attributeType) {
        var result = ofNullable(attributeType);
        Preconditions.checkNotNullIllegal(result, "Could not find attribute type: " + attributeType);
        return result;
    }

    @Contract("null -> null")
    static @Nullable AttributeType ofNullable(@MinecraftType(MinecraftType.Type.ATTRIBUTE_TYPE) @Nullable Object attributeType) {
        if (attributeType instanceof AttributeType) {
            return (AttributeType) attributeType;
        }
        return AttributeTypeRegistry.getInstance().resolveMapping(attributeType);
    }

    static @NotNull RegistryItemStream<@NotNull AttributeType> all() {
        return AttributeTypeRegistry.getInstance().getRegistryItemStream();
    }

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ATTRIBUTE_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ATTRIBUTE_TYPE) @Nullable Object @NotNull... objects);
}
