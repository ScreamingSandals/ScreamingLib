package org.screamingsandals.commands.velocity.registry;

import com.google.inject.Inject;
import com.velocitypowered.api.command.RawCommand;
import org.screamingsandals.commands.core.registry.AbstractCommandRegistry;
import org.screamingsandals.commands.velocity.wrapper.VelocityCommandWrapper;

public class VelocityCommandRegistry extends AbstractCommandRegistry<RawCommand>  {

    @Inject
    public VelocityCommandRegistry(VelocityCommandWrapper wrapper, VelocityServerRegistry serverRegistry) {
        super(wrapper, serverRegistry);
    }
}
