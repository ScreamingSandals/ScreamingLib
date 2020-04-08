package org.screamingsandals.lib.commands.api;

import org.screamingsandals.lib.commands.common.CommandFrame;

public interface CommandManager {

    void registerCommand(CommandFrame commandFrame);

    boolean isCommandRegistered(String commandName);

    void unregisterCommand(String commandName);
}
