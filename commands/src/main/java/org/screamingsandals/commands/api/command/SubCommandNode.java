package org.screamingsandals.commands.api.command;

public interface SubCommandNode extends CommandBase {

    CommandNode getParent();
}
