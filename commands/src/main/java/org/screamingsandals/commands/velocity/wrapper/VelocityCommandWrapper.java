package org.screamingsandals.commands.velocity.wrapper;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.HandlerRegistry;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;

import java.util.List;

public class VelocityCommandWrapper extends AbstractCommandWrapper<RawCommand> {

    @Inject
    public VelocityCommandWrapper(HandlerRegistry handlerRegistry) {
        super(handlerRegistry);
    }

    @Override
    @SuppressWarnings("deprecation")
    public WrappedCommand<RawCommand> wrap(CommandNode node) {
        return () -> new RawCommand() {
            @Override
            public void execute(CommandSource source, @NotNull @NonNull String[] args) {

            }

            @Override
            public boolean hasPermission(CommandSource source, @NotNull @NonNull String[] args) {
                //I want to handle this :)
                return true;
            }

            @Override
            public List<String> suggest(CommandSource source, @NotNull @NonNull String[] currentArgs) {
                return null;
            }
        };
    }
}
