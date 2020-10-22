package org.screamingsandals.commands.api.builder;

import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.tab.TabCallback;
import org.screamingsandals.commands.core.command.SimpleCommandNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

public class SCBuilder {
    protected static Logger log = LoggerFactory.getLogger(SCBuilder.class);
    private static SCBuilder instance;
    private final CommandRegistry registry;

    @Inject
    public SCBuilder(CommandRegistry registry) {
        instance = this;
        this.registry = Preconditions.checkNotNull(registry, "registry");
    }

    public static CommandBuilder command(String name) {

        return new CommandBuilder(instance.registry, Preconditions.checkNotNull(name, "name"));
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
        protected TabCallback tabCallback;

        public CommandBuilder permission(String permission) {
            this.permission = Preconditions.checkNotNull(permission, "permission");
            return this;

        }

        public CommandBuilder description(String description) {
            this.description = Preconditions.checkNotNull(description, "description");
            return this;
        }

        public CommandBuilder usage(String usage) {
            this.usage = Preconditions.checkNotNull(usage, "usage");
            return this;
        }

        public CommandBuilder callback(CommandCallback callback) {
            return callback(Preconditions.checkNotNull(callback, "callback"), CommandCallback.Priority.NORMAL);
        }

        public CommandBuilder callback(CommandCallback callback, CommandCallback.Priority priority) {
            callbacks.put(Preconditions.checkNotNull(priority, "priority"),
                    Preconditions.checkNotNull(callback, "callback"));
            return this;
        }

        public CommandBuilder tabCallback(TabCallback tabCallback) {
            this.tabCallback = Preconditions.checkNotNull(tabCallback, "tabCallback");
            return this;
        }

        public CommandNode build() {
            final var node = SimpleCommandNode.buildNode(
                    name, permission, description, usage, callbacks, tabCallback);
            final var result = registry.register(node);

            if (result.isFail()) {
                log.trace("Result of registering command named [{}] has FAILED! Result: [{}]", name, result.getMessage());
                return null;
            }
            return node;
        }
    }

    public static class SubCommandBuilder extends CommandBuilder {
        private final CommandNode parent;

        SubCommandBuilder(CommandRegistry registry, String name, CommandNode parent) {
            super(registry, name);
            this.parent = parent;
            this.description = parent.getDescription();
            this.permission = parent.getPermission();
            this.usage = parent.getUsage();
        }

        @Override
        public SubCommandBuilder permission(String permission) {
            return (SubCommandBuilder) super.permission(permission);
        }

        @Override
        public SubCommandBuilder description(String description) {
            return (SubCommandBuilder) super.description(description);
        }

        @Override
        public SubCommandBuilder usage(String usage) {
            return (SubCommandBuilder) super.usage(usage);
        }

        @Override
        public SubCommandBuilder callback(CommandCallback callback) {
            return (SubCommandBuilder) super.callback(callback);
        }

        @Override
        public SubCommandBuilder callback(CommandCallback callback, CommandCallback.Priority priority) {
            return (SubCommandBuilder) super.callback(callback, priority);
        }

        @Override
        public SubCommandBuilder tabCallback(TabCallback tabCallback) {
            return (SubCommandBuilder) super.tabCallback(tabCallback);
        }

        @Override
        public CommandNode build() {
            final var node = SimpleCommandNode.buildNode(name, permission, description, usage,
                    callbacks, parent, parent.getOwner().orElse(null), tabCallback);
            final var result = registry.register(node);

            if (result.isFail()) {
                log.trace("Result of registering sub-command named [{}] has FAILED! Result: [{}]", name, result.getMessage());
                return null;
            }
            return node;
        }
    }
}
