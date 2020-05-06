package org.screamingsandals.lib.commands.common.wrapper;

public interface CommandWrapper<T, K> {
    T getCommandBase();

    K getCommandInstance();

    void reload();
}
