package org.screamingsandals.lib.gamecore.commands;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.common.CommandBuilder;
import org.screamingsandals.lib.commands.common.RegisterCommand;
import org.screamingsandals.lib.commands.common.interfaces.ScreamingCommand;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.resources.editor.SpawnerEditor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

@RegisterCommand
public class SpawnerEditorCommand implements ScreamingCommand {
    private final List<String> actions = new LinkedList<>();

    @Override
    public void register() {
        CommandBuilder.bukkitCommand()
                .create("scseditor", GameCore.getInstance().getAdminPermissions(), Collections.singletonList("sce"))
                .setDescription("Super cool spawner editor!") //TODO language
                .setUsage("/scseditor help")
                .handlePlayerCommand(this::handleCommand)
                .handlePlayerTab(this::handleTab)
                .register();

        actions.addAll(List.of("max-spawned", "amount", "period", "team", "location", "time-unit", "save", "exit", "help"));
    }

    private void handleCommand(Player player, List<String> args) {
        final var size = args.size();
        final var editorFromManager = GameCore.getGameManager().getSpawnerEditor(player);

        System.out.println(size);
        if (editorFromManager.isEmpty() || size < 1) {
            mpr("game-builder.spawners.editor.invalid-entry").sendList(player);
            return;
        }

        final var spawnerEditor = editorFromManager.get();
        final var spawner = spawnerEditor.getResourceSpawner();

        if (size > 2) {
            mpr("game-builder.spawners.editor.invalid-entry").sendList(player);
            return;
        }

        if (size == 2) {
            final var action = args.get(0);
            final var value = args.get(1);

            switch (action) {
                case "max-spawned": {
                    if (canCastToInt(value, player)) {
                        spawner.setMaxSpawned(castToInt(value));
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    }
                    break;
                }
                case "amount": {
                    if (canCastToInt(value, player)) {
                        spawner.setAmount(castToInt(value));
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", value)
                                .send(player);
                    }
                    break;
                }
                case "period": {
                    if (canCastToInt(value, player)) {
                        spawner.setPeriod(castToInt(value));
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
                        mpr("generals.errors.invalid-team")
                                .replace("%team%", value)
                                .send(player);
                    }
                    break;
                }
                case "time-unit": {
                    String replaced;
                    if (canBeTime(value.toLowerCase())) {
                        if (value.equalsIgnoreCase("ticks")) {
                            spawner.setTimeUnit(TimeUnit.MILLISECONDS);
                            replaced = "ticks";
                        } else {
                            spawner.setTimeUnit(TimeUnit.valueOf(value.toUpperCase()));
                            replaced = value;
                        }
                        mpr("game-builder.spawners.editor.changed")
                                .replace("%value%", action)
                                .replace("%newValue%", replaced)
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

        if (args.size() == 1) {
            final var command = args.get(0).toLowerCase();

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
                    spawner.setLocation(LocationAdapter.create(player.getLocation()));
                    mpr("game-builder.spawners.editor.location").send(player);
                    rebuildHologram(spawnerEditor, player, false);
                    return;
                }
                default: {
                    mpr("game-builder.spawners.editor.invalid-entry").sendList(player);
                }
            }

        }
    }

    private List<String> handleTab(Player player, List<String> args) {
        final List<String> toReturn = new LinkedList<>();
        final var editorFromManager = GameCore.getGameManager().getSpawnerEditor(player);

        if (editorFromManager.isEmpty() || args.size() < 1) {
            return toReturn;
        }

        final var spawnerEditor = editorFromManager.get();
        final var currentGame = spawnerEditor.getGameBuilder().getGameFrame();
        final var typed = args.get(0);

        if (args.size() == 1) {
            toReturn.addAll(addAvailable(typed, actions));
            return toReturn;
        }

        if (args.size() == 2) {
            switch (typed.toLowerCase()) {
                case "time-unit": {
                    toReturn.addAll(addAvailable(typed, List.of("ticks", "minutes", "hours")));
                    return toReturn;

                }
                case "team": {
                    final List<String> available = new LinkedList<>();
                    currentGame.getTeams().forEach(gameTeam -> available.add(gameTeam.getTeamName()));
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

    private boolean canCastToInt(String value, Player player) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            mpr("general.errors.invalid-number")
                    .replace("%entry%", value)
                    .send(player);
            return false;
        }
    }

    private int castToInt(String value) {
        return Integer.parseInt(value);
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
