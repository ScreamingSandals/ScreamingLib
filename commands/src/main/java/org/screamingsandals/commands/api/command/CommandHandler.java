package org.screamingsandals.commands.api.command;

import java.util.List;

public interface CommandHandler {

    void handle(CommandContext context);

    CommandNode withCallback(CommandCallback callback);

    List<CommandCallback> getCallbacks();
}
