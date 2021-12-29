package org.screamingsandals.lib.utils;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class Version {
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\d\\.\\d\\d?(?:\\.\\d)?");
    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;

    public static Version extract(String extractable) {
        final var matcher = VERSION_PATTERN.matcher(extractable);
        if (!matcher.find()) {
            throw new IllegalStateException("Version not found in extractable");
        }
        return new Version(matcher.group());
    }

    public Version(String version) {
        final var split = version.split("\\.");

        majorVersion = Integer.parseInt(split[0]);
        minorVersion = Integer.parseInt(split[1]);
        patchVersion = (split.length == 3) ? Integer.parseInt(split[2]) : 0;
    }

    public boolean isVersion(int major, int minor) {
        return isVersion(major, minor, 0);
    }

    public boolean isVersion(int major, int minor, int patch) {
        return majorVersion > major || (majorVersion >= major && (minorVersion > minor || (minorVersion >= minor && patchVersion >= patch)));
    }

    @Override
    public String toString() {
        return majorVersion + "." + minorVersion + ((patchVersion == 0) ? "" : "." + patchVersion);
    }
}
