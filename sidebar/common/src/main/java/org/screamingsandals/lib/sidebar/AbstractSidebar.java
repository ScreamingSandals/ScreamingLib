package org.screamingsandals.lib.sidebar;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSidebar extends AbstractLinedVisual<Sidebar> implements Sidebar {
    @Getter
    protected final List<ScoreboardTeam> teams = new LinkedList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;
    protected SenderMessage title = SenderMessage.empty();

    public AbstractSidebar(UUID uuid) {
        super(uuid);
    }

    @Override
    public Optional<ScoreboardTeam> getTeam(String identifier) {
        return teams.stream().filter(scoreboardTeam -> identifier.equals(scoreboardTeam.identifier())).findFirst();
    }

    @Override
    public Sidebar removeTeam(String identifier) {
        getTeam(identifier).ifPresent(this::removeTeam);
        return this;
    }

    @Override
    public Sidebar removeTeam(ScoreboardTeam scoreboardTeam) {
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
    public Sidebar title(Component title) {
        return title(SenderMessage.of(title));
    }

    @Override
    public Sidebar title(ComponentLike title) {
        if (title instanceof SenderMessage) {
            this.title = (SenderMessage) title;
        } else {
            this.title = SenderMessage.of(title);
        }
        updateTitle0();
        return this;
    }

    @Override
    public Sidebar update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public Sidebar show() {
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
    public Sidebar hide() {
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
}
