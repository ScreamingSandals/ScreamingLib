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

package org.screamingsandals.lib.impl.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.ScoreComponent;

public class BungeeScoreComponent extends BungeeComponent implements ScoreComponent {
    protected BungeeScoreComponent(net.md_5.bungee.api.chat.@NotNull ScoreComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String name() {
        return ((net.md_5.bungee.api.chat.ScoreComponent) wrappedObject).getName();
    }

    @Override
    public @NotNull ScoreComponent withName(@NotNull String name) {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        duplicate.setName(name);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull String objective() {
        return ((net.md_5.bungee.api.chat.ScoreComponent) wrappedObject).getObjective();
    }

    @Override
    public @NotNull ScoreComponent withObjective(@NotNull String objective) {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        duplicate.setObjective(objective);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable String value() {
        var value = ((net.md_5.bungee.api.chat.ScoreComponent) wrappedObject).getValue();
        return value.isEmpty() ? null : value;
    }

    @Override
    public @NotNull ScoreComponent withValue(@Nullable String value) {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        duplicate.setValue(value == null ? "" : value);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public ScoreComponent.@NotNull Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        return new BungeeScoreBuilder(duplicate);
    }

    public static class BungeeScoreBuilder extends BungeeBuilder<ScoreComponent, ScoreComponent.Builder, net.md_5.bungee.api.chat.ScoreComponent> implements ScoreComponent.Builder {

        public BungeeScoreBuilder(net.md_5.bungee.api.chat.@NotNull ScoreComponent component) {
            super(component);
        }

        @Override
        public ScoreComponent.@NotNull Builder name(@NotNull String name) {
            component.setValue(name);
            return this;
        }

        @Override
        public ScoreComponent.@NotNull Builder objective(@NotNull String objective) {
            component.setValue(objective);
            return this;
        }

        @Override
        public ScoreComponent.@NotNull Builder value(@Nullable String value) {
            component.setValue(value == null ? "" : value);
            return this;
        }
    }
}
