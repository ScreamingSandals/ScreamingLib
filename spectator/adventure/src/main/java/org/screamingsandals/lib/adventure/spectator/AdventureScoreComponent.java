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

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.ScoreComponent;

public class AdventureScoreComponent extends AdventureComponent implements ScoreComponent {
    public AdventureScoreComponent(net.kyori.adventure.text.ScoreComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String name() {
        return ((net.kyori.adventure.text.ScoreComponent) wrappedObject).name();
    }

    @Override
    public String objective() {
        return ((net.kyori.adventure.text.ScoreComponent) wrappedObject).objective();
    }

    @Override
    public String value() {
        return ((net.kyori.adventure.text.ScoreComponent) wrappedObject).value();
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
        public ScoreComponent.Builder name(String name) {
            getBuilder().name(name);
            return self();
        }

        @Override
        public ScoreComponent.Builder objective(String objective) {
            getBuilder().objective(objective);
            return self();
        }

        @Override
        public ScoreComponent.Builder value(@Nullable String value) {
            getBuilder().value(value);
            return self();
        }
    }
}
