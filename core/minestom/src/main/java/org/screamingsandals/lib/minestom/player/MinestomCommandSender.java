package org.screamingsandals.lib.minestom.player;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;

public class MinestomCommandSender extends BasicWrapper<CommandSender> implements SenderWrapper {
    public MinestomCommandSender(CommandSender wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void tryToDispatchCommand(String command) {
        MinecraftServer.getCommandManager().execute(wrappedObject, command);
    }

    @Override
    public Type getType() {
        if (wrappedObject instanceof Player) {
            return Type.PLAYER;
        } else if (wrappedObject instanceof ConsoleSender) {
            return Type.CONSOLE;
        }
        return Type.UNKNOWN;
    }

    @Override
    public void sendMessage(String message) {
        wrappedObject.sendMessage(message);
    }

    @Override
    public String getName() {
        return (wrappedObject instanceof Player) ? ((Player) wrappedObject).getUsername() : "CONSOLE";
    }

    @Override
    public @NotNull Audience audience() {
        return wrappedObject;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean op) {
        // empty stub
    }
}
