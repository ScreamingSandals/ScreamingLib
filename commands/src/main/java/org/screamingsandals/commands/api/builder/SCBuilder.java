package org.screamingsandals.commands.api.builder;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;

import java.util.LinkedList;
import java.util.List;

public class SCBuilder {
    private static SCBuilder instance;
    private final CommandRegistry registry;

    @Inject
    public SCBuilder(CommandRegistry registry) {
        instance = this;
        this.registry = registry;
    }

    public static CommandBuilder command(String name) {
        return new CommandBuilder(instance.registry, name);
    }

    public static SubCommandBuilder subCommand(String name, CommandNode parent) {
        return new SubCommandBuilder(instance.registry, name, parent);
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static class CommandBuilder {
        private final CommandRegistry registry;
        private final String name;
        private String permission;
        private String description;
        private String usage;
        private List<CommandCallback> callbacks = new LinkedList<>();

        private CommandNode node;

        public CommandBuilder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public CommandBuilder description(String description) {
            this.description = description;
            return this;
        }


        public CommandBuilder usage(String usage) {
            this.usage = usage;
            return this;
        }

        public CommandBuilder callback(CommandCallback callback) {
            return callback(callback, CommandCallback.Priority.NORMAL);
        }

        public CommandBuilder callback(CommandCallback callback, CommandCallback.Priority priority) {
            callbacks.add(callback);
            return this;
        }
    }

    public static class SubCommandBuilder extends CommandBuilder {
        private final CommandNode parent;

        SubCommandBuilder(CommandRegistry registry, String name, CommandNode parent) {
            super(registry, name);
            this.parent = parent;
        }
    }
}
