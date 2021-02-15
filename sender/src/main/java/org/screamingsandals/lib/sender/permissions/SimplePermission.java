package org.screamingsandals.lib.sender.permissions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class SimplePermission implements Permission {
    private final String permissionString;
    private final boolean defaultAllowed;

    public static SimplePermission of(String line) {
        return of(line, false);
    }
}
