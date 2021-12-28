package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.enchantments.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;

@ConfigSerializable
public class BukkitEnchantmentHolder extends BasicWrapper<Pair<Enchantment, Integer>> implements EnchantmentHolder {

    public BukkitEnchantmentHolder(Enchantment enchantment) {
        this(Pair.of(enchantment, 1));
    }

    public BukkitEnchantmentHolder(Pair<Enchantment, Integer> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.first().getName();
    }

    @Override
    public int level() {
        return wrappedObject.second();
    }

    @Override
    public EnchantmentHolder withLevel(int level) {
        return new BukkitEnchantmentHolder(Pair.of(wrappedObject.first(), level));
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Enchantment) {
            return wrappedObject.equals(Pair.of(object, 1));
        }
        if (object instanceof EnchantmentHolder) {
            return equals(object);
        }
        return equals(EnchantmentHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public boolean isSameType(Object object) {
        if (object instanceof Enchantment) {
            return wrappedObject.first().equals(object);
        } else if (object instanceof BukkitEnchantmentHolder) {
            return ((BukkitEnchantmentHolder) object).wrappedObject.first().equals(wrappedObject.first());
        }
        return platformName().equals(EnchantmentHolder.ofOptional(object).map(EnchantmentHolder::platformName).orElse(null));
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }
}
