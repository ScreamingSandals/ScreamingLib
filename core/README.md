# Base Module for Minecraft Server Plugin

This is the base module required for making minecraft server plugins using the SLib. It contains the base features for working with the server such as item api, block api, entities api etc. All other modules require this module to be present.

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

Support for Bedrock Edition of the game is also planned but not anytime soon.

| Minecraft: Bedrock Edition | Latest |
| :--- | :---: |
| [Nukkit](https://github.com/CloudburstMC/Nukkit) | Planned |
| [Cloudburst](https://github.com/CloudburstMC/Server) | Planned |