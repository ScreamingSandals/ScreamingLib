package org.screamingsandals.lib.lang.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.lang.Language;

import static org.screamingsandals.lib.lang.I.mpr;

public class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        Language language = new Language(this, "en");
        System.out.println(mpr("your_mum").get());
    }

}
