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

package org.screamingsandals.lib.adventure.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.ScoreComponent;

public class AdventureScoreComponent extends AdventureComponent implements ScoreComponent {
    public AdventureScoreComponent(net.kyori.adventure.text.ScoreComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public String name() {
        return ((net.kyori.adventure.text.ScoreComponent) wrappedObject).name();
    }

    @Override
    @NotNull
    public ScoreComponent withName(@NotNull String name) {
        return (ScoreComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.ScoreComponent) wrappedObject).name(name));
    }

    @Override
    @NotNull
    public String objective() {
        return ((net.kyori.adventure.text.ScoreComponent) wrappedObject).objective();
    }

    @Override
    @NotNull
    public ScoreComponent withObjective(@NotNull String objective) {
        return (ScoreComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.ScoreComponent) wrappedObject).objective(objective));
    }

    @Override
    public String value() {
        return ((net.kyori.adventure.text.ScoreComponent) wrappedObject).value();
    }

    @Override
    @NotNull
    public ScoreComponent withValue(@Nullable String value) {
        return (ScoreComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.ScoreComponent) wrappedObject).value(value));
    }

    @Override
    @NotNull
    public ScoreComponent.Builder toBuilder() {
        return new AdventureScoreBuilder(((net.kyori.adventure.text.ScoreComponent) wrappedObject).toBuilder());
    }

    public static class AdventureScoreBuilder extends AdventureBuilder<
            net.kyori.adventure.text.ScoreComponent,
            ScoreComponent.Builder,
            ScoreComponent,
            net.kyori.adventure.text.ScoreComponent.Builder
            > implements ScoreComponent.Builder {

        public AdventureScoreBuilder(net.kyori.adventure.text.ScoreComponent.Builder builder) {
            super(builder);
        }

        @Override
        @NotNull
        public ScoreComponent.Builder name(@NotNull String name) {
            getBuilder().name(name);
            return self();
        }

        @Override
        @NotNull
        public ScoreComponent.Builder objective(@NotNull String objective) {
            getBuilder().objective(objective);
            return self();
        }

        @Override
        @NotNull
        public ScoreComponent.Builder value(@Nullable String value) {
            getBuilder().value(value);
            return self();
        }
    }
}
