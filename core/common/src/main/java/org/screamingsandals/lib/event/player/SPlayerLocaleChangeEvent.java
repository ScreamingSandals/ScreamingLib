package org.screamingsandals.lib.event.player;

import java.util.Locale;

public interface SPlayerLocaleChangeEvent extends SPlayerEvent {

    Locale getLocale();
}
