package org.screamingsandals.lib.proxiedplayer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class ServerWrapper implements Wrapper {
    private final String name;
    private final SocketAddress address;

    /**
     * @return optional containing InetSocketAddress or empty optional if connection is done by Unix domain socket
     */
    public Optional<InetSocketAddress> getAddress() {
        if (address instanceof InetSocketAddress) {
            return Optional.of((InetSocketAddress) address);
        } else {
            return Optional.empty();
        }
    }

    public List<ProxiedPlayerWrapper> getPlayers() {
        return ProxiedPlayerMapper.getPlayers(this);
    }

    public <T> T as(Class<T> type) {
        return ProxiedPlayerMapper.convertServerWrapper(this, type);
    }
}
