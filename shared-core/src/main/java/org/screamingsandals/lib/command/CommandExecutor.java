package org.screamingsandals.lib.command;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.CommandSender;

@FunctionalInterface
public interface CommandExecutor<C extends CommandSender> {
    void execute(@NotNull Context<C> context);
}
