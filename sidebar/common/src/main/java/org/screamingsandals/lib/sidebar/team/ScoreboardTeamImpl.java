package org.screamingsandals.lib.sidebar.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.packet.SClientboundSetPlayerTeamPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.sidebar.TeamedSidebar;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
public class ScoreboardTeamImpl implements ScoreboardTeam {
    protected final TeamedSidebar<?> scoreboard;
    protected final String identifier;
    protected NamedTextColor color = NamedTextColor.WHITE;
    protected Component displayName = Component.empty();
    protected Component teamPrefix = Component.empty();
    protected Component teamSuffix = Component.empty();
    protected boolean friendlyFire = true;
    protected boolean seeInvisible = true;
    protected SClientboundSetPlayerTeamPacket.TagVisibility nameTagVisibility = SClientboundSetPlayerTeamPacket.TagVisibility.ALWAYS;
    protected SClientboundSetPlayerTeamPacket.CollisionRule collisionRule = SClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS;
    protected final List<PlayerWrapper> players = new LinkedList<>();
    private final String teamKey;

    public ScoreboardTeamImpl(TeamedSidebar<?> scoreboard, String identifier) {
        this.scoreboard = scoreboard;
        this.identifier = identifier;
        this.teamKey =
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
    }

    @Override
    public ScoreboardTeam color(NamedTextColor color) {
        this.color = color;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam displayName(Component displayName) {
        this.displayName = displayName;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam teamPrefix(Component teamPrefix) {
        this.teamPrefix = teamPrefix;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam teamSuffix(Component teamSuffix) {
        this.teamSuffix = teamSuffix;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam friendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam seeInvisible(boolean seeInvisible) {
        this.seeInvisible = seeInvisible;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam nameTagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam collisionRule(SClientboundSetPlayerTeamPacket.CollisionRule collisionRule) {
        this.collisionRule = collisionRule;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam player(PlayerWrapper player) {
        if (!players.contains(player)) {
            players.add(player);
            sendAddPlayer(player);
        }
        return this;
    }

    @Override
    public ScoreboardTeam removePlayer(PlayerWrapper player) {
        if (players.contains(player)) {
            players.remove(player);
            sendRemovePlayer(player);
        }
        return this;
    }

    @Override
    public List<PlayerWrapper> players() {
        return List.copyOf(players);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return players();
    }

    public SClientboundSetPlayerTeamPacket constructDestructPacket() {
        return getNotFinalScoreboardTeamPacket(SClientboundSetPlayerTeamPacket.Mode.REMOVE);
    }

    private SClientboundSetPlayerTeamPacket getNotFinalScoreboardTeamPacket(SClientboundSetPlayerTeamPacket.Mode mode) {
        return new SClientboundSetPlayerTeamPacket()
                .teamKey(teamKey)
                .mode(mode);
    }

    public SClientboundSetPlayerTeamPacket constructCreatePacket() {
        var packet = getNotFinalScoreboardTeamPacket(SClientboundSetPlayerTeamPacket.Mode.CREATE);
        packInfo(packet);
        packPlayers(packet, players);
        return packet;
    }

    private void packInfo(SClientboundSetPlayerTeamPacket packet) {
        packet.displayName(displayName)
                .friendlyFire(friendlyFire)
                .seeInvisible(seeInvisible)
                .tagVisibility(nameTagVisibility)
                .collisionRule(collisionRule)
                .teamColor(color)
                .teamPrefix(teamPrefix)
                .teamSuffix(teamSuffix);
    }

    private void packPlayers(SClientboundSetPlayerTeamPacket packet, List<PlayerWrapper> players) {
        packet.entities(players.stream().map(SenderWrapper::getName).collect(Collectors.toList()));
    }

    protected void updateInfo() {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = getNotFinalScoreboardTeamPacket(SClientboundSetPlayerTeamPacket.Mode.UPDATE);
            packInfo(packet);
            scoreboard.getViewers().forEach(packet::sendPacket);
        }
    }

    protected void sendAddPlayer(PlayerWrapper player) {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = getNotFinalScoreboardTeamPacket(SClientboundSetPlayerTeamPacket.Mode.ADD_ENTITY);
            packPlayers(packet, List.of(player));
            scoreboard.getViewers().forEach(packet::sendPacket);
        }
    }

    protected void sendRemovePlayer(PlayerWrapper player) {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = getNotFinalScoreboardTeamPacket(SClientboundSetPlayerTeamPacket.Mode.REMOVE_ENTITY);
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
}
