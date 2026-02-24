package org.screamingsandals.lib.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.command.argument.Argument;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.sender.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ToString
public class CommandBuilder<C extends CommandSender> {
    private final @NotNull String name;
    private final @NotNull List<@NotNull Node> arguments;
    private final @NotNull List<@NotNull Permission> permissions;
    private final @NotNull Class<? extends CommandSender> commandSenderClass;
    private final @Nullable CommandExecutor<?> commandExecutor;

    public CommandBuilder(@NotNull String name) {
        this.name = name;
        this.arguments = List.of();
        this.permissions = List.of();
        this.commandSenderClass = CommandSender.class;
        this.commandExecutor = null;
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull CommandBuilder<C> literal(@NotNull String literal) {
        var clone = new ArrayList<>(arguments);
        clone.add(new Node.Literal(literal));
        return new CommandBuilder<>(
                this.name,
                clone,
                this.permissions,
                this.commandSenderClass,
                this.commandExecutor
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull CommandBuilder<C> argument(@NotNull Argument<? super C, ?> argument) {
        var clone = new ArrayList<>(arguments);
        clone.add(argument);
        return new CommandBuilder<>(
                this.name,
                clone,
                this.permissions,
                this.commandSenderClass,
                this.commandExecutor
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull CommandBuilder<C> permission(@NotNull Permission permission) {
        var clone = new ArrayList<>(permissions);
        clone.add(permission);
        return new CommandBuilder<>(
                this.name,
                this.arguments,
                clone,
                this.commandSenderClass,
                this.commandExecutor
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public <N extends C> @NotNull CommandBuilder<N> ensureSenderType(@NotNull Class<N> senderType) {
        return new CommandBuilder<>(
                this.name,
                this.arguments,
                this.permissions,
                senderType,
                this.commandExecutor
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull CommandBuilder<C> execute(@NotNull CommandExecutor<C> executor) {
        if (this.commandExecutor != null) {
            throw new IllegalStateException("CommandExecutor already set for " + this);
        }

        return new CommandBuilder<>(
                this.name,
                this.arguments,
                this.permissions,
                this.commandSenderClass,
                executor
        );
    }

    @ApiStatus.Internal
    public interface Node {
        @Data
        @Accessors(chain = true, fluent = true)
        class Literal implements Node {
            private final @NotNull String value;
        }
    }
}
