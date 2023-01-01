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
import org.screamingsandals.lib.spectator.ScoreComponent;

public class BungeeScoreComponent extends BungeeComponent implements ScoreComponent {
    protected BungeeScoreComponent(net.md_5.bungee.api.chat.@NotNull ScoreComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public String name() {
        return ((net.md_5.bungee.api.chat.ScoreComponent) wrappedObject).getName();
    }

    @Override
    @NotNull
    public ScoreComponent withName(@NotNull String name) {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        duplicate.setName(name);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    @NotNull
    public String objective() {
        return ((net.md_5.bungee.api.chat.ScoreComponent) wrappedObject).getObjective();
    }

    @Override
    @NotNull
    public ScoreComponent withObjective(@NotNull String objective) {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        duplicate.setObjective(objective);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    @Nullable
    public String value() {
        var value = ((net.md_5.bungee.api.chat.ScoreComponent) wrappedObject).getValue();
        return value.isEmpty() ? null : value;
    }

    @Override
    @NotNull
    public ScoreComponent withValue(@Nullable String value) {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        duplicate.setValue(value == null ? "" : value);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    @NotNull
    public ScoreComponent.Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.ScoreComponent) wrappedObject.duplicate();
        return new BungeeScoreBuilder(duplicate);
    }

    public static class BungeeScoreBuilder extends BungeeBuilder<ScoreComponent, ScoreComponent.Builder, net.md_5.bungee.api.chat.ScoreComponent> implements ScoreComponent.Builder {

        public BungeeScoreBuilder(net.md_5.bungee.api.chat.ScoreComponent component) {
            super(component);
        }

        @Override
        @NotNull
        public ScoreComponent.Builder name(@NotNull String name) {
            component.setValue(name);
            return this;
        }

        @Override
        @NotNull
        public ScoreComponent.Builder objective(@NotNull String objective) {
            component.setValue(objective);
            return this;
        }

        @Override
        @NotNull
        public ScoreComponent.Builder value(@Nullable String value) {
            component.setValue(value);
            return this;
        }
    }
}
