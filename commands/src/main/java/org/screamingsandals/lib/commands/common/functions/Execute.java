package org.screamingsandals.lib.commands.common.functions;

import java.util.List;

public interface Execute {

    @FunctionalInterface
    interface PlayerCommand<T> extends CommandValue {
        void execute(T player, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleCommand<T> extends CommandValue {
        void execute(T console, List<String> args);
    }

    @FunctionalInterface
    interface PlayerSubCommand<T> extends CommandValue {
        void execute(T player, List<String> args);
    }

    @FunctionalInterface
    interface ConsoleSubCommand<T> extends CommandValue {
        void execute(T console, List<String> args);
    }
}
