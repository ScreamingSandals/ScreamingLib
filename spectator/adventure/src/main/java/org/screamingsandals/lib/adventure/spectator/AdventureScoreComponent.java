package org.screamingsandals.lib.adventure.spectator;

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
}
