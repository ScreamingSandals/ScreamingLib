package org.screamingsandals.lib.commands.common.interfaces;

import java.util.List;


public interface CompleteTab {

    @FunctionalInterface
    interface PlayerCommandComplete<T> {
        List<String> complete(T player, List<String> args);
    }

    @FunctionalInterface
    interface PlayerSubCommandComplete<T> {
        List<String> complete(T player, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleCommandComplete<T> {
        List<String> complete(T console, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleSubCommandComplete<T> {
        List<String> complete(T console, List<String> args);
    }

    @FunctionalInterface
    interface SubCommandComplete<T> {
        List<String> complete(T commandSender, List<String> args);
    }
}
