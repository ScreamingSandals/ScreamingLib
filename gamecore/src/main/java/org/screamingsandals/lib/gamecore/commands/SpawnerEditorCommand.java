package org.screamingsandals.lib.gamecore.commands;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.common.RegisterCommand;
import org.screamingsandals.lib.commands.common.SubCommandBuilder;
import org.screamingsandals.lib.commands.common.interfaces.ScreamingCommand;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.resources.SpawnerEditor;
import org.screamingsandals.lib.gamecore.utils.GameUtils;
import org.screamingsandals.lib.tasker.TaskerTime;

import java.util.LinkedList;
import java.util.List;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;
import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

/**
 * Command responsible for editing {@link org.screamingsandals.lib.gamecore.resources.ResourceSpawner}
 * This is fairly easy, just edit the spawner in-game via {@link org.screamingsandals.lib.gamecore.visuals.holograms.GameHologram},
 * and save it.
 *
 * Spawner editor should only run while game with the spawners is in the EDIT mode!
 */
@RegisterCommand
public class SpawnerEditorCommand implements ScreamingCommand {
    private final List<String> actions = new LinkedList<>();

    @Override
    public void register() {
        final var gameCore = GameCore.getInstance();
        SubCommandBuilder.bukkitSubCommand()
                .createSubCommand(gameCore.getMainCommandName(), "sedit", gameCore.getAdminPermissions(), List.of("spawner-editor", "se"))
                .handleSubPlayerCommand(this::handleCommand)
                .handleSubPlayerTab(this::handleTab);

        actions.addAll(List.of("max-spawned", "amount", "period", "team", "location", "time-unit", "save", "exit"));
    }

    private void handleCommand(Player player, List<String> args) {
        final var size = args.size();
        final var editorFromManager = GameCore.getGameManager().getSpawnerEditor(player);

        if (editorFromManager.isEmpty()) {
            mpr("game-builder.spawners.editor.nothing-active").sendList(player);
            return;
        }

        if (size < 2) {
            m("game-builder.spawners.editor.help").sendList(player);
            return;
        }

        final var spawnerEditor = editorFromManager.get();
        final var spawner = spawnerEditor.getResourceSpawner();

        if (size > 3) {
            mpr("game-builder.spawners.editor.invalid-action").sendList(player);
            return;
        }

        if (size == 3) {
            final var action = args.get(1);
            final var value = args.get(2);

            switch (action) {
                case "max-spawned": {
                    if (GameUtils.canCastToInt(value, player)) {
                        spawner.setMaxSpawned(GameUtils.castToInt(value));
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    }
                    break;
                }
                case "amount": {
                    if (GameUtils.canCastToInt(value, player)) {
                        spawner.setAmount(GameUtils.castToInt(value));
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    }
                    break;
                }
                case "period": {
                    if (GameUtils.canCastToInt(value, player)) {
                        spawner.setPeriod(GameUtils.castToInt(value));
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    }
                    break;
                }
                case "team": {
                    final var game = spawnerEditor.getGameBuilder().getGameFrame();
                    final var team = game.getRegisteredTeam(value);

                    if (team.isPresent()) {
                        spawner.setGameTeam(team.get());
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    } else {
                        mpr("general.errors.invalid-team")
                                .replace("%team%", value)
                                .send(player);
                    }
                    break;
                }
                case "time-unit": {
                    if (canBeTime(value.toLowerCase())) {
                        spawner.setTimeUnit(TaskerTime.valueOf(value.toUpperCase()));
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    } else {
                        mpr("game-builder.spawners.editor.invalid-value")
                                .replace("%value%", value)
                                .send(player);
                    }
                    break;
                }
                default:
                    mpr("game-builder.spawners.editor.invalid-action")
                            .replace("%action%", action)
                            .sendList(player);
            }

            rebuildHologram(spawnerEditor, player, false);
            return;
        }

        if (args.size() == 2) {
            final var command = args.get(1).toLowerCase();

            switch (command) {
                case "exit": {
                    spawnerEditor.end();
                    mpr("game-builder.spawners.editor.exit").send(player);
                    rebuildHologram(spawnerEditor, player, true);
                    return;
                }
                case "save": {
                    spawnerEditor.save();
                    mpr("game-builder.spawners.editor.save").send(player);
                    rebuildHologram(spawnerEditor, player, false);
                    return;
                }
                case "location": {
                    spawner.setLocation(new LocationAdapter(player.getLocation()));
                    mpr("game-builder.spawners.editor.location").send(player);
                    rebuildHologram(spawnerEditor, player, false);
                    return;
                }
                default: {
                    mpr("game-builder.spawners.editor.invalid-action").sendList(player);
                }
            }

        }
    }

    private List<String> handleTab(Player player, List<String> args) {
        final List<String> toReturn = new LinkedList<>();
        final var editorFromManager = GameCore.getGameManager().getSpawnerEditor(player);

        if (editorFromManager.isEmpty() || args.size() < 2) {
            return toReturn;
        }

        final var spawnerEditor = editorFromManager.get();
        final var currentGame = spawnerEditor.getGameBuilder().getGameFrame();
        final var typed = args.get(1);

        if (args.size() == 2) {
            toReturn.addAll(addAvailable(typed, actions));
            return toReturn;
        }

        if (args.size() == 3) {
            final var action = args.get(2);
            switch (action.toLowerCase()) {
                case "time-unit": {
                    toReturn.addAll(addAvailable(typed, List.of("ticks", "minutes", "hours")));
                    return toReturn;

                }
                case "team": {
                    final List<String> available = new LinkedList<>();
                    currentGame.getTeams().forEach(gameTeam -> available.add(gameTeam.getName()));
                    toReturn.addAll(addAvailable(typed, available));
                    return toReturn;
                }
            }
        }


        return toReturn;
    }

    private List<String> addAvailable(String typed, List<String> available) {
        final List<String> toReturn = new LinkedList<>();
        for (String found : available) {
            if (found.startsWith(typed)) {
                toReturn.add(found);
            }
        }

        return toReturn;
    }

    private void rebuildHologram(SpawnerEditor spawnerEditor, Player player, boolean original) {
        final var gameBuilder = spawnerEditor.getGameBuilder();
        GameCore.getHologramManager().destroy(spawnerEditor.getGameHologram());
        spawnerEditor.getGameBuilder()
                .buildHologram(original ? spawnerEditor.getOriginalSpawner() : spawnerEditor.getResourceSpawner(), gameBuilder.getGameFrame(), player);
    }

    private boolean canBeTime(String value) {
        switch (value) {
            case "ticks":
            case "seconds":
            case "minutes":
                return true;
            default:
                return false;
        }
    }

}
