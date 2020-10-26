package org.screamingsandals.commands.api.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.tab.TabCallback;

@Getter
public class PartialSubCommandBuilder {
    private final Multimap<CommandCallback.Priority, CommandCallback> callbacks = ArrayListMultimap.create();

    private final String name;

    private String permission;
    private String description;
    private String usage;
    private TabCallback tabCallback;

    PartialSubCommandBuilder(String name) {
        this.name = Preconditions.checkNotNull(name, "name");
    }

    public PartialSubCommandBuilder permission(String permission) {
        this.permission = Preconditions.checkNotNull(permission, "permission");
        return this;

    }

    public PartialSubCommandBuilder description(String description) {
        this.description = Preconditions.checkNotNull(description, "description");
        return this;
    }

    public PartialSubCommandBuilder usage(String usage) {
        this.usage = Preconditions.checkNotNull(usage, "usage");
        return this;
    }

    public PartialSubCommandBuilder callback(CommandCallback callback) {
        return callback(Preconditions.checkNotNull(callback, "callback"), CommandCallback.Priority.NORMAL);
    }

    public PartialSubCommandBuilder callback(CommandCallback callback, CommandCallback.Priority priority) {
        callbacks.put(Preconditions.checkNotNull(priority, "priority"),
                Preconditions.checkNotNull(callback, "callback"));
        return this;
    }

    public PartialSubCommandBuilder tabCallback(TabCallback tabCallback) {
        this.tabCallback = Preconditions.checkNotNull(tabCallback, "tabCallback");
        return this;
    }
}
