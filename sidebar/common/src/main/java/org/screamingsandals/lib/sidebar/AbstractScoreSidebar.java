package org.screamingsandals.lib.sidebar;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractVisual;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractScoreSidebar extends AbstractVisual<ScoreSidebar> implements ScoreSidebar {
    @Getter
    protected final List<ScoreboardTeam> teams = new LinkedList<>();
    @Getter
    protected final List<ScoreEntry> entries = new CopyOnWriteArrayList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;
    protected Component title = Component.empty();

    public AbstractScoreSidebar(UUID uuid) {
        super(uuid);
    }

    @Override
    public Optional<ScoreboardTeam> getTeam(String identifier) {
        return teams.stream().filter(scoreboardTeam -> identifier.equals(scoreboardTeam.identifier())).findFirst();
    }

    @Override
    public ScoreSidebar removeTeam(String identifier) {
        getTeam(identifier).ifPresent(this::removeTeam);
        return this;
    }

    @Override
    public ScoreSidebar removeTeam(ScoreboardTeam scoreboardTeam) {
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
    public ScoreSidebar title(Component title) {
        this.title = title;
        updateTitle0();
        return this;
    }

    @Override
    public ScoreSidebar entity(String identifier, Component displayName) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresentOrElse(scoreEntry -> {
                    scoreEntry.setComponent(displayName);
                    scoreEntry.setReloadCache(true);
                    update();
                }, () -> {
                    var scoreEntry = new ScoreEntry(identifier);
                    scoreEntry.setComponent(displayName);
                    entries.add(scoreEntry);
                    update();
                });
        return this;
    }

    @Override
    public ScoreSidebar score(String identifier, int score) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresent(scoreEntry -> {
                    scoreEntry.setScore(score);
                    update();
                });
        return this;
    }

    @Override
    public ScoreSidebar removeEntity(String identifier) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresent(scoreEntry -> {
                    entries.remove(scoreEntry);
                    update();
                });
        return this;
    }

    @Override
    public ScoreSidebar update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public ScoreSidebar show() {
        if (isShown()) {
            return this;
        }

        ready = true;
        visible = true;
        viewers.forEach(a -> onViewerAdded(a, false));
        update();
        return this;
    }

    @Override
    public ScoreSidebar hide() {
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

        SidebarManager.removeSidebar(this);
    }

    protected abstract void updateTitle0();

    @Data
    public static class ScoreEntry {
        private final String identifier;
        private Component component;
        private int score;
        private String cache;
        private boolean reloadCache;
    }
}
