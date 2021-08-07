package org.screamingsandals.lib.bukkit.npc;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacket_i_ActionTypeAccessor;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.npc.NPCManager;
import org.screamingsandals.lib.npc.event.NPCInteractEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.visuals.VisualsTouchListener;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import java.util.UUID;

@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class,
        AbstractTaskInitializer.class
})
public class BukkitNPCManager extends NPCManager {
    private static final Object ATTACK_ACTION_FIELD = Reflect.getField(ServerboundInteractPacketAccessor.getFieldATTACK_ACTION());

    @Deprecated //INTERNAL USE ONLY!
    public static void init(Plugin plugin, Controllable controllable) {
        NPCManager.init(() -> new BukkitNPCManager(plugin, controllable));
    }

    protected BukkitNPCManager(Plugin plugin, Controllable controllable) {
        super(controllable);
        controllable.child().postEnable(() -> new VisualsTouchListener<>(BukkitNPCManager.this, plugin));
    }

    public void onPlayerMove(SPlayerMoveEvent event) {
        if (getActiveNPCS().isEmpty()) {
            return;
        }

        final var player = event.getPlayer();

        getActiveNPCS().values().forEach(npc -> {
            if (npc.isShown() && npc.shouldLookAtPlayer() && npc.getViewers().contains(player)) {
                npc.lookAtPlayer(event.getNewLocation(), player);
            }
        });
    }

    @Override
    public void fireVisualTouchEvent(PlayerWrapper sender, NPC visual, Object packet) {
        final var nmsEnum = Reflect.getField(packet, ServerboundInteractPacketAccessor.getFieldAction());
        final var attackEnum = Reflect.getField(ServerboundInteractPacket_i_ActionTypeAccessor.getFieldATTACK());

        NPCInteractEvent.InteractType interactType = (nmsEnum == attackEnum  || nmsEnum == ATTACK_ACTION_FIELD || (nmsEnum != null && attackEnum.toString().equals("ATTACK")))
                ?  NPCInteractEvent.InteractType.LEFT_CLICK : NPCInteractEvent.InteractType.RIGHT_CLICK;

        Tasker.build(() -> EventManager.fire(new NPCInteractEvent(sender, visual, interactType))).afterOneTick().start();
    }

    @Override
    public NPC createVisual(UUID uuid, LocationHolder holder, boolean touchable) {
        return new BukkitNPC(uuid, holder, touchable);
    }
}
