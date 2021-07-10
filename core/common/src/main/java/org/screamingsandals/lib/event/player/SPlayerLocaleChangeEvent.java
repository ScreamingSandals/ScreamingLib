package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
public class SPlayerLocaleChangeEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final String locale;
}
