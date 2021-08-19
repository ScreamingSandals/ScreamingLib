package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SSculkSensorReceiveEvent;

// TODO: what about not doing it via reflection? huh
public class BlockReceiveGameEventListener extends AbstractBukkitEventHandlerFactory<Event, SSculkSensorReceiveEvent> {

    public BlockReceiveGameEventListener(Plugin plugin) {
        super(Reflect.getClassSafe("org.bukkit.event.block.BlockReceiveGameEvent"), SSculkSensorReceiveEvent.class, plugin);
    }

    @Override
    protected SSculkSensorReceiveEvent wrapEvent(Event event, EventPriority priority) {
        var wrapper = new InvocationResult(event);
        return new SSculkSensorReceiveEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(wrapper.fastInvoke("getBlock"))),
                ImmutableObjectLink.of(() -> NamespacedMappingKey.of(wrapper.fastInvokeResulted("getEvent").fastInvokeResulted("getKey").toString())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(wrapper.fastInvoke("getEntity")).orElse(null))
        );
    }
}
