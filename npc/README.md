# NPC
Library for creation of lightweight NPC's

Supported versions: [1.9.4 - 1.17].

## Implementing lib to your project

Supported platforms are: bukkit (more platform support will be added later)

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
        <artifactId>npc-YOUR_PLATFORM</artifactId>
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
    implementation 'org.screamingsandals.lib:npc-YOUR_PLATFORM:LATEST_VERSION_HERE'
}

// Shadow plugin configuration and relocation package org.screamingsandals.lib to your own package
```



