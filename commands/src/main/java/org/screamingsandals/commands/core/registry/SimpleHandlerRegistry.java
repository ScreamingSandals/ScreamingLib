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
    private CommandHandler commandHandler;
    private TabHandler tabHandler;

    public SimpleHandlerRegistry() {
        this.commandHandler = new SimpleCommandHandler();
        this.tabHandler = new SimpleTabHandler();
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public void setCommandHandler(@NotNull CommandHandler commandHandler) {
        this.commandHandler = Preconditions.checkNotNull(commandHandler);
    }

    @Override
    public TabHandler getTabHandler() {
        return tabHandler;
    }

    @Override
    public void setTabHandler(@NotNull TabHandler tabHandler) {
        this.tabHandler = Preconditions.checkNotNull(tabHandler);
    }
}
