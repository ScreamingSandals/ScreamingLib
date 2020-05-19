package org.screamingsandals.lib.gamecore.utils;

import java.util.concurrent.TimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

public class GameUtils {

    public static String convertTimeUnitToLanguage(int period, TimeUnit timeUnit) {
        switch (timeUnit) {
            case MILLISECONDS: {
                if (period == 50) {
                    return m("general.time-units.tick").get();
                } else {
                    return m("general.time-units.ticks").get();
                }
            }
            case SECONDS: {
                if (period == 1) {
                    return m("general.time-units.second").get();
                } else {
                    return m("general.time-units.seconds").get();
                }
            }
            case MINUTES: {
                if (period == 1) {
                    return m("general.time-units.minute").get();
                } else {
                    return m("general.time-units.minutes").get();
                }
            }
        }
        return timeUnit.toString();
    }

    public static int convertMilisecondsToTick(int period) {
        return period / 50;
    }

    public static int convertTickToMiliseconds(int period) {
        return period * 50;
    }

    public static String convertNullToLanguage() {
        return m("general.null-translated", "Nothing").get();
    }

    public static String getInfinityLanguage() {
        return m("general.infinity", "Infinity").get();
    }
}
