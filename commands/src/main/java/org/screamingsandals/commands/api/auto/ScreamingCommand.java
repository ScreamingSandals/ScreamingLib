package org.screamingsandals.commands.api.auto;

/**
 * Interface for automatic registering from classpath.
 *
 * All you need to do is create the command via {@link org.screamingsandals.commands.api.builder.SCBuilder}
 * in the {@link ScreamingCommand#register()} method.
 */
public interface ScreamingCommand {

    /**
     * Dummy method for invoking command registration
     */
    void register();
}
