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
import org.screamingsandals.lib.bungee.spectator.backports.ScorePortedComponent;
import org.screamingsandals.lib.spectator.ScoreComponent;

public class PortedBungeeScoreComponent extends BungeeComponent implements ScoreComponent {
    protected PortedBungeeScoreComponent(@NotNull ScorePortedComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String name() {
        return ((ScorePortedComponent) wrappedObject).getName();
    }

    @Override
    public @NotNull ScoreComponent withName(@NotNull String name) {
        var duplicate = (ScorePortedComponent) wrappedObject.duplicate();
        duplicate.setName(name);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull String objective() {
        return ((ScorePortedComponent) wrappedObject).getObjective();
    }

    @Override
    public @NotNull ScoreComponent withObjective(@NotNull String objective) {
        var duplicate = (ScorePortedComponent) wrappedObject.duplicate();
        duplicate.setObjective(objective);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable String value() {
        var value = ((ScorePortedComponent) wrappedObject).getValue();
        return value == null || value.isEmpty() ? null : value;
    }

    @Override
    public @NotNull ScoreComponent withValue(@Nullable String value) {
        var duplicate = (ScorePortedComponent) wrappedObject.duplicate();
        duplicate.setValue(value);
        return (ScoreComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public ScoreComponent.@NotNull Builder toBuilder() {
        var duplicate = (ScorePortedComponent) wrappedObject.duplicate();
        return new BungeeScoreBuilder(duplicate);
    }

    public static class BungeeScoreBuilder extends BungeeBuilder<ScoreComponent, ScoreComponent.Builder, ScorePortedComponent> implements ScoreComponent.Builder {

        public BungeeScoreBuilder(@NotNull ScorePortedComponent component) {
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
            component.setValue(value);
            return this;
        }
    }
}
