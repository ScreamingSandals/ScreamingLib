package org.screamingsandals.commands.bungee.registry;

import com.google.inject.Inject;
import net.md_5.bungee.api.plugin.Command;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.bungee.wrapper.BungeeCommandWrapper;
import org.screamingsandals.commands.core.registry.AbstractCommandRegistry;

public class BungeeCommandRegistry extends AbstractCommandRegistry<Command> implements CommandRegistry {

    @Inject
    public BungeeCommandRegistry(BungeeCommandWrapper wrapper, BungeeServerRegistry serverRegistry) {
        super(wrapper, serverRegistry);
    }
}
