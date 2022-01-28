package org.screamingsandals.lib.minestom.item;

import net.minestom.server.item.Material;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.Optional;

public class MinestomItemTypeHolder extends BasicWrapper<Material> implements ItemTypeHolder {
    private short forcedDurability;

    protected MinestomItemTypeHolder(Material wrappedObject) {
        super(wrappedObject);
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
        return wrappedObject.maxStackSize();
    }

    @Override
    public ItemTypeHolder withForcedDurability(short durability) {
        final var holder = new MinestomItemTypeHolder(wrappedObject);
        holder.forcedDurability = durability;
        return holder;
    }

    @Override
    public Optional<BlockTypeHolder> block() {
        if (!wrappedObject.isBlock()) {
            return Optional.empty();
        }
        // TODO: add once MinestomBlockTypeHolder exists
        return Optional.empty();
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
}
