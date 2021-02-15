package org.screamingsandals.lib.sender.permissions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
public class AndPermission {
    private final List<Permission> permissions;

    public static AndPermission of(Permission...permissions) {
        return of(Arrays.asList(permissions));
    }
}
