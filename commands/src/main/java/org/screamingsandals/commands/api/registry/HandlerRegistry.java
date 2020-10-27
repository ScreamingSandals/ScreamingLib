package org.screamingsandals.commands.api.registry;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.commands.api.handler.CommandHandler;
import org.screamingsandals.commands.api.handler.TabHandler;

public interface HandlerRegistry {

    CommandHandler getCommandHandler();

    void setCommandHandler(@NotNull CommandHandler commandHandler);

    TabHandler getTabHandler();

    void setTabHandler(@NotNull TabHandler tabHandler);
}
