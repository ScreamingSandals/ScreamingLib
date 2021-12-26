package org.screamingsandals.lib.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.function.Function;
import java.util.function.Supplier;

@AbstractService
public abstract class CloudConstructor {
    private static CloudConstructor cloudConstructor;

    @ApiStatus.Internal
    public static void init(Supplier<CloudConstructor> cloudConstructorSupplier) {
        if (cloudConstructor != null) {
            throw new UnsupportedOperationException("CloudConstructor has been already initialized!");
        }

        cloudConstructor = cloudConstructorSupplier.get();
    }

    public static boolean isInitialized() {
        return cloudConstructor != null;
    }

    public static CommandManager<CommandSenderWrapper> construct(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception {
        if (cloudConstructor == null) {
            throw new UnsupportedOperationException("CloudConstructor is not initialized yet!");
        }
        return cloudConstructor.construct0(commandCoordinator);
    }

    public abstract CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception;
}
