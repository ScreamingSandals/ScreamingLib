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

package org.screamingsandals.lib.adventure.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SelectorComponent;

public class AdventureSelectorComponent extends AdventureComponent implements SelectorComponent {
    public AdventureSelectorComponent(net.kyori.adventure.text.@NotNull SelectorComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String pattern() {
        return ((net.kyori.adventure.text.SelectorComponent) wrappedObject).pattern();
    }

    @Override
    public @NotNull SelectorComponent withPattern(@NotNull String pattern) {
        return (SelectorComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.SelectorComponent) wrappedObject).pattern(pattern));
    }

    @Override
    public SelectorComponent.@NotNull Builder toBuilder() {
        return new AdventureSelectorBuilder(((net.kyori.adventure.text.SelectorComponent) wrappedObject).toBuilder());
    }

    @Override
    public @Nullable Component separator() {
        try {
            return AdventureBackend.wrapComponent(((net.kyori.adventure.text.SelectorComponent) wrappedObject).separator());
        } catch (Throwable ignored) {
            return null; // added in Adventure 4.8.0
        }
    }

    @Override
    public @NotNull SelectorComponent withSeparator(@Nullable Component separator) {
        try {
            return (SelectorComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.SelectorComponent) wrappedObject).separator(separator == null ? null : separator.as(net.kyori.adventure.text.Component.class)));
        } catch (Throwable ignored) {
            // added in Adventure 4.8.0
        }
        return this;
    }

    public static class AdventureSelectorBuilder extends AdventureBuilder<
            net.kyori.adventure.text.SelectorComponent,
            SelectorComponent.Builder,
            SelectorComponent,
            net.kyori.adventure.text.SelectorComponent.Builder
            > implements SelectorComponent.Builder {

        public AdventureSelectorBuilder(net.kyori.adventure.text.SelectorComponent.Builder builder) {
            super(builder);
        }

        @Override
        public SelectorComponent.@NotNull Builder pattern(@NotNull String pattern) {
            getBuilder().pattern(pattern);
            return self();
        }

        @Override
        public SelectorComponent.@NotNull Builder separator(@Nullable Component separator) {
            try {
                getBuilder().separator(separator == null ? null : separator.as(net.kyori.adventure.text.Component.class));
            } catch (Throwable ignored) {
                // added in Adventure 4.8.0
            }
            return self();
        }
    }
}
