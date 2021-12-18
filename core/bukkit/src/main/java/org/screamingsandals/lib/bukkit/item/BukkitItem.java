package org.screamingsandals.lib.bukkit.item;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.item.builder.BukkitItemBuilder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BukkitItem extends BasicWrapper<ItemStack> implements Item {
    public BukkitItem(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ItemTypeHolder getType() {
        return ItemTypeHolder.of(wrappedObject.getType());
    }

    @Override
    public int getAmount() {
        return wrappedObject.getAmount();
    }

    @Override
    @Nullable
    public Component getDisplayName() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return ComponentObjectLink.processGetter(meta, "displayName", meta::getDisplayName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Component> getLore() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null && meta.hasLore()) {
            var list = new ArrayList<Component>();
            AdventureUtils
                    .get(meta, "lore")
                    .ifPresentOrElse(classMethod ->
                                    classMethod.invokeInstanceResulted(meta)
                                            .as(List.class)
                                            .stream()
                                            .map(ComponentUtils::componentFromPlatform)
                                            .forEach(o -> list.add((Component) o)),
                            () -> Objects.requireNonNull(meta.getLore())
                                    .stream()
                                    .map(AdventureHelper::toComponent)
                                    .forEach(list::add)
                    );
            return List.copyOf(list); // immutable
        }
        return List.of();
    }

    @Override
    public ItemBuilder builder() {
        return new BukkitItemBuilder(wrappedObject.clone());
    }

    @Override
    public boolean isSimilar(Item item) {
        return wrappedObject.isSimilar(item.as(ItemStack.class));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Item clone() {
        return new BukkitItem(wrappedObject.clone());
    }
}
