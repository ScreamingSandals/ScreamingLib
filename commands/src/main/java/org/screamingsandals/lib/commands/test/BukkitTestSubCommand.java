package org.screamingsandals.lib.commands.test;

import org.screamingsandals.lib.commands.common.RegisterCommand;
import org.screamingsandals.lib.commands.common.interfaces.ScreamingCommand;

@RegisterCommand(subCommand = true)
public class BukkitTestSubCommand implements ScreamingCommand {

    @Override
    public void register() {

    }
}
