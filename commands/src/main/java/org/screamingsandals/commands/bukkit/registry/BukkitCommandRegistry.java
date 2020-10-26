package org.screamingsandals.commands.bukkit.registry;

import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.screamingsandals.commands.bukkit.wrapper.BukkitCommandWrapper;
import org.screamingsandals.commands.core.registry.AbstractCommandRegistry;

public class BukkitCommandRegistry extends AbstractCommandRegistry<Command> {

    @Inject
    public BukkitCommandRegistry(BukkitCommandWrapper wrapper, BukkitServerCommandRegistry serverRegistry) {
        super(wrapper, serverRegistry);
    }
}
