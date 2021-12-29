package org.screamingsandals.lib.spectator.event;

import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface ClickEvent extends Wrapper {

    Action action();

    String value();

    enum Action {
        OPEN_URL,
        OPEN_FILE,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        /**
         * Books only
         */
        CHANGE_PAGE,
        @LimitedVersionSupport(">= 1.15")
        COPY_TO_CLIPBOARD
    }
}
