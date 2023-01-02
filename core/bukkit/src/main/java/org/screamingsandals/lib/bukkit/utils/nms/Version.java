/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.utils.nms;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Version {
	
	public static final int MAJOR_VERSION;
	public static final int MINOR_VERSION;
	public static final int PATCH_VERSION;
	
	static {
		Pattern versionPattern = Pattern.compile("\\(MC: (\\d+)\\.(\\d+)\\.?(\\d+?)?");
        Matcher matcher = versionPattern.matcher(Bukkit.getVersion());
        int majorVersion = 1;
        int minorVersion = 0;
        int patchVersion = 0;
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            try {
            	majorVersion = Integer.parseInt(matchResult.group(1), 10);
            } catch (Exception ignored) {
            }
            try {
            	minorVersion = Integer.parseInt(matchResult.group(2), 10);
            } catch (Exception ignored) {
            }
            if (matchResult.groupCount() >= 3) {
                try {
                    patchVersion = Integer.parseInt(matchResult.group(3), 10);
                } catch (Exception ignored) {
                }
            }
        }
        MAJOR_VERSION = majorVersion;
        MINOR_VERSION = minorVersion;
        PATCH_VERSION = patchVersion;
	}

	public static boolean isVersion(int major, int minor) {
		return isVersion(major, minor, 0);
	}

	public static boolean isVersion(int major, int minor, int patch) {
		return MAJOR_VERSION > major || (MAJOR_VERSION >= major && (MINOR_VERSION > minor || (MINOR_VERSION >= minor && PATCH_VERSION >= patch)));
	}
}
