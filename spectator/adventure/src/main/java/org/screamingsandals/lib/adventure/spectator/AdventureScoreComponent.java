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
