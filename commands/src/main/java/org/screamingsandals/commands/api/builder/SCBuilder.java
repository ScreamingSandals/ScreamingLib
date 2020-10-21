package org.screamingsandals.commands.api.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.core.command.SimpleCommandNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        protected final Multimap<CommandCallback.Priority, CommandCallback> callbacks = ArrayListMultimap.create();
        protected final CommandRegistry registry;
        protected final String name;
        protected String permission;
        protected String description;
        protected String usage;

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
            callbacks.put(priority, callback);
            return this;
        }

        public CommandNode build() {
            final var node = SimpleCommandNode.buildNode(name, permission, description, usage, callbacks);
            registry.register(node);
            return node;
        }
    }

    public static class SubCommandBuilder extends CommandBuilder {
        private final CommandNode parent;

        SubCommandBuilder(CommandRegistry registry, String name, CommandNode parent) {
            super(registry, name);
            this.parent = parent;
        }

        public CommandNode build() {
            final var node = SimpleCommandNode.buildNode(name, permission, description, usage,
                    callbacks, parent, parent.getOwner().orElse(null));
            registry.register(node);
            return node;
        }
    }
}
