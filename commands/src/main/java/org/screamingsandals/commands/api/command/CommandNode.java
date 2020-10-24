package org.screamingsandals.commands.api.command;

import org.screamingsandals.commands.api.tab.TabCallback;
import org.screamingsandals.commands.core.command.SimpleCommandNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This is the command node. Everything related to commands is node. Remember that.
 * <p>
 * Parent node = Main node. This node can contain sub-nodes.
 * </p>
 * <p>
 * Owner node = Sub-node - sub command registered to parent. This node can
 * contain another sub-nodes, which are arguments for the command
 */
public interface CommandNode {

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
     * Name of the node (used as command name or sub command name)
     *
     * @return name value
     */
    String getName();

    /**
     * @return name of the parent node
     */
    default Optional<String> getParentName() {
        return getParent()
                .map(CommandNode::getName)
                .or(Optional::empty);
    }

    /**
     * @return name of the owner node
     */
    default Optional<String> getOwnerName() {
        return getOwner()
                .map(CommandNode::getName)
                .or(Optional::empty);
    }

    /**
     * Permissions needed to execute the command
     * Does not apply to console
     *
     * @return permissions for this node
     */
    String getPermission();

    /**
     * What this command does
     *
     * @return description of hte command
     */
    String getDescription();

    /**
     * How this command should be used
     * Send to player if execution is wrong
     * Shown in /help command
     *
     * @return usage of the command
     */
    String getUsage();

    /**
     * Aliases for the command
     *
     * @return aliases
     */
    List<String> getAliases();

    /**
     * Returns parent of this node. Parent is the main node,
     * for example if command is "/foo bar", "foo" is the parent.
     *
     * @return If the node is parent, returns empty optional
     */
    Optional<CommandNode> getParent();

    /**
     * Returns owner of this node. Owner is the sub-node,
     * for example if command is "/foo bar baz", "bar" is the owner.
     *
     * @return If the node is main, returns empty optional
     */
    Optional<CommandNode> getOwner();

    /**
     * Checks if this node has parent
     *
     * @return true if parent is present
     */
    default boolean hasParent() {
        return getParent().isPresent();
    }

    /**
     * Check if this node has owner
     *
     * @return true if owner is present
     */
    default boolean hasOwner() {
        return getOwner().isPresent();
    }

    /**
     * Adds new sub-node to this node.
     * If the node is parent, the new node is added as sub-node.
     * <p>
     * If the node is owner, the new node is added as args-node.
     *
     * @param node the new node
     */
    void addSubNode(CommandNode node);

    /**
     * Returns sub-node by name.
     * This includes sub-commands only.
     *
     * @param name node name
     * @return node
     */
    Optional<CommandNode> getSubNode(String name);

    /**
     * This counts only sub-commands
     *
     * @return all registered sub-nodes
     */
    Map<String, CommandNode> getSubNodes();

    /**
     * @return all registered callbacks
     */
    List<CommandCallback> getCallbacks();

    /**
     * Registers new callback
     * The {@link org.screamingsandals.commands.api.command.CommandCallback.Priority is set to NORMAL by default}
     *
     * @param callback new callback
     */
    void addCallback(CommandCallback callback);

    /**
     * Registers new callback
     *
     * @param priority priority of the callback
     * @param callback new callback
     */
    void addCallback(CommandCallback.Priority priority, CommandCallback callback);

    /**
     * @return registered tab callback
     */
    TabCallback getTabCallback();

    /**
     * Registers new tab callback.
     * There can be only one callback at all.
     *
     * @param tabCallback new callback
     */
    void setTabCallback(TabCallback tabCallback);
}
