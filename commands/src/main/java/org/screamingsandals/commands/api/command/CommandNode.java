package org.screamingsandals.commands.api.command;

import org.screamingsandals.commands.core.command.SimpleCommandNode;

import java.util.Map;
import java.util.Optional;

public interface CommandNode {

    static CommandNode empty(String name) {
        return new SimpleCommandNode(name);
    }

    static CommandNode copyOf(String name, CommandNode node) {
        return new SimpleCommandNode(name, node);
    }

    /**
     * Name of the node (used as command name or sub command name)
     * @return name value
     */
    String getName();

    String getPermissions();

    String getDescription();

    String getUsage();

    /**
     * Returns parent of the command
     * @return command node, duh
     */
    CommandNode getParent();

    /**
     * Returns owner of this node
     * If the node is main, return empty optional
     * @return command node, duh
     */
    Optional<CommandNode> getOwner();

    /**
     * Adds new sub node to parent, can be  used even for
     * args (sub command for sub command, etc.)
     * @param node
     */
    void addSubNode(CommandNode node);

    /**
     *
     * @param name
     * @return
     */
    Optional<CommandNode> getSubNode(String name);

    Map<String, CommandNode> getSubNodes();

    CommandHandler getHandler();

    void setHandler(CommandHandler handler);
}
