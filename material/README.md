# Material Resolver
Library for working with items. Currently optimized for items, blocks are planned.

## Features
* Material type resolving
* Potion type resolving
* Enchantment resolving
* Item building  
* Modifying inventories  
* More features are WIP

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
        <artifactId>material-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<!-- Shade plugin configuration and relocation package org.screamingsandals.lib to your own package -->
```

### With Gradle

#### Standard Gradle
```groovy
repositories {
    maven { 
        url 'https://repo.screamingsandals.org/public' 
    }
}

dependencies {
    implementation 'org.screamingsandals.lib:material-YOUR_PLATFORM:LATEST_VERSION_HERE'
}

// Shadow plugin configuration and relocation package org.screamingsandals.lib to your own package
```

#### ScreamingSandals' Plugin Builder
```groovy
dependencies {
    shade screaming('material-YOUR_PLATFORM', 'LATEST_VERSION')
}

shadowJar {
    relocate 'org.screamingsandals.lib', 'com.example.package.lib'
}
```




