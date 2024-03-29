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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.spectator.StaticAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;

import java.util.List;

public interface AudienceComponentLike extends ComponentLike {
    /**
     * Resolves the component for specific audience. Should not be used with {@link Audience.ForwardingToMulti}.
     *
     * @param audience audience
     * @return new component
     */
    @ApiStatus.Internal
    @NotNull Component asComponent(@Nullable Audience audience);

    /**
     * Resolves the component for specific audience. Should not be used with {@link Audience.ForwardingToMulti}.
     *
     * @param audience audience
     * @return new components in list
     */
    @ApiStatus.Internal
    default @NotNull List<Component> asComponentList(@Nullable Audience audience) {
        return List.of(asComponent(audience));
    }

    static @NotNull AudienceComponentLike empty() {
        return new StaticAudienceComponentLike(Component.empty());
    }

    static @NotNull AudienceComponentLike of(@NotNull Component component) {
        return new StaticAudienceComponentLike(component);
    }

    static @NotNull AudienceComponentLike of(@NotNull ComponentLike component) {
        if (component instanceof AudienceComponentLike) {
            return (AudienceComponentLike) component;
        }
        return new StaticAudienceComponentLike(component.asComponent());
    }
}
