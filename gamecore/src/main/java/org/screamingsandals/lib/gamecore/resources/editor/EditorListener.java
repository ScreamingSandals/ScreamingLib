package org.screamingsandals.lib.gamecore.resources.editor;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.screamingsandals.lib.debug.Debug;

@RequiredArgsConstructor
public class EditorListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final var message = event.getMessage();

        if (startsWith(message, "/scseditor")) {
            return;
        }

        if (startsWith(message, "/sce")) {
            return;
        }

        Debug.info("cancel chat");
        event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final var message = event.getMessage();

        if (startsWith(message, "/scseditor")) {
            return;
        }

        if (startsWith(message, "/sce")) {
            return;
        }

        Debug.info("cancel cmd");
        event.setCancelled(true);
    }

    private boolean startsWith(String message, String key) {
        return message.startsWith(key);
    }

}
