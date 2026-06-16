<img width="1254" height="1254" alt="Gamerule-manager" src="https://github.com/user-attachments/assets/68fd6a8a-d06f-4667-abd0-e3d31820dd70" />

# GamerulesManager

A Minecraft server plugin that allows server owners and administrators to manage world gamerules through an easy-to-use GUI.

GamerulesManager lets you select a world, view its gamerules, change boolean and integer values, copy gamerules between worlds, and reset gamerules to their default values.

---

## Requirements

> **Important:** GamerulesManager v5.x is a breaking update.

| Plugin Version | Minecraft Version | Java Version |
|---|---:|---:|
| v5.x | Minecraft 26.x.x | Java 25 |
| v4.x | Minecraft 1.13 - 1.21 | Java 8+ |

If your server is running Minecraft 1.13 - 1.21, use the latest v4.x version of the plugin.

---

## Features

- Manage gamerules through a clean inventory GUI
- Select which world you want to configure
- Toggle boolean gamerules with one click
- Edit integer gamerules through an AnvilGUI input
- Copy gamerules from one world and paste them to another
- Reset all gamerules of a world to their default values
- Configurable gamerule descriptions
- Configurable display items for each gamerule
- Per-player GUI session handling
- bStats support

---

## Commands

| Command | Description | Permission |
|---|---|---|
| `/gamerule` | Opens the GamerulesManager GUI | `gamerulemanager.use` |
| `/gamerule reload` | Reloads the plugin configuration | `gamerulemanager.reload` |

### Aliases

- `/mgamerule`
- `/ggamerule`
- `/gamerules`
- `/gamerulemanager`

## Permissions

| Permission | Description |
|---|---|
| `gamerulemanager.use` | Allows the player to open the GUI |
| `gamerulemanager.reload` | Allows the player to reload the plugin configuration |

## Installation
1. Download the latest plugin `.jar`.
2. Place it inside your server's `plugins` folder.
3. Restart your server.
4. Open the GUI with `/gamerule`.

## Configuration

The plugin uses `config.yml` to customize messages, gamerule descriptions, and display items.

```yaml
freeze_damage: "&7Whether players should take damage when freezing in powder snow."
random_tick_speed: "&7How often random block ticks occur, such as crop growth and leaf decay."
players_sleeping_percentage: "&7The percentage of players that must sleep to skip the night."
```

You can also change the item used for each gamerule:

```yaml
gameruleItems:
  freeze_damage: BLUE_ICE
  random_tick_speed: WHEAT
  players_sleeping_percentage: WHITE_BED
```

If an item is missing or invalid, the plugin will use `BOOK` as a fallback.

## Updating from v4.x to v5.x

GamerulesManager v5.x only supports Minecraft 26.x.x and requires Java 25.

Before updating:

1. Make sure your server is running Minecraft 26.x.x.
2. Make sure your server is running Java 25.
3. Back up your old `config.yml`.
4. Delete the old config file or manually migrate the old camelCase gamerule names to the new snake_case format.

Example migration:

```yaml
# Old v4.x format
freezeDamage: "&7Whether the player should take damage when inside powder snow."

# New v5.x format
freeze_damage: "&7Whether players should take damage when freezing in powder snow."
```

If you are staying on Minecraft 1.13 - 1.21, do not update to v5.x. Use the latest v4.x version instead.

## Metrics

This plugin uses bStats to collect anonymous usage statistics.

[bStats](https://bstats.org/plugin/bukkit/GamerulesManager/15346)

## Links

[Spigot Resource](https://www.spigotmc.org/resources/gamerules-manager-1-13-1-19.102215/)

## Screenshots

![Boolean Gamerule Example](https://user-images.githubusercontent.com/17084358/216966504-be8dfcee-e37b-4ec0-8ed5-948a325a3685.png)
![Copy Gamerules ](https://user-images.githubusercontent.com/17084358/216966507-12ba6440-f3a9-4cd2-9b35-4af7c844dd7f.png)
![Integer Gamerule Example](https://user-images.githubusercontent.com/17084358/216966512-d0c9987a-8ff6-44d5-84fd-781871ea5c2b.png)
![Menu Page 1](https://user-images.githubusercontent.com/17084358/216966515-95c215e4-820f-4a93-bac5-f3a85c65f604.png)
![Menu Page 2](https://user-images.githubusercontent.com/17084358/216966519-fbff80e1-28aa-4b0d-a5cd-7e1e8680c49d.png)
![Paste Gamerules](https://user-images.githubusercontent.com/17084358/216966523-ab763900-978e-47ea-9ea4-1abb788bbe39.png)
![Reset Gamerules](https://user-images.githubusercontent.com/17084358/216966526-99867955-66b5-4af2-b870-9eb6e930d9ba.png)
![World Selection](https://user-images.githubusercontent.com/17084358/216966527-b97c2fcf-ae87-4d3b-bcf7-226c2a1d5438.png)
