package org.screamingsandals.commands.api.builder;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.LinkedList;
import java.util.List;

@Getter
public class PartialSubCommandBuilder {
    private final List<CommandCallback> callbacks = new LinkedList<>();

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
        callbacks.add(Preconditions.checkNotNull(callback, "callback"));
        return this;
    }

    public PartialSubCommandBuilder tabCallback(TabCallback tabCallback) {
        this.tabCallback = Preconditions.checkNotNull(tabCallback, "tabCallback");
        return this;
    }
}
