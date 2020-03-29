package org.screamingsandals.lib.update_checker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Result {
	private final String version;
	
	public boolean needsUpdate(String checkVersion0) {
		var currentVersion = checkVersion0.split("-")[0];
		currentVersion += (checkVersion0.toLowerCase().contains("pre")
				|| checkVersion0.toLowerCase().contains("snapshot")) ? ".0" : ".1";
		var checkVersion = version.split("-")[0];
		checkVersion += (version.toLowerCase().contains("pre")
				|| version.toLowerCase().contains("snapshot")) ? ".0" : ".1";
		
		currentVersion = currentVersion.replace(".", "");
		checkVersion = checkVersion.replace(".", "");

		try {
			int current = Integer.parseInt(currentVersion);
			int check = Integer.parseInt(checkVersion);

			return check > current;
		} catch (NumberFormatException e) {
		}
		return false;
	}
}
