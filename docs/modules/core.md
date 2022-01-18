# Core (server)

Base module required for pretty much everything in SLib. Contains the base features for working with the server, such as the item API, block API, entities API and more. All other modules require this module to be present.

## Platform support

|  Minecraft: Java Edition | <1.9.4 | 1.9.4 | 1.10.x | 1.11.x | 1.12.x | 1.13.x | 1.14.x | 1.15.x | 1.16.x | 1.17.x |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | 
| [Spigot](https://www.spigotmc.org/wiki/spigot/) / [Paper](https://papermc.io) (and forks) | No | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| [Glowstone](https://glowstone.net/) | No | ? | ? | ? | ? | | | | | ? | |
| [MohistMC](https://mohistmc.com/) (and similar) | No | | | | ? | | | | ? | ? |
| [Minestom](https://minestom.net/) | | | | | | | | | No | Planned |
| [Sponge API 8+](https://www.spongepowered.org/) | | | | | | | | | Planned | Planned |

*? = the version may be supported, but the current state is unknown*  
*Empty field = there's no such version of the specific platform*

Support for the Bedrock Edition is also planned, but not anytime soon.

| Minecraft: Bedrock Edition | Latest |
| :--- | :---: |
| [Nukkit](https://github.com/CloudburstMC/Nukkit) | Planned |
| [Cloudburst](https://github.com/CloudburstMC/Server) | Planned |

## Usage
Supported platforms are: `bukkit, minestom, sponge`

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
        <artifactId>core-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>annotation</artifactId>
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
    implementation 'org.screamingsandals.lib:core-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:annotation:LATEST_VERSION_HERE'
}
```

## Comparison to the Bukkit API

| Bukkit API class | SLib class                                                                 |
|------------------|----------------------------------------------------------------------------|
| Player           | PlayerWrapper                                                              |
| HumanEntity      | EntityHuman                                                                |
| LivingEntity     | EntityLiving                                                               |
| Projectile       | EntityProjectile                                                           |
| Item             | EntityItem                                                                 |
| ExperienceOrb    | EntityExperience                                                           |
| LightningStrike  | EntityLightning                                                            |
| Firework         | EntityFirework                                                             |
| Entity           | EntityBasic                                                                |
| World            | WorldHolder                                                                |
| Location         | LocationHolder                                                             |
| Block            | BlockHolder                                                                |
| Material         | ItemTypeHolder (for item materials), BlockTypeHolder (for block materials) |
| Server           | Server                                                                     |
| ItemStack        | Item                                                                       |

## Holders
ScreamingLib makes use of so-called Holders, platform unopinionated data holding classes.  

Holders make use of Minecraft namespaced keys to stay platform independent.
A very useful resource for getting Minecraft namespaced keys is Articdive's [ArticData](https://github.com/Articdive/ArticData) repository.

### Instantiating
You can instantiate a holder with the `<Anything>Holder#of(Object)` method.

#### BlockTypeHolder and ItemTypeHolder
For example, I'm going to create a holder holding the stone material.
```java
final BlockTypeHolder block = BlockTypeHolder.of("minecraft:stone");
final ItemTypeHolder item = ItemTypeHolder.of("minecraft:stone");
// both are going to be STONE on bukkit
PlayerMapper.getConsoleSender().sendMessage(block.platformName());
PlayerMapper.getConsoleSender().sendMessage(item.platformName());
```

### Comparing
You can compare a holder with the `<Anything>Holder#is(Object)` method.

#### BlockTypeHolder and ItemTypeHolder
For example, I'm going to check if the material is stone, air or something else.
```java
if (holder.is("minecraft:stone")) {
    PlayerMapper.getConsoleSender().sendMessage("Holder is stone!");
} else if (holder.isAir()) {
    PlayerMapper.getConsoleSender().sendMessage("Holder is air/empty!");
} else {
    PlayerMapper.getConsoleSender().sendMessage("Holder is " + holder.platformName() + "!");
}
```

## Services
Services are classes with the `@Service` annotation, specified in the `@Init` annotation on the main plugin class. They are initialized when the plugin starts to load.
It is possible to retrieve the service class instances with the ServiceManager class (`ServiceManager#get(Class<?>)`).  

```java
@Service(dependsOn = {
	SomeExampleServiceThatThisDependsOn.class
}, loadAfter = {
	SomeExampleServiceThatWillBeLoadedBeforeThis.class
})
public class ExampleService {
	@OnPostConstruct
	public void onConstruct() {
		// Service construction logic
	}

	@OnEnable
	public void enable() {
		// Service enable logic
	}

	@OnDisable
	public void disable() {
		// Service disable logic (plugin is disabling)
	}
}
```

### Annotations
* `@Service(dependsOn = {}, loadAfter = {})` - Marks the annotated class as a service. Service classes specified in the `dependsOn` field are loaded *before* initializing the annotated class, while service classes specified in the `loadAfter` field are loaded *after* initializing the annotated class.
* `@OnPostConstruct` - Marks the annotated method to run directly after the service class is constructed.
* `@OnEnable` - Marks the annotated method to run after the service class is initialized.
* `@OnDisable` - Marks the annotated method to run after the service class is being disabled (plugin is disabling).
* `@Init(platforms = {}, services = {})` (plugin class only) - Defines service classes that should be initialized when the plugin is loading (order sensitive). In the `platforms` field, you can specify platforms that the service classes in the annotation will be initialized on (array of `PlatformType`, can be left out).

## Configuration
ScreamingLib doesn't bundle a configuration system like Bukkit, so you will have to use an external library. We recommend using Sponge's [Configurate](https://github.com/SpongePowered/Configurate) library, which I will demonstrate the usage for in this chapter.

Configurate supports many formats, but I'll use `configurate-yaml` in this example (all work on the same principle). You can check out the available formats [here](https://github.com/SpongePowered/Configurate#configurate-loaders).

### Usage

#### Maven
```xml
<dependencies>
    <dependency>
        <groupId>org.spongepowered</groupId>
        <!--- https://github.com/SpongePowered/Configurate#configurate-loaders -->
        <artifactId>configurate-yaml</artifactId>
        <version>4.1.2</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    // https://github.com/SpongePowered/Configurate#configurate-loaders
    implementation 'org.spongepowered:configurate-yaml:4.1.2'
}
```

### Examples

#### Creating a config manager service
```java
@Service
public final class ConfigManager {
    private ConfigurationNode node;

    // used for retrieving the config values
    public ConfigurationNode node(Object... keys) {
        return node.node(keys);
    }

    @OnEnable
    public void enable(ExamplePlugin plugin) {
        plugin.saveResource("config.yml", false); // saves the config file
    }

    // SLib automatically provides the loader
    // change the config loader implementation class if you don't use the yaml version
    @OnPostEnable
    public void postEnable(@ConfigFile("config.yml") YamlConfigurationLoader loader) {
        // tries to load the file, prints the stacktrace if something went wrong
        try {
            node = loader.load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }
}
```

#### Retrieving config values
Example config:
```yaml
section:
    value: 1
```

Retrieving values from the example config:
```java
@Service(dependsOn = {
    ConfigManager.class
})
public final class ExampleService {
    @OnEnable
    public void enable(ConfigManager configManager) {
        // drills down through the structure
        PlayerMapper.getConsoleSender().sendMessage(Integer.toString(configManager.node("section", "value").getInt(0))) // gets the value or default if not present
        // lists can be retrieved with ConfigurationNode#childrenList()
        // maps can be retrieved with ConfigurationNode#childrenMap()
    }
}
```

## Examples

### Iterating over online players
If you want to iterate over all online players and compute something for each one, utilize the `List<PlayerWrapper> Server#getConnectedPlayers()` method.
```java
// for loop
for (final PlayerWrapper player : Server.getConnectedPlayers()) {
    player.sendMessage("Hello!");
}
// Iterable#forEach()
Server.getConnectedPlayers().forEach(player -> player.sendMessage("Hello!"));
```

### Retrieving a player

#### Converting a platform player to PlayerWrapper
If you want to convert a platform player (e.g. Bukkit's `Player`) to `PlayerWrapper`, utilize the `PlayerWrapper PlayerMapper#wrapPlayer(Object)` method.
```java
final PlayerWrapper player = PlayerMapper.wrapPlayer(platformPlayer);
player.sendMessage("Hello!");
```
#### Converting a UUID to OfflinePlayerWrapper
If you want to convert a UUID to an OfflinePlayerWrapper, utilize the `OfflinePlayerWrapper PlayerMapper#wrapOfflinePlayer(Object)` method.
```java
// will probably throw UnsupportedOperationException, since the random UUID doesn't belong to a player that has joined your server
final OfflinePlayerWrapper player = PlayerMapper.wrapPlayer(UUID.randomUUID());
PlayerMapper.getConsoleSender().sendMessage("Is player online? " + Boolean.toString(player.isOnline()));
```

#### Converting a name to PlayerWrapper
If you want to convert a player's name to PlayerWrapper, utilize the `Optional<PlayerWrapper> PlayerMapper#getPlayer(String)` method.
```java
PlayerMapper.getPlayer("Misat11").ifPresent(player -> player.sendMessage("Hello misat!"));
```

### Handling events
!!! warning "Platform events"

    ScreamingLib `EventManager` is **not** listening to non-standard events fired through the event manager of the platform you're running on!


#### OnEvent annotation
First of all, start by making a simple service class, like this:
```java
@Service
public class ExampleService {
    // Service class
}
```
Then create a new public method that returns void, has an event parameter and annotate it with `@OnEvent`. It is possible to specify the priority field and the ignoreCancelled field in the annotation.
```java
@Service
public class ExampleService {
    @OnEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(SPlayerInteractEvent event) {
        // Event handling logic
    }
}
```
Then register the service class in the `@Init` annotation on the main plugin class, and you're done!

#### EventManager registration
You can also listen to events by registering a `Consumer<AbstractEvent>` with the `EventManager`.
```java
EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, event -> PlayerMapper.getConsoleSender().sendMessage("Player " + event.getPlayer() + " has left."));
```

### Firing events
!!! warning "Platform events"

    ScreamingLib `EventManager` is **not** firing events to the event manager of the platform you're running on!


#### Synchronously
You can fire an event synchronously with the `<T extends SEvent> T EventManager#fire(T)` method.
```java
final ExampleEvent event = EventManager.fire(new ExampleEvent());
// if your event is cancellable, you can check for it
if (!event.isCancelled()) {
    PlayerMapper.getConsoleSender().sendMessage("Hello World!");
}
```

#### Asynchronously
You can fire an event asynchronously with the `<T extends SAsyncEvent> CompletableFuture<T> EventManager#fireAsync(T)` method.
```java
EventManager.fireAsync(new ExampleAsynchronousEvent()).thenAccept(event -> {
    // if your event is cancellable, you can check for it
    if (!event.isCancelled()) {
        PlayerMapper.getConsoleSender().sendMessage("Hello World!");
    }
});
```

### Creating events

#### Synchronous
A synchronous event needs to implement the `SEvent` class (or `SCancellableEvent` class, if cancellable).

```java
public class ExampleEvent implements SCancellableEvent {
    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
```

#### Asynchronous
An asynchronous event needs to implement the `SAsyncEvent` class (or `SCancellableAsyncEvent`, if cancellable).

```java
public class ExampleEvent implements SCancellableAsyncEvent {
    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
```

### Running tasks

!!! tip "Managing tasks without Tasker"

    If you want to manage tasks yourself (without using Tasker), you can utilize the `Server#runSynchronously(Runnable)` method to run a task synchronously.

#### Normal task
So let's say, that we want to create a task to send a message to console every 30 seconds until the server stops/plugin disables.  

First of all, let's create a task builder, which takes in a `Runnable`.
```java
Tasker.build(() -> PlayerMapper.getConsoleSender().sendMessage("Hello World!"));
```
Then, let's add the repeat time period.
```java
Tasker.build(() -> PlayerMapper.getConsoleSender().sendMessage("Hello World!")).repeat(30, TaskerTime.SECONDS);
```
Now let's start the task.
```java
Tasker.build(() -> PlayerMapper.getConsoleSender().sendMessage("Hello World!")).repeat(30, TaskerTime.SECONDS).start();
```
And you're done!

#### Self-cancelling task
Let's create a task which sends a message to the console 10 times and then stops.  

First of all, let's create a task builder, which takes in a `Function<TaskBase, Runnable>`.
```java
final AtomicInteger count = new AtomicInteger(0);
Tasker.build(taskBase -> () -> {
    if (count.get() >= 10) {
        taskBase.cancel();
        return;
    }
    PlayerMapper.getConsoleSender().sendMessage("Hello World!");
    count.getAndIncrement();
});
``` 
Then, let's run the task right away.
```java
final AtomicInteger count = new AtomicInteger(0);
Tasker.build(taskBase -> () -> {
    if (count.get() >= 10) {
        taskBase.cancel();
        return;
    }
    PlayerMapper.getConsoleSender().sendMessage("Hello World!");
    count.getAndIncrement();
}).afterOneTick().start();
```
Congrats, you've just made a self-cancelling task!

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