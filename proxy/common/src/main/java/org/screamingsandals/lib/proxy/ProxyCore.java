package org.screamingsandals.lib.proxy;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;

@ServiceDependencies(dependsOn = {
        EventManager.class,
        ProxiedPlayerMapper.class
})
@InternalCoreService
public abstract class ProxyCore {
}
