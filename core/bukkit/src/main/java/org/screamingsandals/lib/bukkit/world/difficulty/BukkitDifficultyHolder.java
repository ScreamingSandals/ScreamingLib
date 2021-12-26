package org.screamingsandals.lib.bukkit.world.difficulty;

import org.bukkit.Difficulty;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;

import java.util.Arrays;

public class BukkitDifficultyHolder extends BasicWrapper<Difficulty> implements DifficultyHolder {
    protected BukkitDifficultyHolder(Difficulty wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof DifficultyHolder || object instanceof Difficulty) {
            return equals(object);
        }
        return equals(DifficultyHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
