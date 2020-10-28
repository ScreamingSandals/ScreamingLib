package org.screamingsandals.commands.bungee.wrapper;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.HandlerRegistry;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BungeeCommandWrapper extends AbstractCommandWrapper<Command> {

    @Inject
    public BungeeCommandWrapper(HandlerRegistry handlerRegistry) {
        super(handlerRegistry);
    }

    @Override
    public WrappedCommand<Command> wrap(CommandNode node) {
        return new WrappedCommand<>() {
            @Override
            public Command getCommand() {
                return new WrapperBungeeCommand(node, handlerRegistry);
            }

            @Override
            public CommandNode getNode() {
                return node;
            }
        };
    }

    private static class WrapperBungeeCommand extends Command implements TabExecutor {
        private final CommandNode node;
        private final HandlerRegistry handlerRegistry;

        public WrapperBungeeCommand(CommandNode node, HandlerRegistry handlerRegistry) {
            super(node.getName(), node.getPermission(), node.getAliases().toArray(String[]::new));
            this.node = node;
            this.handlerRegistry = handlerRegistry;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            handlerRegistry.getCommandHandler().handle(
                    new CommandContext(SenderWrapper.of(sender), node, Arrays.asList(args)));
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            final var context = new CommandContext(SenderWrapper.of(sender), node, Arrays.asList(args));
            final var toReturn = handlerRegistry.getTabHandler().handle(context);

            return Objects.requireNonNullElseGet(toReturn, List::of);
        }
    }
}
