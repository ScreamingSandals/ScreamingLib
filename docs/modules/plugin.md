# Plugin
Module required for generating platform plugin classes.

## Features
* List of all plugins
* Main class generation
* Plugin file (e.g. plugin.yml) generation

## Usage
Supported platforms are: `bukkit, sponge, minestom, bungee, velocity`

### Maven
```xml
<repositories>
    <repository>
        <id>screamingrepo</id>
        <url>https://repo.screamingsandals.org/repository/maven-public</url>
    </repository>
    <repository>
        <id>papermc</id>
        <url>https://papermc.io/repo/repository/maven-public</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>plugin-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>screaming-annotation</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<!-- Shade plugin configuration and relocation package org.screamingsandals.lib to your own package -->
```

### Gradle
```groovy
repositories {
    maven { 
        url 'https://repo.screamingsandals.org/repository/maven-public'
    }
    maven {
        url 'https://papermc.io/repo/repository/maven-public'
    }
}

dependencies {
    implementation 'org.screamingsandals.lib:plugin-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
}
```

## Examples
### Making your first plugin
!!! warning "Adventure"

    If you're experiencing errors related to [Adventure](https://github.com/KyoriPowered/adventure), make sure to relocate the `net.kyori.adventure` package!


#### Creating the main plugin class
Start with extending the `PluginContainer` class, like this:
```java
public class ExamplePlugin extends PluginContainer {
    private static ExamplePlugin INSTANCE;

    public ExamplePlugin() {
        INSTANCE = this;
    }

    // factory method for easy retrieval of the plugin instance
    // ExamplePlugin.getInstance()
    public static ExamplePlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin has not been initialized yet!");
        }
        return INSTANCE;
    }

    @Override
    public void load() {
        // Plugin load logic
    }

    @Override
    public void enable() {
        // Plugin enable logic
    }

    @Override
    public void disable() {
        // Plugin disable logic
    }
}
```
After that, add the `@Plugin` and `@Init` (only if you use SLib services) annotations.
```java
@Plugin(
        id = "ExamplePlugin",
        name = "ExamplePlugin",
        authors = {"ScreamingSandals"},
        version = "0.0.1-SNAPSHOT"
)
@Init(services = {
        ExampleService.class
})
public class ExamplePlugin extends PluginContainer {
    private static ExamplePlugin INSTANCE;

    public ExamplePlugin() {
        INSTANCE = this;
    }

    // factory method for easy retrieval of the plugin instance
    // ExamplePlugin.getInstance()
    public static ExamplePlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin has not been initialized yet!");
        }
        return INSTANCE;
    }

    @Override
    public void load() {
        // Plugin load logic
    }

    @Override
    public void enable() {
        // Plugin enable logic
    }

    @Override
    public void disable() {
        // Plugin disable logic
    }
}
```
If you want to depend on some plugin(s), you can add the `@PluginDependencies` annotation.
```java
@Plugin(
        id = "ExamplePlugin",
        name = "ExamplePlugin",
        authors = {"ScreamingSandals"},
        version = "0.0.1-SNAPSHOT"
)
@PluginDependencies(platform = PlatformType.BUKKIT, dependencies = {
    "DependencyPlugin"
}, softDependencies = {
    "SoftDependencyPlugin"
}, loadBefore = {
    "LoadBeforePlugin"
})
@Init(services = {
        ExampleService.class
})
public class ExamplePlugin extends PluginContainer {
    private static ExamplePlugin INSTANCE;

    public ExamplePlugin() {
        INSTANCE = this;
    }

    // factory method for easy retrieval of the plugin instance
    // ExamplePlugin.getInstance()
    public static ExamplePlugin getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Plugin has not been initialized yet!");
        }
        return INSTANCE;
    }
    
    @Override
    public void load() {
        // Plugin load logic
    }

    @Override
    public void enable() {
        // Plugin enable logic
    }

    @Override
    public void disable() {
        // Plugin disable logic
    }
}
```
And that's it!

### Interacting with other plugins
There's the `PluginManager` class for that, let me show you an example.

For all next steps, you will need the plugin key, which you can get with the `PluginKey PluginManager#createKey(String)`  method, like this:
```java
// takes the plugin name
final PluginKey key = PluginManager.createKey("ExamplePlugin");
```

#### Checking if a plugin is enabled
For checking if a plugin is enabled, you can use the `boolean PluginManager#isEnabled(PluginKey)` method.

```java
if (PluginManager.isEnabled(key)) {
    PlayerMapper.getConsoleSender().sendMessage("Plugin is enabled!");
} else {
    PlayerMapper.getConsoleSender().sendMessage("Plugin is not enabled!");
}
```

#### Getting the plugin's main class
For getting the plugin's main class, you can use the `@Nullable Object PluginManager#getPlatformClass(PluginKey)` method.

```java
final ExamplePlugin plugin = (ExamplePlugin) PluginManager.getPlatformClass(key);
```

#### Getting all plugins on the server
For getting all plugins on the server, you can use the `List<PluginDescription> PluginManager#getAllPlugins()` method.

```java
for (final PluginDescription plugin : PluginManager.getAllPlugins()) {
    PlayerMapper.getConsoleSender().sendMessage(plugin.getName());
}
```
