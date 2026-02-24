package org.screamingsandals.lib.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;

public class IntegerArgument<C extends CommandSender> extends Argument<C, Integer> {
    public IntegerArgument(@NotNull String name, @Nullable Integer defaultValue, boolean required) {
        super(name, defaultValue, required);
    }
}
