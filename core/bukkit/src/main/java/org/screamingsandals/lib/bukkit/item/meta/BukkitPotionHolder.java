package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitPotionHolder extends BasicWrapper<PotionData> implements PotionHolder {

    public BukkitPotionHolder(PotionType type) {
        this(new PotionData(type));
    }

    public BukkitPotionHolder(PotionData wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        if (wrappedObject.isExtended()) {
            return "LONG_" + wrappedObject.getType().name();
        } else if (wrappedObject.isUpgraded()) {
            return "STRONG_" + wrappedObject.getType().name();
        }
        return wrappedObject.getType().name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof PotionData || object instanceof PotionHolder) {
            return equals(object);
        }
        return equals(PotionHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == PotionType.class) {
            return (T) wrappedObject.getType();
        }
        return super.as(type);
    }
}
