package org.screamingsandals.lib.bukkit.sidebar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.sidebar.team.BukkitScoreboardTeam;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sidebar.AbstractSidebar;
import org.screamingsandals.lib.sidebar.Sidebar;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class BukkitSidebar extends AbstractSidebar {
    private final String objectiveKey;
    private final ConcurrentSkipListMap<Integer, String> lines = new ConcurrentSkipListMap<>();

    public BukkitSidebar(UUID uuid) {
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
            ClassStorage.sendPackets(bukkitPlayer, allScores());
            ClassStorage.sendPacket(bukkitPlayer, displayObjective());
            teams.forEach(scoreboardTeam ->
                ClassStorage.sendPacket(bukkitPlayer, ((BukkitScoreboardTeam) scoreboardTeam).constructCreatePacket())
            );
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            var bukkitPlayer = player.as(Player.class);
            teams.forEach(scoreboardTeam ->
                    ClassStorage.sendPacket(bukkitPlayer, ((BukkitScoreboardTeam) scoreboardTeam).constructDestructPacket())
            );
            ClassStorage.sendPacket(bukkitPlayer, destroyObjective());
        }
    }

    @Override
    protected void update0() {
        var list = getLines()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= 0 && entry.getKey() <= 15)
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().getText())
                .map(AdventureHelper::toLegacy)
                .collect(Collectors.toList());

        Collections.reverse(list);

        for (var i = 0; i < list.size(); i++) {
            list.set(i, makeUnique(list.get(i), list));
        }

        var packets = new ArrayList<>();
        var forRemoval = new ArrayList<Integer>();

        for (var i = 0; i < 15; i++) {
            if (i < list.size()) {
                if (lines.containsKey(i)) {
                    packets.add(destroyScore(lines.get(i)));
                }
                lines.put(i, list.get(i));
                packets.add(createScorePacket(i, list.get(i)));
            } else if (lines.containsKey(i)) {
                packets.add(destroyScore(lines.get(i)));
                forRemoval.add(i);
            }
        }

        forRemoval.forEach(lines::remove);

        if (visible) {
            viewers.forEach(playerWrapper -> ClassStorage.sendPackets(playerWrapper.as(Player.class), packets));
        }
    }

    @Override
    public void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            var packet = updateObjective();
            viewers.forEach(p -> ClassStorage.sendPacket(p.as(Player.class), packet));
        }
    }

    // INTERNAL METHODS

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

    private InvocationResult notFinalObjectivePacket() {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
        packet.setField("a", objectiveKey);
        if (packet.setField("b", asMinecraftComponent(title)) == null) {
            packet.setField("b", AdventureHelper.toLegacy(title));
        }
        packet.setField("c", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardHealthDisplay, "INTEGER"));
        return packet;
    }

    private Object destroyObjective() {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
        packet.setField("a", objectiveKey);
        packet.setField("d", 1);
        return packet.raw();
    }

    private Object displayObjective() {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardDisplayObjective);
        packet.setField("a", 1);
        packet.setField("b", objectiveKey);
        return packet.raw();
    }

    private Object createScorePacket(int i, String value) {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardScore);
        packet.setField("a", value);
        packet.setField("b", objectiveKey);
        packet.setField("c", i);
        packet.setField("d", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardAction, "CHANGE"));
        return packet.raw();
    }

    private Object destroyScore(String value) {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardScore);
        packet.setField("a", value);
        packet.setField("b", objectiveKey);
        packet.setField("d", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardAction, "REMOVE"));
        return packet.raw();
    }

    private List<Object> allScores() {
        return lines.entrySet()
                .stream()
                .map(entry -> createScorePacket(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public String makeUnique(String toUnique, List<String> from) {
        if (toUnique == null) toUnique = " ";
        final var stringBuilder = new StringBuilder(toUnique);
        while (from.contains(stringBuilder.toString())) {
            stringBuilder.append(" ");
        }

        if (stringBuilder.length() > 40) {
            return stringBuilder.substring(0, 40);
        }
        return stringBuilder.toString();
    }

    public static Object asMinecraftComponent(Component component) {
        try {
            return MinecraftComponentSerializer.get().serialize(component);
        } catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
            return Reflect.getMethod(ClassStorage.NMS.ChatSerializer, "a,field_150700_a", String.class)
                    .invokeStatic(GsonComponentSerializer.gson().serialize(component));
        }
    }

    @Override
    public ScoreboardTeam team(String identifier) {
        var team = new BukkitScoreboardTeam(this, identifier);
        teams.add(team);
        if (visible && !viewers.isEmpty()) {
            var packet = team.constructCreatePacket();
            viewers.forEach(player -> ClassStorage.sendPacket(player.as(Player.class), packet));
        }
        return team;
    }
}
