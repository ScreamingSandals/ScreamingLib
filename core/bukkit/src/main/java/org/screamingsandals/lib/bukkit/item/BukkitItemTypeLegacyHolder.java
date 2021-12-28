package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;

import java.util.Arrays;
import java.util.Optional;

public class BukkitItemTypeLegacyHolder extends BasicWrapper<Pair<Material, Short>> implements ItemTypeHolder {

    public BukkitItemTypeLegacyHolder(Material material) {
        this(Pair.of(material, (short) 0));
    }

    public BukkitItemTypeLegacyHolder(Material material, short forcedDurability) {
        this(Pair.of(material, forcedDurability));
    }

    public BukkitItemTypeLegacyHolder(Pair<Material, Short> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.first().name();
    }

    @Override
    public short forcedDurability() {
        return wrappedObject.second();
    }

    @Override
    public int maxStackSize() {
        return wrappedObject.first().getMaxStackSize();
    }

    @Override
    public ItemTypeHolder withForcedDurability(short durability) {
        return new BukkitItemTypeLegacyHolder(Pair.of(wrappedObject.first(), durability));
    }

    @Override
    public Optional<BlockTypeHolder> block() {
        if (!wrappedObject.first().isBlock()) {
            return Optional.empty();
        }
        return Optional.of(BlockTypeHolder.of(wrappedObject.first()).withLegacyData(wrappedObject.second().byteValue()));
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Material && wrappedObject.second() == 0) {
            return wrappedObject.first().equals(object);
        }
        if (object instanceof ItemTypeHolder) {
            return equals(object);
        }
        return equals(ItemTypeHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == Material.class) { // the wrapped type is Pair, so we will help the BasicWrapper a little with unwrapping Material
            return (T) wrappedObject.first();
        } else if (type == ItemStack.class) {
            return (T) new ItemStack(wrappedObject.first(), 1, wrappedObject.second());
        }
        return super.as(type);
    }
}