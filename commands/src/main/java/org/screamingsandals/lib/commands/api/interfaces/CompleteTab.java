package org.screamingsandals.lib.commands.api.interfaces;

import java.util.List;

public interface CompleteTab {

    interface Player<T> {
        List<String> complete(T player, List<String> args);
    }

    interface Console<T> {
        List<String> complete(T console, List<String> args);
    }
}
