package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerLocaleChangeEvent extends SPlayerEvent {
    private final ImmutableObjectLink<String> locale;

    public SPlayerLocaleChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                    ImmutableObjectLink<String> locale) {
        super(player);
        this.locale = locale;
    }

    public String getLocale() {
        return locale.get();
    }
}
