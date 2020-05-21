package org.screamingsandals.lib.commands.common.interfaces;

public interface CompleteTab {

    @FunctionalInterface
    interface PlayerCommandComplete<T> extends Completable<T> {
    }

    @FunctionalInterface
    interface PlayerSubCommandComplete<T> extends Completable<T> {
    }

    @FunctionalInterface
    interface ConsoleCommandComplete<T> extends Completable<T> {
    }

    @FunctionalInterface
    interface ConsoleSubCommandComplete<T> extends Completable<T> {
    }

    @FunctionalInterface
    interface SubCommandComplete<T> extends Completable<T> {
    }
}
