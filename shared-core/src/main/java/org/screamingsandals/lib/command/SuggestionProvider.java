package org.screamingsandals.lib.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.List;

@FunctionalInterface
public interface SuggestionProvider<C extends CommandSender> {
    @Nullable List<@NotNull String> suggest(@NotNull Context<C> context, @NotNull String input);
}
