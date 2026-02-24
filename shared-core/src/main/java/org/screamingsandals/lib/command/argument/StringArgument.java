package org.screamingsandals.lib.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;

public class StringArgument<C extends CommandSender> extends Argument<C, String> {
    public StringArgument(@NotNull String name, @Nullable String defaultValue, boolean required) {
        super(name, defaultValue, required);
    }
}
