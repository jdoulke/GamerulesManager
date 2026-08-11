<img width="1254" height="1254" alt="Gamerule-manager" src="https://github.com/user-attachments/assets/68fd6a8a-d06f-4667-abd0-e3d31820dd70" />

# Ultimate Gamerules Manager

Ultimate Gamerules Manager is a Minecraft server plugin that allows server owners and administrators to manage world gamerules through a clean and simple GUI.

Select a world, browse its gamerules, change values, copy settings between worlds, or restore everything to Minecraft's defaults. All without manually typing gamerule commands.

---

## Requirements

| Plugin Version | Minecraft Version | Java Version |
|---|---:|---:|
| v5.x | Minecraft 26.1.x - 26.2.x | Java 25 |
| v4.x | Minecraft 1.13 - 1.21 | Java 8+ |

> **Important for v5.x:** GamerulesManager v5.x supports Minecraft **26.1.x through 26.2.x** and requires **Java 25**.
>
> Minecraft **26.1.x** is the minimum supported version for v5.x, while **26.2.x** is the latest tested release.
>
> If your server is running Minecraft **1.13 - 1.21**, use the latest v4.x version instead.

---

## Features

- Manage gamerules through an inventory GUI
- Select the world you want to configure
- Toggle boolean gamerules with one click
- Edit integer gamerules directly through chat
- Cancel an integer edit at any time by typing `cancel`
- Copy all gamerule values from one world
- Paste copied gamerules into another world
- Reset a world's gamerules to their default values
- View the current value of every gamerule
- View each gamerule's default value
- Customize gamerule descriptions through `config.yml`
- Customize GUI display items through `config.yml`
- Configure the messages used during integer value input
- bStats support

---

## How It Works

When you run `/gamerule`, a world selection menu opens. After selecting a world, the plugin opens the gamerule management GUI for that world.

Each item represents a gamerule. Hover over an item to view useful information about that gamerule, including its current value, description, and default value.

Only gamerules available in the selected world are shown and processed.

There are two gamerule types:

### Boolean Gamerules

Click the item to toggle the value between `true` and `false`.

### Integer Gamerules

Click the item and the GUI will temporarily close. The plugin will ask you to enter the new value directly in chat.

The prompt also shows the gamerule's default value. Type a valid integer to apply the new value, or type `cancel` to return without changing it.

The value you enter is handled by the plugin and is not broadcast to public chat. Invalid input can be retried, and after two invalid attempts the edit is cancelled automatically.

---

## Commands

| Command | Description | Permission |
|---|---|---|
| `/gamerule` | Opens the world selection GUI | `gamerulemanager.use` |
| `/gamerule reload` | Reloads the plugin configuration | `gamerulemanager.reload` |

### Aliases

- `/mgamerule`
- `/ggamerule`
- `/gamerules`
- `/gamerulemanager`

---

## Permissions

| Permission | Description |
|---|---|
| `gamerulemanager.use` | Allows the player to open and use the Gamerule Manager |
| `gamerulemanager.reload` | Allows the player to reload the plugin configuration |

---

## Installation

1. Download the plugin `.jar` file.
2. Place it inside your server's `plugins` folder.
3. Restart your server.
4. Use `/gamerule` to open the GUI.

No configuration is required for basic usage.

---

## Configuration

The default `config.yml` includes gamerule descriptions, GUI display items, and configurable messages.

### Integer Input Messages

```yaml
# Messages shown while entering a gamerule value through chat.
# Available placeholders for valuePrompt: %gamerule%, %default_value%
valuePrompt: "&eType the new integer value for &b%gamerule%&e in chat. &7Default value: &f%default_value%&e."
cancelHint: "&7Type &ccancel&7 to go back without changing it."
invalidValue: "&eYou didn't type an &cinteger number&e. Try again or type &ccancel&e."
changeCancelled: "&eGamerule change cancelled."
tooManyInvalid: "&cToo many invalid attempts. Gamerule change cancelled."
```

### Gamerule Descriptions

```yaml
freeze_damage: "&7Whether players should take damage when freezing in powder snow."
random_tick_speed: "&7How often random block ticks occur, such as crop growth and leaf decay."
players_sleeping_percentage: "&7The percentage of players that must sleep to skip the night."
```

### Gamerule Display Items

```yaml
gameruleItems:
  freeze_damage: BLUE_ICE
  random_tick_speed: WHEAT
  players_sleeping_percentage: WHITE_BED
```

If a configured material is missing or invalid, the plugin automatically uses `BOOK` as a fallback.

---

## Updating from v4.x to v5.x

GamerulesManager v5.x is a breaking update.

Before updating:

1. Make sure your server is running Minecraft **26.1.x or 26.2.x**.
2. Make sure your server is running **Java 25**.
3. Back up your old `config.yml`.
4. Delete the old config file or manually migrate old camelCase gamerule names to the new snake_case format.

Example migration:

```yaml
# v4.x
freezeDamage: "&7Whether the player should take damage when inside powder snow."

# v5.x
freeze_damage: "&7Whether players should take damage when freezing in powder snow."
```

If your server is staying on Minecraft **1.13 - 1.21**, continue using the latest v4.x release.

---

## Metrics

Ultimate Gamerules Manager uses bStats to collect anonymous usage statistics.

[bStats](https://bstats.org/plugin/bukkit/GamerulesManager/15346)

---

## Links

[Spigot Resource](https://www.spigotmc.org/resources/102215/)

---

## Screenshots

![Boolean Gamerule Example](https://user-images.githubusercontent.com/17084358/216966504-be8dfcee-e37b-4ec0-8ed5-948a325a3685.png)
![Copy Gamerules](https://user-images.githubusercontent.com/17084358/216966507-12ba6440-f3a9-4cd2-9b35-4af7c844dd7f.png)
![Menu Page 1](https://user-images.githubusercontent.com/17084358/216966515-95c215e4-820f-4a93-bac5-f3a85c65f604.png)
![Menu Page 2](https://user-images.githubusercontent.com/17084358/216966519-fbff80e1-28aa-4b0d-a5cd-7e1e8680c49d.png)
![Paste Gamerules](https://user-images.githubusercontent.com/17084358/216966523-ab763900-978e-47ea-9ea4-1abb788bbe39.png)
![Reset Gamerules](https://user-images.githubusercontent.com/17084358/216966526-99867955-66b5-4af2-b870-9eb6e930d9ba.png)
![World Selection](https://user-images.githubusercontent.com/17084358/216966527-b97c2fcf-ae87-4d3b-bcf7-226c2a1d5438.png)
