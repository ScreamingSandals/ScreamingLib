package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.adapter.WorldAdapter;
import org.screamingsandals.lib.gamecore.resources.ResourceSpawner;
import org.screamingsandals.lib.gamecore.resources.SpawnerEditor;
import org.screamingsandals.lib.gamecore.resources.SpawnerHologramHandler;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.utils.GameUtils;
import org.screamingsandals.lib.gamecore.visuals.holograms.HologramType;
import org.screamingsandals.lib.gamecore.world.BaseWorld;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;
import org.screamingsandals.lib.lang.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;
import static org.screamingsandals.lib.lang.I.mpr;

@Data
public abstract class GameBuilder<T extends GameFrame> {
    protected T gameFrame;
    protected String gameName;
    protected SpawnerEditor spawnerEditor;
    protected StoreListener storeListener;

    public boolean create(String gameName, Player player) {
        if (GameCore.getGameManager().isGameRegistered(gameName)) {
            mpr("core.errors.game-already-created").send(player);
            return false;
        }

        this.gameName = gameName;
        return true;
    }

    public void load(T gameFrame, Player player) {
        if (gameFrame == null) {
            mpr("core.errors.game-does-not-exists").send(player);
        }
        this.gameFrame = gameFrame;
    }

    public void save(Player player) {
        gameFrame.countMaxPlayers();
    }

    public boolean isCreated() {
        return gameFrame != null;
    }

    public boolean isReadyToSave(boolean fireError) {
        return gameFrame.checkIntegrity(fireError)
                && spawnerEditor == null;
    }

    public void setDisplayName(String displayName) {
        gameFrame.setDisplayedName(displayName);
    }

    public void setGameWorld(GameWorld gameWorld) {
        gameFrame.setGameWorld(gameWorld);
    }

    public void setLobbyWorld(LobbyWorld lobbyWorld) {
        gameFrame.setLobbyWorld(lobbyWorld);
    }

    public void setMinPlayers(int minPlayers) {
        gameFrame.setMinPlayers(minPlayers);
    }

    public void setMinPlayersToStart(int minPlayers) {
        gameFrame.setMinPlayersToStart(minPlayers);
    }

    public void setGameTime(int gameTime) {
        gameFrame.setGameTime(gameTime);
    }

    public void setLobbyTime(int lobbyTime) {
        gameFrame.setLobbyTime(lobbyTime);
    }

    public void setStartTime(int startTime) {
        gameFrame.setStartTime(startTime);
    }

    public void setEndTime(int endTime) {
        gameFrame.setEndTime(endTime);
    }

    public void setDeathmatchTime(int deathmatchTime) {
        gameFrame.setDeathmatchTime(deathmatchTime);
    }

    public void addTeam(GameTeam gameTeam) {
        gameFrame.getTeams().add(gameTeam);
    }

    public void addSpawner(ResourceSpawner resourceSpawner, Player player) {
        gameFrame.getResourceManager().register(resourceSpawner);

        buildHologram(resourceSpawner, gameFrame, player);
    }

    public void addStore(GameStore gameStore) {
        gameFrame.getStores().add(gameStore);
    }

    public void removeTeam(String teamName) {
        gameFrame.getTeams().removeIf(gameTeam -> gameTeam.getTeamName().equalsIgnoreCase(teamName));
    }

    public void removeTeam(GameTeam gameTeam) {
        gameFrame.getTeams().removeIf(gameTeam1 -> gameTeam1.isSame(gameTeam));
    }

    public void setGameWorld(String worldName) {
        var gameWorld = gameFrame.getGameWorld();
        if (gameWorld == null) {
            gameWorld = new GameWorld(worldName);
        }

        gameWorld.setWorldAdapter(new WorldAdapter(worldName));
    }

    public void setGameWorldBorder(Location location, int whichOne) {
        final var adapter = new LocationAdapter(location);
        var gameWorld = gameFrame.getGameWorld();

        if (gameWorld == null) {
            gameWorld = new GameWorld(location.getWorld().getName());
            gameFrame.setGameWorld(gameWorld);
        }

        setWorldBorder(adapter, gameWorld, whichOne);
    }

    public void setLobbyWorldBorder(Location location, int whichOne) {
        final var adapter = new LocationAdapter(location);
        var lobbyWorld = gameFrame.getLobbyWorld();

        if (lobbyWorld == null) {
            lobbyWorld = new LobbyWorld(location.getWorld().getName());
            gameFrame.setLobbyWorld(lobbyWorld);
        }

        setWorldBorder(adapter, lobbyWorld, whichOne);
    }

