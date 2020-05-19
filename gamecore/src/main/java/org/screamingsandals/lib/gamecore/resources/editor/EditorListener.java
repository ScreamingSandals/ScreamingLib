package org.screamingsandals.lib.gamecore.resources.editor;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;

import java.util.concurrent.TimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

@RequiredArgsConstructor
public class EditorListener implements Listener {
    private final SpawnerEditor spawnerEditor;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final var player = event.getPlayer();
        final var args = event.getMessage().split(" ");
        final var spawner = spawnerEditor.getResourceSpawner();

        if (args.length > 2) {
            mpr("game-builder.spawners.editor.invalid-entry").sendList(player);

            event.setCancelled(true);
            return;
        }

        if (args.length == 2) {
            final var action = args[0];
            final var value = args[1];

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
                case "spawn-time": {
                    String replaced;
                    if (canBeTime(value.toLowerCase())) {
                        if (value.equals("ticks") || value.equals("tick")) {
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

            event.setCancelled(true);
            rebuildHologram(player, false);
            return;
        }

        if (args.length == 1) {
            final var command = args[0].toLowerCase();

            switch (command) {
                case "exit": {
                    spawnerEditor.end();
                    mpr("game-builder.spawners.editor.exit").send(player);
                    rebuildHologram(player, true);
                    return;
                }
                case "save": {
                    spawnerEditor.save();
                    mpr("game-builder.spawners.editor.save").send(player);
                    break;
                }
                case "location": {
                    spawner.setLocation(LocationAdapter.create(player.getLocation()));
                    mpr("game-builder.spawners.editor.location").send(player);
                    break;
                }
                default: {
                    mpr("game-builder.spawners.editor.invalid-entry").sendList(player);
                }
            }

            event.setCancelled(true);
            rebuildHologram(player, false);
        }
    }

    private void rebuildHologram(Player player, boolean original) {
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
