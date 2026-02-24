package org.screamingsandals.lib.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;

public class DoubleArgument<C extends CommandSender> extends Argument<C, Double> {
    public DoubleArgument(@NotNull String name, @Nullable Double defaultValue, boolean required) {
        super(name, defaultValue, required);
    }
}
