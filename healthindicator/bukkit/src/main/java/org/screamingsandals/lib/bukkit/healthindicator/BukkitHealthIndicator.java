package org.screamingsandals.lib.bukkit.healthindicator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.healthindicator.AbstractHealthIndicator;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

public class BukkitHealthIndicator extends AbstractHealthIndicator {
    private final String objectiveKey;
    private final ConcurrentSkipListMap<String, Integer> values = new ConcurrentSkipListMap<>();

    public BukkitHealthIndicator(UUID uuid) {
        super(uuid);
        this.objectiveKey = new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(16)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (visible) {
            var bukkitPlayer = player.as(Player.class);
            ClassStorage.sendPacket(bukkitPlayer, createObjective());
            ClassStorage.sendPacket(bukkitPlayer, displayObjective());

            values.forEach((s, integer) -> ClassStorage.sendPacket(bukkitPlayer, createScorePacket(s, integer)));
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        var bukkitPlayer = player.as(Player.class);
        ClassStorage.sendPacket(bukkitPlayer, destroyObjective());
    }

    @Override
    protected void update0() {
        if (visible) {
            var packets = new ArrayList<>();

            var trackedPlayers = List.copyOf(this.trackedPlayers);

            List.copyOf(values.keySet()).stream().filter(s -> trackedPlayers.stream().noneMatch(p -> p.getName().equals(s))).forEach(s -> {
                values.remove(s);
                packets.add(destroyScore(s));
            });

            trackedPlayers.forEach(playerWrapper -> {
                if (!playerWrapper.isOnline()) {
                    removeViewer(playerWrapper);
                    return;
                }

                var health = (int) Math.round(playerWrapper.as(EntityHuman.class).getHealth());
                var key = playerWrapper.getName();
                if (!values.containsKey(key) || values.get(key) != health) {
                    values.put(key, health);
                    packets.add(createScorePacket(key, health));
                }
            });

            viewers.forEach(player -> ClassStorage.sendPackets(player.as(Player.class), packets));
        }
    }

    @Override
    protected void updateSymbol0() {
        if (visible) {
            var packet = updateObjective();
            viewers.forEach(player -> ClassStorage.sendPacket(player.as(Player.class), packet));
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        values.clear();
    }

    private InvocationResult notFinalObjectivePacket() {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
        packet.setField("a", objectiveKey);
        if (packet.setField("b", asMinecraftComponent(symbol.asComponent())) == null) {
            packet.setField("b", AdventureHelper.toLegacy(symbol.asComponent()));
        }
        packet.setField("c", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardHealthDisplay, "INTEGER"));
        return packet;
    }

    private Object createObjective() {
        var packet = notFinalObjectivePacket();
        packet.setField("d", 0);
        return packet.raw();
    }

    private Object updateObjective() {
        var packet = notFinalObjectivePacket();
        packet.setField("d", 2);
        return packet.raw();
    }

    private Object destroyObjective() {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
        packet.setField("a", objectiveKey);
        packet.setField("d", 1);
        return packet.raw();
    }

    private Object displayObjective() {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardDisplayObjective);
        packet.setField("a", 2);
        packet.setField("b", objectiveKey);
        return packet.raw();
    }

    private Object createScorePacket(String key, int value) {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardScore);
        packet.setField("a", key);
        packet.setField("b", objectiveKey);
        packet.setField("c", value);
        packet.setField("d", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardAction, "CHANGE"));
        return packet.raw();
    }

    private Object destroyScore(String key) {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardScore);
        packet.setField("a", key);
        packet.setField("b", objectiveKey);
        packet.setField("d", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardAction, "REMOVE"));
        return packet.raw();
    }

    public static Object asMinecraftComponent(Component component) {
        try {
            return MinecraftComponentSerializer.get().serialize(component);
        } catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
            return Reflect.getMethod(ClassStorage.NMS.ChatSerializer, "a,field_150700_a", String.class)
                    .invokeStatic(GsonComponentSerializer.gson().serialize(component));
        }
    }
}
