package org.screamingsandals.lib.commands.api.interfaces;

import java.util.List;

public interface Execute {
    interface Player<T> {
        void execute(T player, List<String> args);
    }

    interface Console<T> {
        void execute(T console, List<String> args);
    }
}
