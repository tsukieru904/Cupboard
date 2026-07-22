# Cupboard

Cupboard is a Rust-style protection plugin designed for Minecraft PvP servers. Players protect their builds and items using a Block of Gold (the "Cupboard"), while destructive mechanics like TNT raiding remain available — fully configurable to match your server's balance and style.

**Supported platforms:** Spigot, Bukkit, Paper, Purpur, Leaves, Folia, Luminol

---

## Description

Cupboard adds a territory protection system similar to Rust. By placing a Block of Gold, players claim a 19×19×19 cube area centered on the block. Within this area, unauthorized players cannot place or break blocks. Explosives like TNT remain effective (configurable), enabling base raiding while keeping Minecraft's core mechanics intact.

---

## Features

- Area protection using Block of Gold (19×19×19, centered)
- Access control via right-clicking the gold block
- Prevent TNT damage in protected areas (configurable)
- Prevent Creeper damage in protected areas (enabled by default)
- Prevent Nether portals from being:
  - Blocked by solid blocks
  - Covered by lava
  - Used by non-player entities
- Customizable Nether portal search radius
- PigZombies drop Nether Wart
- Configurable world borders
  - Border can shrink over time
  - Adjustable scale for Nether and The End
- TNT can destroy obsidian, water, and lava (configurable)
- Fully translatable via language files (English, Traditional Chinese, and more)

---

## Protection Details

**What is protected:**
1. Unauthorized players cannot place or break blocks
2. Unauthorized players cannot use stone pressure plates or stone buttons
3. Mobs cannot trigger stone pressure plates
4. Creepers cannot destroy blocks within the protected area

**What is NOT protected:**
1. Unauthorized players can place TNT, which ignites immediately (configurable)
2. Unauthorized players can use:
   - Wooden pressure plates and buttons
   - Chests
   - Armor stands
3. TNT can destroy blocks inside the protected area (configurable)

---

## Commands and Permissions

- `/kill` — Allows players to kill themselves (useful if trapped in a protected area)
- No permission system required
- Operators in Creative Mode automatically bypass protections

---

## Installation

1. Download the plugin `.jar` file
2. Place it in the `plugins/` folder of your server
3. Restart the server
4. Edit `config.yml` and the locale file (`locales/`) to adjust settings

---

## Language Files

Locale files live under `plugins/Cupboard/locales/`. Set the file name (without `.yml`) in `config.yml` under the `locale` key. Bundled locales:

| Locale key | Language |
|---|---|
| `en-EN` | English (default) |
| `zh` | Chinese (Traditional) |
| `zh_TW` | Chinese (Taiwan, Traditional) |
| `zh_HK` | Chinese (Hong Kong, Traditional) |

You can add your own translations by creating a new `.yml` file in the `locales/` folder using the same keys, then pointing `locale` in `config.yml` to its file name.

---

## Compatibility

This plugin is tested and compatible with:

- Spigot
- Bukkit
- Paper
- Purpur
- Leaves
- Folia
- Luminol

**Version notes:**
- Targets the Minecraft 1.20 API baseline (1.20.x and newer)
- Requires Java 17+
- Folia is supported through the compatibility scheduler layer
- Purpur is supported as a Paper fork with the same Bukkit/Paper API surface

---

## License

MIT, GPL-3.0, etc.

---

## Credits

Developed by [YourNameHere].
Inspired by the territory protection system in Rust.

---

**Other languages:** [繁體中文 (README_zh_TW.md)](README_zh_TW.md)
