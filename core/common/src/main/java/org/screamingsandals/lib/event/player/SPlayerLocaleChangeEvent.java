package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;

import java.util.Locale;

public interface SPlayerLocaleChangeEvent extends SPlayerEvent, PlatformEventWrapper {

    Locale getLocale();
}
