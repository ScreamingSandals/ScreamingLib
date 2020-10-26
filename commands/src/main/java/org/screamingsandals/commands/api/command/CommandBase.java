package org.screamingsandals.commands.api.command;

import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.List;

/**
 * This is the command node. Everything related to commands is node. Remember that.
 * <p>
 * Parent node = Main node. This node can contain sub-nodes.
 * </p>
 * <p>
 * Owner node = Sub-node - sub command registered to parent. This node can
 * contain another sub-nodes, which are arguments for the command
 */
public interface CommandBase {

    /**
     * Name of the node (used as command name or sub command name)
     *
     * @return name value
     */
    String getName();

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
