package org.screamingsandals.lib.bukkit.world.gamerule;

import org.bukkit.GameRule;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;

import java.util.Arrays;

public class BukkitGameRuleHolder extends BasicWrapper<GameRule<?>> implements GameRuleHolder {

    protected BukkitGameRuleHolder(GameRule<?> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof GameRule || object instanceof GameRuleHolder) {
            return equals(object);
        }
        return equals(GameRuleHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
