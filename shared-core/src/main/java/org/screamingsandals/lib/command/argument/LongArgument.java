package org.screamingsandals.lib.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;

public class LongArgument<C extends CommandSender> extends Argument<C, Long> {
    public LongArgument(@NotNull String name, @Nullable Long defaultValue, boolean required) {
        super(name, defaultValue, required);
    }
}
