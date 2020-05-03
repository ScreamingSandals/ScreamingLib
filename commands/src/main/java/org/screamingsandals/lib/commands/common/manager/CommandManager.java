package org.screamingsandals.lib.commands.common.manager;

import org.screamingsandals.lib.commands.common.wrapper.CommandWrapper;

public interface CommandManager {

    void destroy();

    void registerCommand(CommandWrapper<?, ?> commandWrapper);

    boolean isCommandRegistered(String commandName);

    void unregisterCommand(String commandName);
}
