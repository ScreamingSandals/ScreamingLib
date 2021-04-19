package org.screamingsandals.lib.bukkit.sidebar;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.sidebar.team.BukkitScoreboardTeam;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sidebar.AbstractScoreSidebar;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class BukkitScoreSidebar extends AbstractScoreSidebar {
    private final String objectiveKey;
    private final List<ScoreEntry> lines = new CopyOnWriteArrayList<>();

    public BukkitScoreSidebar(UUID uuid) {
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
            ClassStorage.sendPacket(bukkitPlayer, createObjective(player));
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
        var list = entries
                .stream()
                .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
                .limit(15)
                .collect(Collectors.toList());

        var packets = new ArrayList<>();
        var forRemoval = new ArrayList<ScoreEntry>();

        lines.stream().filter(scoreEntry -> !list.contains(scoreEntry)).forEach(forRemoval::add);
        forRemoval.forEach(scoreEntry -> {
            lines.remove(scoreEntry);
            destroyScore(scoreEntry.getCache());
        });

        list.forEach(scoreEntry -> {
            if (!lines.contains(scoreEntry)) {
                lines.add(scoreEntry);
            }
            if (scoreEntry.getCache() == null || scoreEntry.isReloadCache()) {
                scoreEntry.setCache(crop(AdventureHelper.toLegacy(scoreEntry.getComponent())));
                scoreEntry.setReloadCache(false);
            }
            packets.add(createScorePacket(scoreEntry.getScore(), scoreEntry.getCache()));
        });

        if (visible) {
            viewers.forEach(playerWrapper -> ClassStorage.sendPackets(playerWrapper.as(Player.class), packets));
        }
    }

    @Override
    public void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            viewers.forEach(p -> ClassStorage.sendPacket(p.as(Player.class), updateObjective(p)));
        }
    }

    // INTERNAL METHODS

    private Object createObjective(PlayerWrapper player) {
        var packet = notFinalObjectivePacket(player);
        packet.setField("d", 0);
        return packet.raw();
    }

    private Object updateObjective(PlayerWrapper player) {
        var packet = notFinalObjectivePacket(player);
        packet.setField("d", 2);
        return packet.raw();
    }

    private InvocationResult notFinalObjectivePacket(PlayerWrapper player) {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
        packet.setField("a", objectiveKey);
        if (packet.setField("b", BukkitSidebar.asMinecraftComponent(title.asComponent(player))) == null) {
            packet.setField("b", AdventureHelper.toLegacy(title.asComponent(player)));
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
        return lines
                .stream()
                .map(entry -> createScorePacket(entry.getScore(), entry.getCache()))
                .collect(Collectors.toList());
    }

    public String crop(String baseLine) {
        if (baseLine.length() > 40) {
            return baseLine.substring(0, 40);
        }
        return baseLine;
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
