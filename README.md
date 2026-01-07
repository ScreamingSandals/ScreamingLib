# ScreamingLib
[![Build and publish ScreamingLib](https://github.com/ScreamingSandals/ScreamingLib/actions/workflows/publish.yml/badge.svg)](https://github.com/ScreamingSandals/ScreamingLib/actions/workflows/publish.yml)

ScreamingLib is a WIP multiplatform library for creating Minecraft plugins.

## Module list

### Modules for servers
* [Base module](core/README.md)
* [Hologram](extensions/hologram/README.md)
* [Health Indicator](extensions/healthindicator/README.md)
* [Lang](extensions/lang/README.md)
* [NPC](extensions/npc/README.md)
* [Packet library for Minecraft: Java Edition](extensions/packets/README.md)
* [Placeholders](extensions/placeholders/README.md)
* [Cloud Command Framework support](extensions/cloud/README.md)
* [Scoreboards and Sidebars](extensions/sidebar/README.md)
* [Clickable Signs](extensions/signs/README.md)

### Modules for proxy
* [Base module](proxy/README.md)
* [Lang](extensions/lang/README.md)
* [Cloud Command Framework support](extensions/cloud/README.md)

## Compiling

This project uses **Gradle** and requires **JDK 17** or newer (the compiled artifacts require JDK 11 or newer). To build it, clone the repository and run:

```bash
./gradlew clean build
```

On Windows, use:

```bat
gradlew.bat clean build
```

The compiled JAR file for each module will be located in the `build/libs` folder of each subproject. You can also publish it to your local maven repository (`gradlew publishToMavenLocal`).

## License

This project is licensed under the **Apache License 2.0** License - see the [LICENSE](LICENSE) file for details.
