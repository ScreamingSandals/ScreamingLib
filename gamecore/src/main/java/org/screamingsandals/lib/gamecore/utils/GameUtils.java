package org.screamingsandals.lib.gamecore.utils;

import org.screamingsandals.lib.gamecore.core.GameTimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

public class GameUtils {

    public static String convertTimeUnitToLanguage(int period, GameTimeUnit gameTimeUnit) {
        switch (gameTimeUnit) {
            case TICKS: {
                if (period <= 1) {
                    return m("general.time-units.tick").get();
                } else {
                    return m("general.time-units.ticks").get();
                }
            }
            case SECONDS: {
                if (period <= 1) {
                    return m("general.time-units.second").get();
                } else {
                    return m("general.time-units.seconds").get();
                }
            }
            case MINUTES: {
                if (period <= 1) {
                    return m("general.time-units.minute").get();
                } else {
                    return m("general.time-units.minutes").get();
                }
            }
        }
        return gameTimeUnit.toString();
    }

    public static String convertNullToLanguage() {
        return m("general.null-translated", "Nothing").get();
    }

    public static String getInfinityLanguage() {
        return m("general.infinity", "Infinity").get();
    }
}
