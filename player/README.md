# Player Utils
Library for working with players on server side (Bukkit, Sponge, Minestom)

## Features
* Sending chat messages to players
* Opening and closing inventories, retrieving currently opened inventory (Material Resolver needed)  
* More features are WIP

[Material Resolver](../material/README.md) is optional dependency. If you want inventory features, you need to shade it.
[World](../world/README.md) is optional dependency. If you want features connected with worlds and locations, you need to shade it.

## Implementing lib to your project

Supported platforms are: bukkit, sponge, minestom

### With Maven
```xml
<repositories>
    <repository>
        <id>screamingrepo</id>
        <url>https://repo.screamingsandals.org/public</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>player-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<!-- Shade plugin configuration and relocation package org.screamingsandals.lib to your own package -->
```

### With Gradle

```groovy
repositories {
    maven { 
        url 'https://repo.screamingsandals.org/public' 
    }
}

dependencies {
    implementation 'org.screamingsandals.lib:player-YOUR_PLATFORM:LATEST_VERSION_HERE'
}

// Shadow plugin configuration and relocation package org.screamingsandals.lib to your own package
```



