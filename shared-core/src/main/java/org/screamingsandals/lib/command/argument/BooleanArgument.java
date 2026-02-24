package org.screamingsandals.lib.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.command.Context;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.List;
import java.util.Queue;

public final class BooleanArgument<C extends CommandSender> extends Argument<C, Boolean> {
    public BooleanArgument(@NotNull String name, boolean required, @Nullable Boolean defaultValue) {
        super(name, new Parser<>(), null, required, defaultValue);
    }

    public static final class Parser<C extends CommandSender> implements Argument.Parser<C, Boolean> {
        private static final @NotNull List<String> SUGGESTION = List.of("true", "false");

        @Override
        public @NotNull Boolean parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue) {
            var input = inputQueue.peek();
            if (input == null) {
                // TODO: FAIL
                return null;
            }

            if ("true".equalsIgnoreCase(input) || "yes".equalsIgnoreCase(input) || "on".equalsIgnoreCase(input)) {
                inputQueue.remove();
                return true;
            }
            if ("false".equalsIgnoreCase(input) || "no".equalsIgnoreCase(input) || "off".equalsIgnoreCase(input)) {
                inputQueue.remove();
                return false;
            }

            // TODO: FAIL
            return null;
        }

        @Override
        public @Nullable List<@NotNull String> suggest(@NotNull Context<C> context, @NotNull String input) {
            return SUGGESTION;
        }
    }
}
