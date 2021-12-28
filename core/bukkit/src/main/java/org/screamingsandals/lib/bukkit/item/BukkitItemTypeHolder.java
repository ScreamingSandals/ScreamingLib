package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.Optional;

public class BukkitItemTypeHolder extends BasicWrapper<Material> implements ItemTypeHolder {

    // Because people can be stupid + it's also used in our current code for deserializing items ;)
    private short forcedDurability;

    public BukkitItemTypeHolder(Material wrappedObject) {
        super(wrappedObject);
        if (!wrappedObject.isItem()) {
            throw new UnsupportedOperationException("BukkitItemTypeHolder can wrap only item types!!!");
        }
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public short forcedDurability() {
        return forcedDurability;
    }

    @Override
    public int maxStackSize() {
        return wrappedObject.getMaxStackSize();
    }

    @Override
    public ItemTypeHolder withForcedDurability(short durability) {
        var holder = new BukkitItemTypeHolder(wrappedObject);
        holder.forcedDurability = durability;
        return holder;
    }

    @Override
    public Optional<BlockTypeHolder> block() {
        if (!wrappedObject.isBlock()) {
            return Optional.empty();
        }
        return Optional.of(BlockTypeHolder.of(wrappedObject));
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Material || object instanceof ItemTypeHolder) {
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
        if (type == ItemStack.class) {
            ItemStack stack = new ItemStack(wrappedObject);
            if (forcedDurability != 0) {
                // it's somehow still supported because people can be stupid sometimes
                ItemMeta meta = stack.getItemMeta();
                if (meta instanceof Damageable) {
                    ((Damageable) meta).setDamage(forcedDurability);
                    stack.setItemMeta(meta);
                }
            }
            return (T) stack;
        }
        return super.as(type);
    }
}
