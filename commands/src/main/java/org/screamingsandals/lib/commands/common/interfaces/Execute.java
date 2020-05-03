package org.screamingsandals.lib.commands.common.interfaces;

import java.util.List;

public interface Execute {

    @FunctionalInterface
    interface PlayerCommand<T> {
        void execute(T player, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleCommand<T> {
        void execute(T console, List<String> args);
    }

    @FunctionalInterface
    interface PlayerSubCommand<T> {
        void execute(T player, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleSubCommand<T> {
        void execute(T console, List<String> args);
    }
}
