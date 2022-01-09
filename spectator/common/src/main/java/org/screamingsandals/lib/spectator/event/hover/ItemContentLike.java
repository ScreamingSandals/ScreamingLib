package org.screamingsandals.lib.spectator.event.hover;

public interface ItemContentLike extends ContentLike {
    ItemContent asItemContent();

    @Override
    default Content asContent() {
        return asItemContent();
    }
}
