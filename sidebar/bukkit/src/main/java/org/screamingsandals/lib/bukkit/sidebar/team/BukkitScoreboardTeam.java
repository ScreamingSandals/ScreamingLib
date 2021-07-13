package org.screamingsandals.lib.bukkit.sidebar.team;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardTeam;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.sidebar.TeamedSidebar;
import org.screamingsandals.lib.sidebar.team.AbstractScoreboardTeam;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BukkitScoreboardTeam extends AbstractScoreboardTeam {
    private final TeamedSidebar<?> scoreboard;
    private final Component teamKey;

    public BukkitScoreboardTeam(TeamedSidebar<?> scoreboard, String identifier) {
        super(identifier);
        this.scoreboard = scoreboard;
        this.teamKey = Component.text(
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString()
        );
    }

    @Override
    protected void updateInfo() {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = getNotFinalScoreboardTeamPacket(SPacketPlayOutScoreboardTeam.Mode.UPDATE);
            packInfo(packet);
            scoreboard.getViewers().forEach(packet::sendPacket);
        }
    }

    @Override
    protected void sendAddPlayer(PlayerWrapper player) {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = getNotFinalScoreboardTeamPacket(SPacketPlayOutScoreboardTeam.Mode.ADD_ENTITY);
            packPlayers(packet, List.of(player));
            scoreboard.getViewers().forEach(packet::sendPacket);
        }
    }

    @Override
    protected void sendRemovePlayer(PlayerWrapper player) {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = getNotFinalScoreboardTeamPacket(SPacketPlayOutScoreboardTeam.Mode.REMOVE_ENTITY);
            packPlayers(packet, List.of(player));
            scoreboard.getViewers().forEach(packet::sendPacket);
        }
    }

    @Override
    public void destroy() {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = constructDestructPacket();
            scoreboard.getViewers().forEach(packet::sendPacket);
        }
    }

    public SPacketPlayOutScoreboardTeam constructDestructPacket() {
        return getNotFinalScoreboardTeamPacket(SPacketPlayOutScoreboardTeam.Mode.REMOVE);
    }

    private SPacketPlayOutScoreboardTeam getNotFinalScoreboardTeamPacket(SPacketPlayOutScoreboardTeam.Mode mode) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardTeam.class)
                .setTeamName(teamKey)
                .setMode(mode);
    }

    public SPacketPlayOutScoreboardTeam constructCreatePacket() {
        var packet = getNotFinalScoreboardTeamPacket(SPacketPlayOutScoreboardTeam.Mode.CREATE);
        packInfo(packet);
        packPlayers(packet, players);
        return packet;
    }

    private void packInfo(SPacketPlayOutScoreboardTeam packet) {
        packet.setDisplayName(displayName);
        packet.setFlags(friendlyFire, seeInvisible);
        packet.setTagVisibility(nameTagVisibility);
        packet.setCollisionRule(collisionRule);
        packet.setTeamColor(color);
        packet.setTeamPrefix(teamPrefix);
        packet.setTeamSuffix(teamSuffix);
    }

    private void packPlayers(SPacketPlayOutScoreboardTeam packet, List<PlayerWrapper> players) {
        packet.setEntities(players.stream().map(SenderWrapper::getName).collect(Collectors.toList()));
    }
}
