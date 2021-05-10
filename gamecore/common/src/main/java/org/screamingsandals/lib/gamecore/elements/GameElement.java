package org.screamingsandals.lib.gamecore.elements;

import lombok.Data;
import org.screamingsandals.lib.utils.Nameable;
import org.screamingsandals.lib.utils.UniqueIdentifiable;

import java.util.Optional;
import java.util.UUID;

@Data
public abstract class GameElement implements UniqueIdentifiable, Nameable {
    protected final UUID uuid;
    protected String name;

    @Override
    public Optional<String> getName() {
        return Optional.empty();
    }
}
