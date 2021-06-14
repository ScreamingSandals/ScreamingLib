package org.screamingsandals.lib.proxiedplayer.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerLeaveEvent extends AbstractEvent {
    private final ProxiedPlayerWrapper player;
    private final LoginStatus status;

    public enum LoginStatus {
        CANCELLED_BY_PROXY,
        CANCELLED_BY_USER,
        CANCELLED_BY_USER_BEFORE_COMPLETE,
        CONFLICTING_LOGIN,
        PRE_SERVER_JOIN,
        SUCCESSFUL_LOGIN;

        public static final List<LoginStatus> VALUES = Arrays.asList(values());

        public static LoginStatus convert(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow();
        }
    }
}
