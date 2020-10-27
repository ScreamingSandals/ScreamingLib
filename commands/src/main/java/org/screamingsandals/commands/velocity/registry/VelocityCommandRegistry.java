package org.screamingsandals.commands.velocity.registry;

import com.velocitypowered.api.command.RawCommand;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.commands.core.registry.AbstractCommandRegistry;

public class VelocityCommandRegistry extends AbstractCommandRegistry<RawCommand> {

    public VelocityCommandRegistry(AbstractCommandWrapper<RawCommand> wrapper, ServerRegistry<RawCommand> serverRegistry) {
        super(wrapper, serverRegistry);
    }
}
