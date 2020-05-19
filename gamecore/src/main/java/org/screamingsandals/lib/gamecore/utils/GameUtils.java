package org.screamingsandals.lib.gamecore.utils;

import java.util.concurrent.TimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

public class TimeUtils {

    public static String convertTimeUnitToLanguage(int period, TimeUnit timeUnit) {
        switch (timeUnit) {
            case MILLISECONDS: {
                if (period == 50) {
                    return mpr("general.time-units.tick").get();
                } else {
                    return mpr("general.time-units.ticks").get();
                }
            }
            case SECONDS: {
                if (period == 1) {
                    return mpr("general.time-units.second").get();
                } else {
                    return mpr("general.time-units.seconds").get();
                }
            }
            case MINUTES: {
                if (period == 1) {
                    return mpr("general.time-units.minute").get();
                } else {
                    return mpr("general.time-units.minutes").get();
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
}
