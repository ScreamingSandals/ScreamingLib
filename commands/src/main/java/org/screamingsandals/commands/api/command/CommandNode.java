package org.screamingsandals.commands.api.command;

import org.screamingsandals.commands.core.command.SimpleCommandNode;

import java.util.Map;
import java.util.Optional;

public interface CommandNode extends CommandBase {

    /**
     * Creates new empty node. Name is required.
     *
     * @param name name of the node
     * @return new empty node
     */
    static CommandNode empty(String name) {
        return SimpleCommandNode.empty(name);
    }

    /**
     * Adds new sub-node to this node.
     * If the node is parent, the new node is added as sub-node.
     * <p>
     * If the node is owner, the new node is added as args-node.
     *
     * @param node the new node
     */
    void addSubNode(SubCommandNode node);

    /**
     * Returns sub-node by name.
     *
     * @param name node name
     * @return node
     */
    Optional<SubCommandNode> getSubNode(String name);

    /**
     * Removes the sub-node from this node
     * @param name name of the node
     */
    void removeSubNode(String name);

    /**
     * @return all registered sub-nodes
     */
    Map<String, SubCommandNode> getSubNodes();
}
