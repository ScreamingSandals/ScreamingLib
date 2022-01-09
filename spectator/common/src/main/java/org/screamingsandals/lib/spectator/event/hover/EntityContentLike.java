package org.screamingsandals.lib.spectator.event.hover;

public interface EntityContentLike extends ContentLike {
    EntityContent asEntityContent();

    @Override
    default Content asContent() {
        return asEntityContent();
    }
}
