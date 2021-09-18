# Base Module for Minecraft Proxy Plugin
Library for working with players on proxy side (BungeeCord and Velocity).

## Features
* Changing player's server
* Sending chat messages to players
* More features are WIP

## Implementing lib to your project

Supported platforms are: bungee, velocity

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
        <artifactId>proxy-YOUR_PLATFORM</artifactId>
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
    implementation 'org.screamingsandals.lib:proxy-YOUR_PLATFORM:LATEST_VERSION_HERE'
}
```




