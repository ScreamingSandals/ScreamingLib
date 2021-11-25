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
}

dependencies {
    implementation 'org.screamingsandals.lib:core-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
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

### Usage

#### Maven
```xml
<dependencies>
    <dependency>
        <groupId>org.spongepowered</groupId>
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
    implementation 'org.spongepowered:configurate-yaml:4.1.2'
}
```

### Examples

#### Creating a config manager service
```java
@Service
public final class ConfigManager {
    private AbstractConfigurationLoader<?> loader;
    private ConfigurationNode node;

    // used for retrieving the config values
    public ConfigurationNode node(Object... keys) {
        return node.node(keys);
    }

    @OnEnable
    public void enable() {
        // you will have to create a getInstance method in your plugin's main class to get the pluginInstance
        // check the plugin example in the plugin chapter
        final File configFile = Paths.get(
            ExamplePlugin.getInstance().getDataFolder().getAbsolutePath(), "config.yml"
        ).toFile();
        // checking if the file exists and is not a directory
        if (!configFile.isFile()) {
            // copying the config file from the JAR
            Files.copy(ExamplePlugin.class.getResourceAsStream("/config.yml"), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        // constructs the loader
        loader = YamlConfigurationLoader.builder().file(configFile).build();
    }

    @OnPostEnable
    public void postEnable() {
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
@OnEnable(dependsOn = {
    ConfigManager.class
})
public void enable(ConfigManager configManager) {
    // drills down through the structure
    PlayerMapper.getConsoleSender().sendMessage(Integer.toString(configManager.node("section", "value").getInt(0))) // gets the value or default if not present
    // yaml lists can be retrieved with ConfigurationNode#childrenList()
    // yaml maps can be retrieved with ConfigurationNode#childrenMap()
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
final Optional<PlayerWrapper> player = PlayerMapper.getPlayer("Misat11");
if (player.isPresent()) {
    player.orElseThrow().sendMessage("Hello misat!");
} else {
    PlayerMapper.getConsoleSender().sendMessage("Misat is not online!");
}
```