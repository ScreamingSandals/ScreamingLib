package org.screamingsandals.lib.gamecore;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.config.custom.ScreamingConfig;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.config.CoreConfig;
import org.screamingsandals.lib.gamecore.config.GameConfig;
import org.screamingsandals.lib.gamecore.config.VisualsConfig;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameManager;
import org.screamingsandals.lib.gamecore.core.cycle.CycleType;
import org.screamingsandals.lib.gamecore.core.entities.EntityManager;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorManager;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.events.core.SCoreLoadedEvent;
import org.screamingsandals.lib.gamecore.events.core.SCoreUnloadedEvent;
import org.screamingsandals.lib.gamecore.exceptions.GameCoreException;
import org.screamingsandals.lib.gamecore.language.GameLanguage;
import org.screamingsandals.lib.gamecore.listeners.player.PlayerListener;
import org.screamingsandals.lib.gamecore.player.PlayerManager;
import org.screamingsandals.lib.gamecore.visuals.holograms.HologramManager;
import org.screamingsandals.lib.gamecore.world.regeneration.LegacyWorldRegeneration;
import org.screamingsandals.lib.gamecore.world.regeneration.Regenerable;
import org.screamingsandals.lib.gamecore.world.regeneration.WorldRegeneration;
import org.screamingsandals.lib.tasker.Tasker;

import java.io.File;
import java.util.Objects;

@Data
public class GameCore {
    private static GameCore instance;
    private final Plugin plugin;
    private final Tasker tasker;
    private final ErrorManager errorManager;
    private final PlayerManager playerManager;
    private final EntityManager entityManager;
    private final HologramManager hologramManager;
    private final GameLanguage gameLanguage;
    private VisualsConfig visualsConfig;
    private GameManager<?> gameManager;
    private ScreamingConfig screamingConfig;

    public GameCore(Plugin plugin, GameLanguage gameLanguage, ScreamingConfig screamingConfig) {
        this.plugin = plugin;
        instance = this;

        //init game language in case we don't have any provided by the actual game
        this.gameLanguage = Objects.requireNonNullElseGet(gameLanguage, () -> new GameLanguage(plugin, "en", "&aGame&eCore"));
        this.screamingConfig = Objects.requireNonNullElseGet(screamingConfig, GameConfig::new);

        tasker = Tasker.getSpigot(plugin);
        errorManager = new ErrorManager();
        playerManager = new PlayerManager();
        entityManager = new EntityManager();
        hologramManager = new HologramManager(plugin);

        Debug.setFallbackName("GameCore-" + plugin.getName());
    }


    public <T extends GameFrame> void load(File gamesFolder, Class<T> tClass, CycleType cycleType) throws GameCoreException {
        try {
            this.gameManager = new GameManager<>(gamesFolder, tClass, cycleType);
            registerListeners();
            fireEvent(new SCoreLoadedEvent(this));
        } catch (Exception exception) {
            final var entry = errorManager.newError(new BaseError(ErrorType.GAME_CORE_ERROR, exception));
            throw new GameCoreException(entry.getMessage());
        }
    }

    public void destroy() {
        entityManager.unregisterAll();
        gameManager.unregisterAll();
        tasker.destroy();
        errorManager.destroy();
        hologramManager.destroy();

        plugin.getServer().getServicesManager().unregisterAll(plugin);

        fireEvent(new SCoreUnloadedEvent(this));
    }

    public void reload() {
        gameManager.getRegisteredGames().forEach(GameFrame::stop);

        tasker.destroy();
        errorManager.destroy();
        entityManager.unregisterAll();
        hologramManager.destroy();

        gameManager.getRegisteredGames().forEach(GameFrame::start);
    }

    private void registerListeners() {
        registerListener(new PlayerListener());
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

    public static Tasker getTasker() {
        return instance.tasker;
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

    public static Regenerable getRegenerator() {
        if (PaperLib.isVersion(13)) {
            return new WorldRegeneration();
        } else {
            return new LegacyWorldRegeneration();
        }
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

    public boolean isVerbose() {
        return screamingConfig.get(CoreConfig.DefaultKeys.VERBOSE, true).getBoolean();
    }

    public String getMainCommandName() {
        return screamingConfig.get(CoreConfig.DefaultKeys.MAIN_COMMAND_NAME, "gc").getString();
    }

    public String getAdminPermissions() {
        return screamingConfig.get(CoreConfig.DefaultKeys.ADMIN_PERMISSIONS, "gamecore.admin").getString();
    }
}
