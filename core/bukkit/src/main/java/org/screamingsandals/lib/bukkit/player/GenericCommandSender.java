package org.screamingsandals.lib.bukkit.player;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;

public class GenericCommandSender extends BasicWrapper<CommandSender> implements SenderWrapper {
    public GenericCommandSender(CommandSender wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Type getType() {
        if (wrappedObject instanceof Player) {
            return Type.PLAYER;
        } else if (wrappedObject instanceof ConsoleCommandSender) {
            return Type.CONSOLE;
        } else {
            return Type.UNKNOWN;
        }
    }

    @Override
    public void sendMessage(String message) {
        wrappedObject.sendMessage(message);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public @NotNull Audience audience() {
        return BukkitCore.audiences().sender(wrappedObject);
    }

    @Override
    public boolean isOp() {
        return wrappedObject.isOp();
    }

    @Override
    public void setOp(boolean op) {
        wrappedObject.setOp(op);
    }

    @Override
    public void tryToDispatchCommand(String command) {
        Bukkit.dispatchCommand(wrappedObject, command);
    }
}
