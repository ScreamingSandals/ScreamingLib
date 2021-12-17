package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.Collection;

@LimitedVersionSupport(">= 1.13")
public interface SPlayerCommandSendEvent extends SPlayerEvent, PlatformEventWrapper {

    Collection<String> getCommands();
}
