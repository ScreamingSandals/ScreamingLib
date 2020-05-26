package org.screamingsandals.lib.signs;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignManager {
    private final FileConfiguration config;
    private final File configFile;
    private final HashMap<Location, SignBlock> signs = new HashMap<>();
    private boolean modify = false;
    private final SignOwner owner;

    @SuppressWarnings("unchecked")
    public SignManager(SignOwner owner, FileConfiguration config, File configFile) {
        this.owner = owner;
    	this.config = config;
        this.configFile = configFile;

        var conf = (List<Map<String, Object>>) config.getList("sign");
        if (conf != null) {
            for (var c : conf) {
                var name = c.get("name").toString();
                if (name == null || name.trim().equals("")) {
                	name = c.get("game").toString(); // Compatibility with old BedWars sign.yml
                }
                var loc = (Location) c.get("location");
                signs.put(loc, new SignBlock(loc, name));
            }
        }
    }

    public boolean isSignRegistered(Location location) {
        return signs.containsKey(location);
    }

    public void unregisterSign(Location location) {
        if (signs.containsKey(location)) {
            signs.remove(location);
            modify = true;
        }
    }

    public boolean registerSign(Location location, String game) {
        if (owner.isNameExists(game)) {
        	var block = new SignBlock(location, game);
            signs.put(location, block);
            modify = true;
            owner.updateSign(block);
            return true;
        }
        return false;
    }

    public SignBlock getSign(Location location) {
        return signs.get(location);
    }

    public List<SignBlock> getSignsForName(String name) {
        List<SignBlock> list = new ArrayList<>();
        for (var sign : signs.values()) {
            if (sign.getName().equals(name)) {
                list.add(sign);
            }
        }
        return list;
    }

    public void save() {
        save(false);
    }

    public void save(boolean force) {
        if (modify || force) {
            var list = new ArrayList<>();
            for (var sign : signs.values()) {
                var map = new HashMap<>();
                map.put("location", sign.getLocation());
                map.put("name", sign.getName());
                list.add(map);
            }

            config.set("sign", list);

            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
