package org.screamingsandals.commands.core.guice;

import com.google.inject.Inject;

/**
 * Class for automatic registering of them commands
 */
public interface CommandClass {

    /**
     * Invokes this method that should contains all commands
     */
    @Inject
    void register();
}
