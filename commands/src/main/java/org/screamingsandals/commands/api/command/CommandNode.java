package org.screamingsandals.commands.api.command;

import org.screamingsandals.commands.core.command.SimpleCommandNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommandNode {

    static CommandNode empty(String name) {
        return SimpleCommandNode.empty(name);
    }

    static CommandNode copyOf(String name, CommandNode node) {
        return SimpleCommandNode.copy(name, node);
    }

    /**
     * Name of the node (used as command name or sub command name)
     * @return name value
     */
    String getName();

    /**
     * Permissions needed to execute the command
     * Does not apply to console
     * @return permissions for this node
     */
    String getPermissions();

    /**
     *
     * @return
     */
    String getDescription();

    /**
     *
     * @return
     */
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

    List<CommandCallback> getCallbacks();

    void addCallback(CommandCallback callback);
}
