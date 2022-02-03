package org.screamingsandals.lib.bungee.spectator.audience.adapter;

import net.md_5.bungee.api.CommandSender;
import org.screamingsandals.lib.spectator.audience.ConsoleAudience;
import org.screamingsandals.lib.spectator.audience.adapter.ConsoleAdapter;

public class BungeeConsoleAdapter extends BungeeAdapter implements ConsoleAdapter {
    public BungeeConsoleAdapter(CommandSender sender, ConsoleAudience owner) {
        super(sender, owner);
    }

    @Override
    public ConsoleAudience owner() {
        return (ConsoleAudience) super.owner();
    }
}
