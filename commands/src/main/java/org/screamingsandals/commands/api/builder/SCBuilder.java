package org.screamingsandals.commands.api.builder;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;

public class SCBuilder {
    private static SCBuilder instance;
    private final CommandRegistry<?> registry;

    @Inject
    public SCBuilder(CommandRegistry<?> registry) {
        instance = this;
        this.registry = Preconditions.checkNotNull(registry, "registry");
    }

    public static CommandBuilder command(String name) {

        return new CommandBuilder(instance.registry, Preconditions.checkNotNull(name, "name"));
    }

    public static SubCommandBuilder subCommand(String name, CommandNode parent) {
        return new SubCommandBuilder(name, parent);
    }

    public static PartialSubCommandBuilder subCommand(String name) {
        return new PartialSubCommandBuilder(name);
    }
}
