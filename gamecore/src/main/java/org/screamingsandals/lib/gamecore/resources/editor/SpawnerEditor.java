package org.screamingsandals.lib.gamecore.resources.editor;

import lombok.Data;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.resources.ResourceSpawner;

@Data
public class SpawnerEditor {
    private final Player player;
    private final ResourceSpawner resourceSpawner;
    private final EditorListener editorListener;

    public SpawnerEditor(Player player, ResourceSpawner resourceSpawner) {
        this.player = player;
        this.resourceSpawner = resourceSpawner;
        this.editorListener = new EditorListener();
    }
}
