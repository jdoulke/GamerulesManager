<img width="1254" height="1254" alt="Gamerule-manager" src="https://github.com/user-attachments/assets/68fd6a8a-d06f-4667-abd0-e3d31820dd70" />

# Ultimate Gamerules Manager

Ultimate Gamerules Manager is a Minecraft server plugin for managing world gamerules through a simple inventory GUI.

Instead of remembering gamerule commands or switching between worlds manually, administrators can open one menu, select a world, inspect its gamerules, change values, copy settings between worlds, or restore them to Minecraft defaults.

---

## Requirements

| Plugin Version | Minecraft Version | Java Version |
|---|---:|---:|
| v5.x | Minecraft 26.1.x - 26.2.x | Java 25 |
| v4.x | Minecraft 1.13 - 1.21 | Java 8+ |

> **Important:** v5.x supports Minecraft 26.1.x through 26.2.x and requires Java 25.
>
> Minimum supported version: **26.1.x**  
> Latest tested version: **26.2.x**
>
> Servers running Minecraft 1.13 - 1.21 should use the latest v4.x release instead.

---

## Features

- Manage gamerules through an inventory GUI
- Select the world you want to configure
- View the current and default value of each gamerule
- Toggle boolean gamerules with one click
- Edit integer gamerules directly through chat
- Cancel integer editing by typing `cancel`
- Keep integer input private instead of broadcasting it to public chat
- Copy gamerule values from one world and paste them into another
- Reset a world's gamerules to their Minecraft default values
- Process only gamerules that are available in the selected world
- Customize gamerule descriptions through `config.yml`
- Customize the material used for each gamerule item
- Configure messages used during integer value input
- Keep selected worlds and copied gamerules isolated per player
- bStats support

---

## How It Works

Run `/gamerule` to open the world selection menu.

Choose a world and the plugin will open the Gamerule Manager for that world. Each gamerule is represented by an item containing its current value, description, and Minecraft default value.

Only gamerules that are valid for the selected world are shown and processed. This also prevents experimental or feature-gated gamerules from being used in worlds where they are not available.

### Boolean Gamerules

Boolean gamerules can be changed immediately from the GUI.

Click the gamerule item to toggle its value between `true` and `false`. The item is refreshed after the change so the new value is visible straight away.

### Integer Gamerules

Integer gamerules use chat input.

Click an integer gamerule and the inventory will close while the plugin waits for the new value. The prompt also shows Minecraft's default value for that gamerule.

Example:

```text
Type the new integer value for random_tick_speed in chat. Default value: 3.
Type cancel to go back without changing it.
```

The message containing the value is intercepted by the plugin and is not sent to other players.

Enter a valid integer to apply the new value, or type:

```text
cancel
```

to return without changing anything.

Invalid input can be retried. After two invalid attempts, the edit is cancelled automatically and the gamerule GUI is reopened.

---

## Copy, Paste and Reset

### Copy Gamerules

Open a world and click **Copy Gamerules** to store its current gamerule values in your session.

### Paste Gamerules

Select another world and click **Paste Gamerules** to apply the copied values there.

If a copied gamerule is not available in the destination world, it is skipped safely.

### Reset Gamerules

Click **Reset Gamerules** to restore all gamerules available in the selected world to their Minecraft default values.

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

1. Download the latest plugin `.jar`.
2. Place it inside your server's `plugins` folder.
3. Restart the server.
4. Run `/gamerule`.
5. Select a world and start managing its gamerules.

No configuration is required for basic usage.

---

## Configuration

The plugin uses `config.yml` for messages, gamerule descriptions, and GUI display items.

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

Descriptions use Minecraft's snake_case gamerule names:

```yaml
freeze_damage: "&7Whether players should take damage when freezing in powder snow."
random_tick_speed: "&7How often random block ticks occur, such as crop growth and leaf decay."
players_sleeping_percentage: "&7The percentage of players that must sleep to skip the night."
```

### Gamerule Display Items

The material used for each gamerule can also be customized:

```yaml
gameruleItems:
  freeze_damage: BLUE_ICE
  random_tick_speed: WHEAT
  players_sleeping_percentage: WHITE_BED
```

If a configured material is missing or invalid, the plugin uses `BOOK` as a fallback.

---

## Updating from v4.x

v5.x uses the gamerule names provided by modern Minecraft versions, which use snake_case keys.

Before moving from v4.x to v5.x:

1. Make sure the server is running Minecraft 26.1.x or 26.2.x.
2. Make sure the server is running Java 25.
3. Back up the existing `config.yml`.
4. Regenerate the config or migrate old camelCase gamerule names to snake_case.

Example:

```yaml
# v4.x
freezeDamage: "&7Whether the player should take damage when inside powder snow."

# v5.x
freeze_damage: "&7Whether players should take damage when freezing in powder snow."
```

Servers staying on Minecraft 1.13 - 1.21 should continue using the latest v4.x release.

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
