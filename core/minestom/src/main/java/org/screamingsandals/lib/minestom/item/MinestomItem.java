package org.screamingsandals.lib.minestom.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MinestomItem extends BasicWrapper<ItemStack> implements Item {
    public MinestomItem(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ItemTypeHolder getType() {
        return ItemTypeHolder.of(wrappedObject.getMaterial());
    }

    @Override
    public int getAmount() {
        return wrappedObject.getAmount();
    }

    @Override
    public @Nullable Component getDisplayName() {
        return wrappedObject.getDisplayName();
    }

    @Override
    public List<Component> getLore() {
        return wrappedObject.getLore();
    }

    @Override
    public List<ItemAttributeHolder> getAttributeModifiers() {
        return null; // TODO
    }

    @Override
    public List<EnchantmentHolder> getEnchantments() {
        return null;
    }

    @Override
    public ItemData getData() {
        return null;
    }

    @Override
    public List<HideFlags> getHideFlags() {
        return null;
    }

    @Override
    public Integer getCustomModelData() {
        return null;
    }

    @Override
    public boolean isUnbreakable() {
        return false;
    }

    @Override
    public int getRepairCost() {
        return 0;
    }

    @Override
    public ItemBuilder builder() {
        return null;
    }

    @Override
    public boolean isSimilar(Item item) {
        return false;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Item clone() {
        //noinspection UnstableApiUsage
        return new MinestomItem(ItemStack.fromItemNBT(wrappedObject.toItemNBT()));
    }

    @Override
    public boolean supportsMetadata(MetadataKey<?> key) {
        return false;
    }

    @Override
    public boolean supportsMetadata(MetadataCollectionKey<?> key) {
        return false;
    }

    @Override
    public <T> @Nullable T getMetadata(MetadataKey<T> key) {
        return null;
    }

    @Override
    public <T> Optional<T> getMetadataOptional(MetadataKey<T> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable <T> Collection<T> getMetadata(MetadataCollectionKey<T> key) {
        return null;
    }
}
