package org.screamingsandals.lib.bungee.spectator.event.hover;

import net.md_5.bungee.api.chat.hover.content.Item;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class BungeeItemContent extends BasicWrapper<Item> implements ItemContent {
    public BungeeItemContent(Item wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public NamespacedMappingKey id() {
        var id = wrappedObject.getId();
        if (id == null) {
            return NamespacedMappingKey.of("minecraft:air"); // md_5's nice api said: will be air if null
        }
        return NamespacedMappingKey.of(id);
    }

    @Override
    public int count() {
        return wrappedObject.getCount();
    }

    @Override
    public @Nullable String tag() {
        var tag = wrappedObject.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getNbt();
    }
}
