package org.screamingsandals.lib.item.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;

import java.util.List;
import java.util.Optional;

public interface ItemBuilder {
    ItemBuilder type(@NotNull ItemTypeHolder type);

    ItemBuilder amount(int amount);

    ItemBuilder displayName(@Nullable Component displayName);

    ItemBuilder lore(@Nullable List<@NotNull Component> lore);

    Optional<Item> build();

}
