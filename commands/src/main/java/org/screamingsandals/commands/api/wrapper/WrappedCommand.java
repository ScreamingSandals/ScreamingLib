package org.screamingsandals.commands.api.wrapper;

import org.screamingsandals.commands.api.command.CommandNode;

public interface WrappedCommand<T> {

    T getCommand();

    CommandNode getNode();
}
