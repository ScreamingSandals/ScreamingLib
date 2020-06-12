package org.screamingsandals.lib.gamecore.config;

import org.screamingsandals.lib.config.SpigotConfigAdapter;

import java.io.File;

/**
 * Default configuration for {@link org.screamingsandals.lib.gamecore.visuals.VisualsManager}
 */
public abstract class VisualsConfig extends SpigotConfigAdapter {
    //SCOREBOARDS
    public static String PATH_SCOREBOARDS_ENABLED = "scoreboards.enabled";
    public static String PATH_SCOREBOARDS_NAME = "scoreboards.name";
    public static String PATH_SCOREBOARDS_CUSTOM_ENABLED = "scoreboards.custom.enabled";
    public static String PATH_SCOREBOARDS_CONTENT = "scoreboards.content.";
    public static String PATH_SCOREBOARDS_CONTENT_LOBBY = "scoreboards.content.lobby";
    public static String PATH_SCOREBOARDS_CONTENT_STARTING = "scoreboards.content.starting";
    public static String PATH_SCOREBOARDS_CONTENT_IN_GAME = "scoreboards.content.game";
    public static String PATH_SCOREBOARDS_CONTENT_DEATHMATCH = "scoreboards.content.deathmatch";
    public static String PATH_SCOREBOARDS_CONTENT_END_GAME = "scoreboards.content.end-game";
    //SCOREBOARDS

    //BOSSBARS
    public static String PATH_BOSSBARS_ENABLED = "bossbars.enabled";
    public static String PATH_BOSSBARS_CUSTOM_ENABLED = "bossbars.custom.enabled";
    public static String PATH_BOSSBARS_CONTENT = "bossbars.content.";
    public static String PATH_BOSSBARS_CONTENT_LOBBY = "bossbars.content.lobby";
    public static String PATH_BOSSBARS_CONTENT_STARTING = "bossbars.content.starting";
    public static String PATH_BOSSBARS_CONTENT_IN_GAME = "bossbars.content.game";
    public static String PATH_BOSSBARS_CONTENT_DEATHMATCH = "bossbars.content.deathmatch";
    public static String PATH_BOSSBARS_CONTENT_END_GAME = "bossbars.content.end-game";

    public static String PATH_BOSSBARS_COLOR = "bossbars.color.";
    public static String PATH_BOSSBARS_COLOR_LOBBY = "bossbars.color.lobby";
    public static String PATH_BOSSBARS_COLOR_STARTING = "bossbars.color.starting";
    public static String PATH_BOSSBARS_COLOR_IN_GAME = "bossbars.color.game";
    public static String PATH_BOSSBARS_COLOR_DEATHMATCH = "bossbars.color.deathmatch";
    public static String PATH_BOSSBARS_COLOR_END_GAME = "bossbars.color.end-game";
    //BOSSBARS

    //TITLES
    public static String PATH_TITLES_ENABLED = "titles.enabled";
    public static String PATH_TITLES_LOBBY_FADE_IN = "titles.lobby.fade-in";
    public static String PATH_TITLES_LOBBY_FADE_OUT = "titles.lobby.fade-out";
    public static String PATH_TITLES_LOBBY_STAY = "titles.lobby.stay";
    public static String PATH_TITLES_LOBBY_TITLE = "titles.lobby.title";
    public static String PATH_TITLES_LOBBY_SUBTITLE = "titles.lobby.subtitle";

    public static String PATH_TITLES_IN_GAME_FADE_IN = "titles.game.fade-in";
    public static String PATH_TITLES_IN_GAME_FADE_OUT = "titles.game.fade-out";
    public static String PATH_TITLES_IN_GAME_STAY = "titles.game.stay";
    public static String PATH_TITLES_IN_GAME_TITLE = "titles.game.title";
    public static String PATH_TITLES_IN_GAME_SUBTITLE = "titles.game.subtitle";

    public static String PATH_TITLES_DEATHMATCH_FADE_IN = "titles.deathmatch.fade-in";
    public static String PATH_TITLES_DEATHMATCH_FADE_OUT = "titles.deathmatch.fade-out";
    public static String PATH_TITLES_DEATHMATCH_STAY = "titles.deathmatch.stay";
    public static String PATH_TITLES_DEATHMATCH_TITLE = "titles.deathmatch.title";
    public static String PATH_TITLES_DEATHMATCH_SUBTITLE = "titles.deathmatch.subtitle";

    public static String PATH_TITLES_END_GAME_FADE_IN = "titles.end-game.fade-in";
    public static String PATH_TITLES_END_GAME_FADE_OUT = "titles.end-game.fade-out";
    public static String PATH_TITLES_END_GAME_STAY = "titles.end-game.stay";
    public static String PATH_TITLES_END_GAME_TITLE = "titles.end-game.title";
    public static String PATH_TITLES_END_GAME_SUBTITLE = "titles.end-game.subtitle";
    //TITLES

    /**
     * @param configFile configuration file
     */
    public VisualsConfig(File configFile) {
        super(configFile);
    }
}