package org.screamingsandals.commands.api.command;

import java.util.Optional;

public interface SubCommandNode extends CommandBase {

    /**
     * Returns parent of this node. Parent is the main node,
     * for example if command is "/foo bar", "foo" is the parent.
     *
     * @return If the node is parent, returns empty optional
     */
    Optional<CommandBase> getParent();
}
