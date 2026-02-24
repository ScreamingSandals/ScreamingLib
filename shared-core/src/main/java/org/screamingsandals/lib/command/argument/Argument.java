package org.screamingsandals.lib.command.argument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.command.CommandBuilder;
import org.screamingsandals.lib.command.Context;
import org.screamingsandals.lib.command.SuggestionProvider;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.List;
import java.util.Queue;

@Data
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
public abstract class Argument<C extends CommandSender, T> implements CommandBuilder.Node {
    private final @NotNull String name;
    private final @NotNull Parser<C, T> parser;
    private final @Nullable SuggestionProvider<C> suggestionProvider;
    private final boolean required;
    private final @Nullable T defaultValue;

    public interface Parser<C extends CommandSender, T> extends SuggestionProvider<C> {
        @NotNull T parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue);

        @Override
        default @Nullable List<@NotNull String> suggest(@NotNull Context<C> context, @NotNull String input) {
            return null;
        }
    }
}
