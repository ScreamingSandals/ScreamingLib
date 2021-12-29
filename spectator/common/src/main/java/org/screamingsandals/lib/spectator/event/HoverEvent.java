package org.screamingsandals.lib.spectator.event;

import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.utils.Wrapper;

public interface HoverEvent extends Wrapper {

    Action action();

    Content content();

    enum Action {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY
        /* SHOW_ACHIEVEMENT no longer exist */
    }
}
