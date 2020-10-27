package org.screamingsandals.commands.api.builder;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.tab.TabCallback;
import org.screamingsandals.commands.core.command.SimpleCommandNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CommandBuilder {
    protected static Logger log = LoggerFactory.getLogger(CommandBuilder.class);
    private final List<CommandCallback> callbacks = new LinkedList<>();
    private final List<PartialSubCommandBuilder> partialSubCommands = new LinkedList<>();

    private final CommandRegistry registry;
    private final String name;

    private String permission = "";
    private String description;
    private String usage;
    private TabCallback tabCallback;

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
        callbacks.add(Preconditions.checkNotNull(callback, "callback"));
        return this;
    }

    public CommandBuilder tabCallback(TabCallback tabCallback) {
        this.tabCallback = Preconditions.checkNotNull(tabCallback, "tabCallback");
        return this;
    }

    public CommandBuilder withSubNode(PartialSubCommandBuilder builder) {
        partialSubCommands.add(builder);
        return this;
    }

    public CommandNode build() {
        final var node = SimpleCommandNode.build(
                name, permission, description, usage, callbacks, tabCallback);
        final var result = registry.registerNode(node);

        if (result.isFail()) {
            log.warn("Result of registering command named [{}] has FAILED! Result: [{}]", name, result.getMessage());
            return null;
        }

        partialSubCommands.forEach(command -> {
            final var subCommand = new SubCommandBuilder(command, node);
            final var subNode = subCommand.build();

            if (subNode == null) {
                log.warn("Error registering SubNode {}!", command.getName());
                return;
            }

            log.debug("Registering sub-node {}", subNode.getName());

            node.addSubNode(subNode);
        });
        return node;
    }
}
