package org.screamingsandals.lib.adventure.spectator.event.hover;

import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureItemContent extends BasicWrapper<HoverEvent.ShowItem> implements ItemContent {
    public AdventureItemContent(HoverEvent.ShowItem wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public NamespacedMappingKey id() {
        return NamespacedMappingKey.of(wrappedObject.item().asString());
    }

    @Override
    public int count() {
        return wrappedObject.count();
    }

    @Override
    @Nullable
    public String tag() {
        var nbt = wrappedObject.nbt();
        if (nbt == null) {
            return null;
        }
        return nbt.string();
    }
}
