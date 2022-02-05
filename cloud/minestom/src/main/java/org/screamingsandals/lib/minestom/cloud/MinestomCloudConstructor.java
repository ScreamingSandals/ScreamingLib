package org.screamingsandals.lib.minestom.cloud;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import net.minestom.server.extensions.Extension;
import org.screamingsandals.lib.cloud.CloudConstructor;
import org.screamingsandals.lib.minestom.player.MinestomPlayerMapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Function;

@Service(dependsOn = {
        MinestomPlayerMapper.class
})
public class MinestomCloudConstructor extends CloudConstructor {
    public static void init() {
        CloudConstructor.init(MinestomCloudConstructor::new);
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        return new MinestomScreamingCloudManager(commandCoordinator);
    }
}
