package org.screamingsandals.lib.sender.permissions;

import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "of")
public class PredicatePermission implements Permission {
    private final Predicate<CommandSenderWrapper> predicate;

    @Override
    public boolean hasPermission(CommandSenderWrapper commandSender) {
        return predicate.test(commandSender);
    }
}
