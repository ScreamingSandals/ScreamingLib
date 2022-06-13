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

package org.screamingsandals.lib.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.List;

public interface SenderMessage extends AudienceComponentLike {
    @Override
    @NotNull
    default Component asComponent(@Nullable Audience audience) {
        if (audience instanceof CommandSenderWrapper) {
            return asComponent((CommandSenderWrapper) audience);
        }
        return asComponent(null);
    }

    @NotNull
    Component asComponent(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    List<Component> asComponentList(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    default TextEntry asTextEntry(@Nullable CommandSenderWrapper wrapper) {
        return TextEntry.of(asComponent(wrapper));
    }

    @NotNull
    default TextEntry asTextEntry(@NotNull String identifier, @Nullable CommandSenderWrapper wrapper) {
        return TextEntry.of(identifier, asComponent(wrapper));
    }

    static SenderMessage empty() {
        return new StaticSenderMessage(Component.empty());
    }

    static SenderMessage of(Component component) {
        return new StaticSenderMessage(component);
    }

    static SenderMessage of(ComponentLike component) {
        return new StaticSenderMessage(component.asComponent());
    }
}
