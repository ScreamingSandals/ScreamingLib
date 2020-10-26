package org.screamingsandals.commands.core.command;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.commands.api.command.CommandBase;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractCommandBase implements CommandBase {
    protected final String name;

    protected String permission;
    protected String description;
    protected String usage;
    protected List<String> aliases = new LinkedList<>();

    protected Multimap<CommandCallback.Priority, CommandCallback> callbacks = ArrayListMultimap.create();
    protected TabCallback tabCallback;

    protected AbstractCommandBase(String name) {
        this.name = name;
    }

    protected AbstractCommandBase(String name, CommandBase base) {
        this.name = name;
        this.permission = base.getPermission();
        this.description = base.getPermission();
        this.usage = base.getUsage();
    }

    @Override
    public void addCallback(CommandCallback callback) {
        addCallback(CommandCallback.Priority.NORMAL, callback);
    }

    @Override
    public void addCallback(CommandCallback.Priority priority, CommandCallback callback) {
        callbacks.put(priority, callback);
    }

    @Override
    public List<CommandCallback> getCallbacks() {
        return new LinkedList<>(callbacks.values());
    }
}
