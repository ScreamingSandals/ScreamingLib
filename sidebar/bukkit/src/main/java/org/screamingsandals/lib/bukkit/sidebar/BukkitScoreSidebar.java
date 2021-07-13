package org.screamingsandals.lib.bukkit.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.sidebar.team.BukkitScoreboardTeam;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sidebar.AbstractScoreSidebar;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class BukkitScoreSidebar extends AbstractScoreSidebar {
    private final Component objectiveKey;
    private final List<ScoreEntry> lines = new CopyOnWriteArrayList<>();

    public BukkitScoreSidebar(UUID uuid) {
        super(uuid);
        this.objectiveKey = Component.text(
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString()
        );
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (visible) {
            getCreateObjectivePacket(player).sendPacket(player);
            allScores().forEach(packet -> packet.sendPacket(player));
            getDisplayObjectivePacket().sendPacket(player);
            teams.forEach(scoreboardTeam ->
                    ((BukkitScoreboardTeam) scoreboardTeam).constructCreatePacket().sendPacket(player)
            );
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            var bukkitPlayer = player.as(Player.class);
            teams.forEach(scoreboardTeam ->
                    ((BukkitScoreboardTeam) scoreboardTeam).constructDestructPacket().sendPacket(player)
            );
            ClassStorage.sendPacket(bukkitPlayer, getDestroyObjectivePacket());
        }
    }

    @Override
    protected void update0() {
        var list = entries
                .stream()
                .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
                .limit(15)
                .collect(Collectors.toList());

        var packets = new ArrayList<SPacket>();
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
            viewers.forEach(viewer -> packets.forEach(packet -> packet.sendPacket(viewer)));
        }
    }

    @Override
    public void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            viewers.forEach(p -> ClassStorage.sendPacket(p.as(Player.class), getUpdateObjectivePacket(p)));
        }
    }

    // INTERNAL METHODS

    private SPacketPlayOutScoreboardObjective getCreateObjectivePacket(PlayerWrapper player) {
        var packet = notFinalObjectivePacket(player);
        packet.setMode(SPacketPlayOutScoreboardObjective.Mode.CREATE);
        return packet;
    }

    private SPacketPlayOutScoreboardObjective getUpdateObjectivePacket(PlayerWrapper player) {
        var packet = notFinalObjectivePacket(player);
        packet.setMode(SPacketPlayOutScoreboardObjective.Mode.UPDATE);
        return packet;
    }

    private SPacketPlayOutScoreboardObjective notFinalObjectivePacket(PlayerWrapper player) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardObjective.class)
                .setObjectiveKey(objectiveKey)
                .setTitle(title.asComponent(player))
                .setCriteria(SPacketPlayOutScoreboardObjective.Type.INTEGER);
    }

    private SPacketPlayOutScoreboardObjective getDestroyObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardObjective.class)
                .setObjectiveKey(objectiveKey)
                .setMode(SPacketPlayOutScoreboardObjective.Mode.DESTROY);
    }

    private SPacketPlayOutScoreboardDisplayObjective getDisplayObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardDisplayObjective.class)
                .setDisplaySlot(SPacketPlayOutScoreboardDisplayObjective.DisplaySlot.SIDEBAR)
                .setObjectiveKey(objectiveKey);
    }

    private SPacketPlayOutScoreboardScore createScorePacket(int i, String value) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardScore.class)
                .setValue(Component.text(value))
                .setObjectiveKey(objectiveKey)
                .setScore(i)
                .setAction(SPacketPlayOutScoreboardScore.ScoreboardAction.CHANGE);
    }

    private Object destroyScore(String value) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardScore.class)
                .setValue(Component.text(value))
                .setObjectiveKey(objectiveKey)
                .setAction(SPacketPlayOutScoreboardScore.ScoreboardAction.REMOVE);
    }

    private List<SPacketPlayOutScoreboardScore> allScores() {
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
            viewers.forEach(packet::sendPacket);
        }
        return team;
    }
}
