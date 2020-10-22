package org.screamingsandals.commands.api.registry;

import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.lib.core.util.result.Result;

public interface CommandRegistry<T> {

    Result register(CommandNode node);

    CommandNode getByName(String name);

    CommandNode getBySubNode(CommandNode node);

    /**
     * Only parent node can be removed from this registry
     * @param node parent node
     * @return if operation was successful
     */
    Result remove(CommandNode node);
}
