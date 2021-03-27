package org.screamingsandals.lib.scoreboard;

import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.scoreboard.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractScoreboard extends AbstractLinedVisual<Scoreboard> implements Scoreboard {
    @Getter
    protected final List<ScoreboardTeam> teams = new LinkedList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;

    public AbstractScoreboard(UUID uuid) {
        super(uuid);
    }

    @Override
    public Optional<ScoreboardTeam> getTeam(String identifier) {
        return teams.stream().filter(scoreboardTeam -> identifier.equals(scoreboardTeam.identifier())).findFirst();
    }

    @Override
    public Scoreboard removeTeam(String identifier) {
        getTeam(identifier).ifPresent(this::removeTeam);
        return this;
    }

    @Override
    public Scoreboard removeTeam(ScoreboardTeam scoreboardTeam) {
        scoreboardTeam.destroy();
        teams.remove(scoreboardTeam);
        return this;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }

        return !data.isEmpty();
    }


    @Override
    public Scoreboard update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public Scoreboard show() {
        if (isShown()) {
            return this;
        }

        ready = true;
        visible = true;
        update();
        return this;
    }

    @Override
    public Scoreboard hide() {
        if (!isShown()) {
            return this;
        }

        visible = false;
        ready = false;
        update();
        return this;
    }

    @Override
    public void destroy() {
        data = null;
        hide();
        viewers.clear();

        ScoreboardManager.removeScoreboard(this);
    }
}
