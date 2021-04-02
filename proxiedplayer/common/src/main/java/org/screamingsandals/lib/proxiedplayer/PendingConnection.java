package org.screamingsandals.lib.proxiedplayer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Pending connection for the login
 */
@Data
@AllArgsConstructor
public class PendingConnection {
    private final String name;
    private final int version;
    private final InetSocketAddress address;
    private final boolean legacy;

    private UUID uuid;
    private boolean onlineMode;
}
