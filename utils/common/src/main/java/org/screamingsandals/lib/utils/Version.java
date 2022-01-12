/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
