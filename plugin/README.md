# Plugin
Library for generating main classes.

## Features
* List of all plugins.
* Main classes generation.
* Plugin files (like plugin.yml) generation.

## Implementing lib to your project

Supported platforms are: bukkit, sponge, minestom, bungee, velocity

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
        <artifactId>plugin-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>screaming-annotation</artifactId>
        <version>LATEST_VERSION</version>
        <scope>provided</scope>
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
    implementation 'org.screamingsandals.lib:plugin-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
}
```




