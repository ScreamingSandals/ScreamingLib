package org.screamingsandals.lib.sender.permissions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
public class OrPermission {
    private final List<Permission> permissions;

    public static OrPermission of(Permission...permissions) {
        return of(Arrays.asList(permissions));
    }
}
