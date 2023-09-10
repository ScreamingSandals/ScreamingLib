# Base Module for Minecraft Server Plugin

This is the base module required for making minecraft server plugins using the SLib. It contains the base features for working with the server such as item api, block api, entities api etc. All other modules require this module to be present.

## Platform support

| Minecraft: Java Edition                                                                   | <1.8.8 | 1.8.8 | 1.9.x | 1.10.x | 1.11.x | 1.12.x | 1.13.x | 1.14.x | 1.15.x |     1.16.x      | 1.17.x |     1.18.x      |      1.19.x      |      1.20.x      |
|:------------------------------------------------------------------------------------------|:------:|:-----:|:-----:|:------:|:------:|:------:|:------:|:------:|:------:|:---------------:|:------:|:---------------:|:----------------:|:----------------:| 
| [Spigot](https://www.spigotmc.org/wiki/spigot/) / [Paper](https://papermc.io) (and forks) |   No   |  Yes  |  Yes  |  Yes   |  Yes   |  Yes   |  Yes   |  Yes   |  Yes   |       Yes       |  Yes   |       Yes       |       Yes        |       Yes        | 
| [Glowstone](https://glowstone.net/) *                                                     |   No   |   ?   |   ?   |   ?    |   ?    |   ?    |        |        |        |                 |   ?    |                 |        ?         |                  |
| MohistMC and similar Forge+Spigot hybrids **                                              |   No   |  No   |  No   |   No   |   No   |   No   |   ?    |   ?    |   ?    |        ?        |   ?    |        ?        |        ?         |        ?         | 
| [Minestom](https://minestom.net/)                                                         |        |       |       |        |        |        |        |        |        |                 |        |                 |                  |     Planned      |
| [Sponge API](https://www.spongepowered.org/) **                                           |        |  No   |  No   |   No   |   No   |   No   |        |        |        | Planned (API 8) |        | Planned (API 9) | Planned (API 10) | Planned (API 11) |

*? = the version may be supported, but the current state is unknown*  
*Empty field = there's no such version of the specific platform*

*\* - Glowstone has no NMS code and as this library heavily depends on it, it is likely not supported yet*  
*\** - Hybrid servers and Sponge often works only with JDK 8 for Minecraft versions below 1.13, while this library requires at least JDK 11. Therefore, these versions are unsupported.*  

Support for Bedrock Edition of the game is also planned but not anytime soon.

| Minecraft: Bedrock Edition | Latest |
| :--- | :---: |
| [Nukkit](https://github.com/CloudburstMC/Nukkit) | Planned |
| [Cloudburst](https://github.com/CloudburstMC/Server) | Planned |