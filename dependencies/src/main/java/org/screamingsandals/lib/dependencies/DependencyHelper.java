package org.screamingsandals.lib.dependencies;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

@AllArgsConstructor
public enum DependencyHelper {
    GROOVY("groovy.util.GroovyScriptEngine", "Groovy", "3.0.3"),
    UNIVOCITY("com.univocity.parsers.csv.CsvParser", "Univocity", "2.8.3")
    // add new dependencies here (and of course to
    ;


    private final String checkClass;
    private final String dependencyName;
    private final String dependencyVersion;

    public void load() {
        try {
            Class.forName(checkClass);
        } catch (ClassNotFoundException classNotFoundException) {
            try {
                var libs = new File("libs/");
                if (!libs.exists()) {
                    libs.mkdir();
                }

                var library = new File(libs, dependencyName.toLowerCase() + ".jar");
                if (!library.exists()) {
                    getLogger().info("[ScreamingDependencyHelper] Obtaining " + dependencyName.toLowerCase() + ".jar version " + dependencyVersion + " for your server");

                    Files.copy(new URL("https://repo.screamingsandals.org/org/screamingsandals/misc/" + dependencyName.toLowerCase() + "/" + dependencyVersion + "/" + dependencyName.toLowerCase() + "-" + dependencyVersion.toLowerCase() + ".jar").openStream(), Paths.get(library.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

                    Files.write(Paths.get(new File(libs,dependencyName.toLowerCase() + ".version").getAbsolutePath()), List.of(dependencyVersion), StandardCharsets.UTF_8);
                }
                if (!library.exists()) {
                    throw new Exception("[ScreamingDependencyHelper] Can't obtain dependency: " + dependencyName);
                }

                getLogger().info("[ScreamingDependencyHelper] Loading " + dependencyName.toLowerCase() + ".jar");

                var plugin = Bukkit.getPluginManager().loadPlugin(library);
                Bukkit.getPluginManager().enablePlugin(plugin);

                getLogger().info("[ScreamingDependencyHelper] " + dependencyName + " is loaded! Don't disable or reload this plugin!");
            } catch (Exception ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
    }
}
