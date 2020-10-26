package org.screamingsandals.commands.bukkit.wrapper;

import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.handler.CommandHandler;
import org.screamingsandals.commands.api.handler.TabHandler;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BukkitCommandWrapper extends AbstractCommandWrapper<Command> {

    @Inject
    public BukkitCommandWrapper(CommandHandler commandHandler, TabHandler tabHandler) {
        super(commandHandler, tabHandler);
    }

    @Override
    public WrappedCommand<Command> wrap(CommandNode node) {
        return () -> new Command(node.getName(), node.getDescription(), node.getUsage(), node.getAliases()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[]
                    args) {
                final var context = new CommandContext(SenderWrapper.of(sender), node, Arrays.asList(args));
                commandHandler.handle(context);

                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String
                    alias, @NotNull String[] args) throws IllegalArgumentException {
                final var toReturn = tabHandler.handle(
                        new CommandContext(SenderWrapper.of(sender), node, Arrays.asList(args)));

                return Objects.requireNonNullElseGet(toReturn, List::of);

            }
        };

    }
}
