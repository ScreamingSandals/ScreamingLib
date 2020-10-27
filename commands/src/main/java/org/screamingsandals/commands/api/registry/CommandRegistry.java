package org.screamingsandals.commands.api.registry;

import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.lib.core.util.result.Result;

import java.util.Map;

public interface CommandRegistry {

    Result registerNode(CommandNode node);

    CommandNode getNode(String name);

    /**
     * Only parent node can be removed from this registry
     *
     * @param node parent node
     * @return if operation was successful
     */
    Result removeNode(CommandNode node);

    Map<String, CommandNode> getNodes();

}
