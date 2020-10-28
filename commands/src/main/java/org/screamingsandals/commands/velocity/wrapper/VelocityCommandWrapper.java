package org.screamingsandals.commands.velocity.wrapper;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.HandlerRegistry;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VelocityCommandWrapper extends AbstractCommandWrapper<RawCommand> {

    @Inject
    public VelocityCommandWrapper(HandlerRegistry handlerRegistry) {
        super(handlerRegistry);
    }

    @Override
    @SuppressWarnings("deprecation")
    public WrappedCommand<RawCommand> wrap(CommandNode node) {
        return new WrappedCommand<>() {
            @Override
            public RawCommand getCommand() {
                return new RawCommand() {
                    @Override
                    public void execute(Invocation invocation) {
                        final var args =  Arrays.asList(invocation.arguments().split(" ", -1));
                        final var context = new CommandContext(SenderWrapper.of(invocation.source()), node, args);

                        handlerRegistry.getCommandHandler().handle(context);
                    }


                    @Override
                    public boolean hasPermission(CommandSource source, @NotNull @NonNull String[] args) {
                        //I want to handle this :)
                        return true;
                    }

                    @Override
                    public List<String> suggest(Invocation invocation) {
                        final var args =  Arrays.asList(invocation.arguments().split(" ", -1));
                        final var context = new CommandContext(SenderWrapper.of(invocation.source()), node, args);
                        final var toReturn = handlerRegistry.getTabHandler().handle(context);

                        return Objects.requireNonNullElseGet(toReturn, List::of);
                    }
                };
            }

            @Override
            public CommandNode getNode() {
                return node;
            }
        };

    }
}
