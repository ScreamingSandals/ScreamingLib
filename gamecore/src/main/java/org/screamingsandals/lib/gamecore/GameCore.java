package org.screamingsandals.lib.gamecore;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameManager;
import org.screamingsandals.lib.gamecore.core.entities.EntityManager;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorManager;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.events.core.SCoreLoadedEvent;
import org.screamingsandals.lib.gamecore.events.core.SCoreUnloadedEvent;
import org.screamingsandals.lib.gamecore.exceptions.GameCoreException;
import org.screamingsandals.lib.gamecore.listeners.player.PlayerListener;
import org.screamingsandals.lib.gamecore.player.PlayerManager;
import org.screamingsandals.lib.gamecore.visuals.holograms.HologramManager;
import org.screamingsandals.lib.tasker.Tasker;

import java.io.File;

@Data
public class GameCore {
    private static GameCore instance;
    private final Plugin plugin;
    private final Tasker tasker;
    private final ErrorManager errorManager;
    private final PlayerManager playerManager;
    private final EntityManager entityManager;
    private final HologramManager hologramManager;
    private GameManager<?> gameManager;
    private boolean verbose = true;
    private String mainCommandName = "gc";
    private String adminPermissions = "gamecore.admin";

    public GameCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;

        tasker = Tasker.getSpigot(plugin);
        errorManager = new ErrorManager();
        playerManager = new PlayerManager();
        entityManager = new EntityManager();
        hologramManager = new HologramManager(plugin);

        Debug.setFallbackName("GameCore-" + plugin.getName());
    }

    public GameCore(Plugin plugin, String mainCommandName, String adminPermissions, boolean verbose) {
        this(plugin);
        this.mainCommandName = mainCommandName;
        this.adminPermissions = adminPermissions;
        this.verbose = verbose;
    }

    public <T extends GameFrame> void load(File gamesFolder, Class<T> tClass) throws GameCoreException {
        try {
            this.gameManager = new GameManager<>(gamesFolder, tClass);

            registerListeners();
            registerCoreCommands();

            fireEvent(new SCoreLoadedEvent(this));
        } catch (Exception exception) {
            final var entry = errorManager.newError(new BaseError(ErrorType.GAME_CORE_ERROR, exception));
            throw new GameCoreException(entry.getDefaultMessage());
        }
    }

    public void destroy() {
        gameManager.unregisterAll();
        entityManager.unregisterAll();
        tasker.destroy();
        errorManager.destroy();
        hologramManager.destroy();

        plugin.getServer().getServicesManager().unregisterAll(plugin);

        fireEvent(new SCoreUnloadedEvent(this));
    }

    public void reload() {
        //TODO: reload all important things
        gameManager.getRegisteredGames().forEach(GameFrame::reload);
    }

    private void registerListeners() {
        registerListener(new PlayerListener());
    }

    private void registerCoreCommands() {

    }

    public static void registerListener(Listener listener) {
        instance.plugin.getServer().getPluginManager().registerEvents(listener, instance.plugin);
    }

    public static void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public static GameCore getInstance() {
        return instance;
    }

    public static GameManager<?> getGameManager() {
        return instance.gameManager;
    }

    public static PlayerManager getPlayerManager() {
        return instance.playerManager;
    }

    public static ErrorManager getErrorManager() {
        return instance.errorManager;
    }

    public static EntityManager getEntityManager() {
        return instance.entityManager;
    }

    public static HologramManager getHologramManager() {
        return instance.hologramManager;
    }

    public static Plugin getPlugin() {
        return instance.plugin;
    }

    public static boolean fireEvent(Event event) {
        if (PaperLib.isPaper()) {
            return event.callEvent();
        } else {
            Bukkit.getPluginManager().callEvent(event);
            if (event instanceof Cancellable) {
                return !((Cancellable) event).isCancelled();
            } else {
                return true;
            }
        }
    }
}
