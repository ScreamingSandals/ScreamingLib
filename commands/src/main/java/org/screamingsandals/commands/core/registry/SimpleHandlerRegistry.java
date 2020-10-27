package org.screamingsandals.commands.core.registry;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.commands.api.handler.CommandHandler;
import org.screamingsandals.commands.api.handler.TabHandler;
import org.screamingsandals.commands.api.registry.HandlerRegistry;
import org.screamingsandals.commands.core.handler.SimpleCommandHandler;
import org.screamingsandals.commands.core.handler.SimpleTabHandler;

@Singleton
public class SimpleHandlerRegistry implements HandlerRegistry {
    private final CommandHandler defaultCommandHandler;
    private final TabHandler defaultTabHandler;

    private CommandHandler commandHandler;
    private TabHandler tabHandler;

    public SimpleHandlerRegistry() {
        this.defaultCommandHandler = new SimpleCommandHandler();
        this.defaultTabHandler = new SimpleTabHandler();
    }

    @Override
    public CommandHandler getCommandHandler() {
        if (commandHandler != null) {
            return commandHandler;
        }

        return defaultCommandHandler;
    }

    @Override
    public void setCommandHandler(@NotNull CommandHandler commandHandler) {
        this.commandHandler = Preconditions.checkNotNull(commandHandler);
    }

    @Override
    public TabHandler getTabHandler() {
        if (tabHandler != null) {
            return tabHandler;
        }

        return defaultTabHandler;
    }

    @Override
    public void setTabHandler(@NotNull TabHandler tabHandler) {
        this.tabHandler = Preconditions.checkNotNull(tabHandler);
    }

    @Override
    public void useDefaults() {
        commandHandler = null;
        tabHandler = null;
    }
}
