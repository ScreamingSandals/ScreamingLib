package org.screamingsandals.commands.api.builder;

import com.google.common.base.Preconditions;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.command.SubCommandNode;
import org.screamingsandals.commands.api.tab.TabCallback;
import org.screamingsandals.commands.core.command.SimpleSubCommandNode;

import java.util.LinkedList;
import java.util.List;

public class SubCommandBuilder {
    private final String name;

    private List<CommandCallback> callbacks = new LinkedList<>();
    private String permission;
    private String description;
    private String usage;
    private TabCallback tabCallback;

    protected CommandNode parent;

    SubCommandBuilder(String name, CommandNode parent) {
        this.name = name;
        this.parent = parent;
        this.description = parent.getDescription();
        this.permission = parent.getPermission();
        this.usage = parent.getUsage();
    }

    SubCommandBuilder(PartialSubCommandBuilder builder, CommandNode parent) {
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.usage = builder.getUsage();
        this.permission = builder.getPermission();
        this.tabCallback = builder.getTabCallback();
        this.callbacks = builder.getCallbacks();
        this.parent = parent;
    }

    public SubCommandBuilder permission(String permission) {
        this.permission = Preconditions.checkNotNull(permission, "permission");
        return this;

    }

    public SubCommandBuilder description(String description) {
        this.description = Preconditions.checkNotNull(description, "description");
        return this;
    }

    public SubCommandBuilder usage(String usage) {
        this.usage = Preconditions.checkNotNull(usage, "usage");
        return this;
    }

    public SubCommandBuilder callback(CommandCallback callback) {
        callbacks.add(Preconditions.checkNotNull(callback, "callback"));
        return this;
    }

    public SubCommandBuilder tabCallback(TabCallback tabCallback) {
        this.tabCallback = Preconditions.checkNotNull(tabCallback, "tabCallback");
        return this;
    }

    public SubCommandNode build() {
        final var node = SimpleSubCommandNode.build(
                name, parent, permission, description, usage, callbacks, tabCallback);

        parent.addSubNode(node);
        return node;
    }
}