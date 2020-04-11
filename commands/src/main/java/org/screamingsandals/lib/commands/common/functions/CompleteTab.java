package org.screamingsandals.lib.commands.common.functions;

import java.util.List;


public interface CompleteTab {

    @FunctionalInterface
    interface PlayerCommandComplete<T> extends CommandValue {
        List<String> complete(T player, List<String> args);
    }

    @FunctionalInterface
    interface PlayerSubCommandComplete<T> extends CommandValue {
        List<String> complete(T player, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleCommandComplete<T> extends CommandValue {
        List<String> complete(T console, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleSubCommandComplete<T> extends CommandValue {
        List<String> complete(T console, List<String> args);
    }

    @FunctionalInterface
    interface SubCommandComplete<T> extends CommandValue {
        List<String> complete(T commandSender, List<String> args);
    }
}
