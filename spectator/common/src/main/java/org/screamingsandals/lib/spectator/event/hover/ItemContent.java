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

package org.screamingsandals.lib.spectator.event.hover;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface ItemContent extends Content, ItemContentLike {
    static ItemContent.Builder builder() {
        return Spectator.getBackend().itemContent();
    }

    NamespacedMappingKey id();

    int count();

    // TODO: NBT api
    @Nullable
    String tag();

    @Override
    default ItemContent asItemContent() {
        return this;
    }

    @Override
    default Content asContent() {
        return this;
    }

    interface Builder {
        Builder id(NamespacedMappingKey id);

        Builder count(int count);

        // TODO: NBT api
        Builder tag(@Nullable String tag);

        ItemContent build();
    }
}
