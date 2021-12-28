package org.screamingsandals.lib.bukkit.world.gamerule;

import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;

import java.util.Arrays;

public class BukkitLegacyGameRuleHolder extends BasicWrapper<String> implements GameRuleHolder {
    protected BukkitLegacyGameRuleHolder(String wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject;
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof GameRuleHolder) {
            return equals(object);
        }
        return equals(GameRuleHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
