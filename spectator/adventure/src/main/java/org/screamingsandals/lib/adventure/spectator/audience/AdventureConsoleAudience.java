package org.screamingsandals.lib.adventure.spectator.audience;

import net.kyori.adventure.audience.Audience;
import org.screamingsandals.lib.spectator.audience.ConsoleAudience;

public class AdventureConsoleAudience extends AdventureAudience implements ConsoleAudience {
    protected AdventureConsoleAudience(Audience wrappedObject, org.screamingsandals.lib.spectator.audience.Audience.ForwardingSingle screamingLibAudience) {
        super(wrappedObject, screamingLibAudience);
    }
}
