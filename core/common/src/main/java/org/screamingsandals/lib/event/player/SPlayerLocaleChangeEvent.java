package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
public class SPlayerLocaleChangeEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<String> locale;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public String getLocale() {
        return locale.get();
    }
}
