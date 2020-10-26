package org.screamingsandals.commands.api.registry;

import org.screamingsandals.commands.api.command.CommandBase;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.lib.core.util.result.Result;

public interface CommandRegistry<T> {

    Result register(CommandNode node);

    CommandBase getByName(String name);

    /**
     * Only parent node can be removed from this registry
     * @param node parent node
     * @return if operation was successful
     */
    Result remove(CommandNode node);

    WrappedCommand<T> getWrappedCommand(CommandNode node);

}
