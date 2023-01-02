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

package org.screamingsandals.lib.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SelectorComponent;

public class BungeeSelectorComponent extends BungeeComponent implements SelectorComponent {
    protected BungeeSelectorComponent(net.md_5.bungee.api.chat.@NotNull SelectorComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String pattern() {
        return ((net.md_5.bungee.api.chat.SelectorComponent) wrappedObject).getSelector();
    }

    @Override
    public @NotNull SelectorComponent withPattern(String pattern) {
        var duplicate = (net.md_5.bungee.api.chat.SelectorComponent) wrappedObject.duplicate();
        duplicate.setSelector(pattern);
        return (SelectorComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull SelectorComponent.Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.SelectorComponent) wrappedObject.duplicate();
        return new BungeeSelectorBuilder(duplicate);
    }

    @Override
    public @Nullable Component separator() {
        return null; // TODO: WHERE IS IT ???
    }

    @Override
    public @NotNull SelectorComponent withSeparator(@Nullable Component separator) {
        return this; // TODO: WHERE IS IT ???
    }

    public static class BungeeSelectorBuilder extends BungeeBuilder<SelectorComponent, SelectorComponent.Builder, net.md_5.bungee.api.chat.SelectorComponent> implements SelectorComponent.Builder {

        public BungeeSelectorBuilder(net.md_5.bungee.api.chat.SelectorComponent component) {
            super(component);
        }

        @Override
        public @NotNull SelectorComponent.Builder pattern(@NotNull String pattern) {
            component.setSelector(pattern);
            return this;
        }

        @Override
        public @NotNull SelectorComponent.Builder separator(@Nullable Component separator) {
            // TODO: Hey md_5, I hate you with all my hearth
            return this;
        }
    }
}
