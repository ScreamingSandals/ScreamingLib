package org.screamingsandals.lib.gamecore.entity;

import lombok.Getter;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.elements.GameElement;
import org.screamingsandals.lib.gamecore.elements.GameElementValue;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

@Getter
public abstract class ClickableEntity<G extends Game<G, P, ?>, P extends GamePlayer<P, G>> extends GameElement {
    private final GameElementValue<LocationHolder> position = new GameElementValue<>();
    private final GameElementValue<EntityTypeHolder> entityType = new GameElementValue<>(EntityTypeMapping.resolve("VILLAGER").orElseThrow());
    private final GameElementValue<Boolean> baby = new GameElementValue<>(false);
    private final GameElementValue<String> entityName = new GameElementValue<>();

    public ClickableEntity(UUID uuid) {
        super(uuid);
    }

    public abstract void performClick(P player);
}
