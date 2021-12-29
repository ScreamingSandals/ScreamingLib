package org.screamingsandals.lib.minestom.entity.damage;

import java.util.Objects;

public class MinestomSpecialDamageCauseHolder extends MinestomDamageCauseHolder {
    private final String name;

    protected MinestomSpecialDamageCauseHolder(String name) {
        super(null);
        this.name = name;
    }

    @Override
    public String platformName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinestomDamageCauseHolder)) return false;
        MinestomDamageCauseHolder that = (MinestomDamageCauseHolder) o;
        return name.equals(that.platformName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
