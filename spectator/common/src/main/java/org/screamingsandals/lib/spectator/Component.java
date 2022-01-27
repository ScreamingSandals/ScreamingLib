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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Collection;
import java.util.List;

public interface Component extends ComponentLike, Wrapper, Content, RawValueHolder {

    static Component empty() {
        return Spectator.getBackend().empty();
    }

    static BlockNBTComponent.Builder blockNBT() {
        return Spectator.getBackend().blockNBT();
    }

    static EntityNBTComponent.Builder entityNBT() {
        return Spectator.getBackend().entityNBT();
    }

    static KeybindComponent.Builder keybind() {
        return Spectator.getBackend().keybind();
    }

    static ScoreComponent.Builder score() {
        return Spectator.getBackend().score();
    }

    static SelectorComponent.Builder selector() {
        return Spectator.getBackend().selector();
    }

    static StorageNBTComponent.Builder storageNBT() {
        return Spectator.getBackend().storageNBT();
    }

    static TextComponent.Builder text() {
        return Spectator.getBackend().text();
    }

    static TranslatableComponent.Builder translatable() {
        return Spectator.getBackend().translatable();
    }

    @Unmodifiable
    List<Component> children();

    @Nullable
    Color color();

    @LimitedVersionSupport(">= 1.16")
    @Nullable
    NamespacedMappingKey font();

    boolean bold();

    boolean italic();

    boolean underlined();

    boolean strikethrough();

    boolean obfuscated();

    @Nullable
    String insertion();

    @Nullable
    HoverEvent hoverEvent();

    @Nullable
    ClickEvent clickEvent();

    @Override
    default Component asComponent() {
        return this;
    }

    @Override
    default Content asContent() {
        return this;
    }

    interface Builder<B extends Builder<B, C>, C extends Component> {
        B color(Color color);

        B append(Component component);

        B append(Component... components);

        B append(Collection<Component> components);

        @LimitedVersionSupport(">= 1.16")
        B font(NamespacedMappingKey font);

        default B bold() {
            return bold(true);
        }

        B bold(boolean bold);

        default B italic() {
            return italic(true);
        }

        B italic(boolean italic);

        default B underlined() {
            return underlined(true);
        }

        B underlined(boolean underlined);

        default B strikethrough() {
            return strikethrough(true);
        }

        B strikethrough(boolean strikethrough);

        default B obfuscated() {
            return obfuscated(true);
        }

        B obfuscated(boolean obfuscated);

        B insertion(@Nullable String insertion);

        B hoverEvent(@Nullable HoverEvent event);

        B clickEvent(@Nullable ClickEvent event);

        C build();
    }
}
