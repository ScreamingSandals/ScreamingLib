package org.screamingsandals.lib.commands.common.interfaces;

public interface Execute {

    @FunctionalInterface
    interface PlayerCommand<T> extends Executable<T> {
    }

    @FunctionalInterface
    interface ConsoleCommand<T> extends Executable<T> {
    }

    @FunctionalInterface
    interface PlayerSubCommand<T> extends Executable<T> {
    }

    @FunctionalInterface
    interface ConsoleSubCommand<T> extends Executable<T> {
    }
}
