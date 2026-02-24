package org.screamingsandals.lib.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;

public class FloatArgument<C extends CommandSender> extends Argument<C, Float> {
    public FloatArgument(@NotNull String name, @Nullable Float defaultValue, boolean required) {
        super(name, defaultValue, required);
    }
}