    public boolean setSpectatorsSpawn(Player player) {
        final var location = player.getLocation();
        final var adapter = gameFrame.getGameWorld();

        if (adapter == null) {
            mpr("game.core.errors.game-world-does-not-exists").send(player);
            return false;
        }

        if (!location.getWorld().getName().equals(location.getWorld().getName())) {
            mpr("general.errors.different-world").send(player);
            return false;
        }

        if (!isLocationInsideGame(location)) {
            mpr("general.errors.outside-of-the-border").send(player);
            return false;
        }

        adapter.setSpectatorSpawn(new LocationAdapter(location));
        return true;
    }

    public boolean setLobbySpawn(Player player) {
        final var location = player.getLocation();
        final var adapter = gameFrame.getLobbyWorld();

        if (adapter == null) {
            mpr("game.core.errors.lobby-world-does-not-exists").send(player);
            return false;
        }

        if (!location.getWorld().getName().equals(location.getWorld().getName())) {
            mpr("general.errors.different-world").send(player);
            return false;
        }

        if (!GameUtils.isInGameBorder(location, adapter.getBorder1().getLocation(), adapter.getBorder2().getLocation())) {
            mpr("general.errors.outside-of-the-border").send(player);
            return false;
        }

        adapter.setSpawn(new LocationAdapter(location));
        return true;
    }

    public boolean isLocationInsideGame(Location location) {
        final var gameWorld = gameFrame.getGameWorld();

        if (gameWorld == null) {
            return false;
        }

        final var border1 = gameWorld.getBorder1();
        final var border2 = gameWorld.getBorder2();

        if (border1 == null || border2 == null) {
            return false;
        }

        return GameUtils.isInGameBorder(location, border1.getLocation(), border2.getLocation());
    }

    public void buildHologram(ResourceSpawner spawner, GameFrame currentGame, Player player) {
        final List<String> lines = new ArrayList<>();
        var period = spawner.getPeriod();
        final var timeUnit = spawner.getGameTimeUnit();
        final var team = spawner.getGameTeam();
        final var maxSpawned = spawner.getMaxSpawned();

        lines.add(Utils.colorize("&a&lGameBuilder"));
        lines.addAll(m("game-builder.spawners.hologram")
                .replace("%color%", spawner.getType().getChatColor())
                .replace("%type%", spawner.getType().getTranslatedName())
                .replace("%mat%", spawner.getType().getMaterial())
                .replace("%spawnAmount%", spawner.getAmount())
                .replace("%time-unit%", period + " " + GameUtils.convertTimeUnitToLanguage(period, timeUnit))
                .replace("%team%", team == null ? GameUtils.convertNullToLanguage() : team.getTeamName())
                .replace("%amount%", maxSpawned == -1 ? GameUtils.getInfinityLanguage() : maxSpawned)
                .replace("%booleanValue%", spawner.isHologram())
                .getList());

        final var gameHologram = GameCore.getHologramManager()
                .spawnTouchableHologram(currentGame, HologramType.BUILDER_SPAWNER, player, spawner.getLocation().getLocation(), lines);
        gameHologram.setUuid(spawner.getUuid());
        gameHologram.setHandler(new SpawnerHologramHandler());
    }

    private void setWorldBorder(LocationAdapter adapter, BaseWorld baseWorld, int whichOne) {
        if (baseWorld.getWorldAdapter() == null) {
            baseWorld.setWorldAdapter(new WorldAdapter(adapter.getWorld().toString()));
        }

        switch (whichOne) {
            case 1: {
                baseWorld.setBorder1(adapter);
                break;
            }
            case 2: {
                baseWorld.setBorder2(adapter);
                break;
            }
            default: {
                Debug.info("Not possible. Report to developers on GitHub!", true);
            }
        }
    }

    public boolean isTeamExists(String teamName) {
        for (var gameTeam : gameFrame.getTeams()) {
            if (gameTeam.getTeamName().equalsIgnoreCase(teamName)) {
                return true;
            }
        }
        return false;
    }

    @Data
    public static class StoreListener implements Listener {
        private final GameFrame gameFrame;

        @EventHandler
        public void onRightClick(PlayerInteractEntityEvent event) {
            var rightClicked = event.getRightClicked();

            if (GameCore.getEntityManager().isRegisteredInGame(gameFrame.getUuid(), rightClicked)) {
                event.setCancelled(true);
                //TODO: open shop
            }
        }

        @EventHandler
        public void onAttack(EntityDamageByEntityEvent event) {
            var who = event.getDamager();
            var entity = event.getEntity();

            if (!(who instanceof Player)) {
                return;
            }

            if (GameCore.getEntityManager().isRegisteredInGame(gameFrame.getUuid(), entity)) {
                event.setCancelled(true);
                mpr("game-builder.store.why-are-you-punching-your-shop").send(who);
            }
        }
    }
}
