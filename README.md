
 🇵🇱 Polski

Opis
**GrzybcioKilof** to plugin Minecraft dodający specjalny kilof, który kopie bloki w obszarze 3x3! Idealny dla graczy, którzy chcą przyspieszyć kopanie i wydobywanie surowców.

✨ Funkcje
- 🪓 **Kopanie 3x3** - Niszczy 9 bloków naraz zamiast jednego
- 🎯 **Inteligentne kopanie** - Kierunek niszczenia bloków zależy od kąta patrzenia gracza
- 💎 **Drop itemów** - Wszystkie zniszczone bloki wypadają jako itemy (uwzględnia Fortune i Silk Touch)
- ⚙️ **Pełna konfiguracja** - Dostosuj nazwę, opis, enchantmenty i promień kopania
- 🎨 **Kolory HEX** - Obsługa kolorów w formacie `&#RRGGBB`
- 🔒 **System uprawnień** - Kontroluj kto może używać kilofa
- 🛡️ **Kompatybilność** - Działa z pluginami ochrony terenu (WorldGuard, GriefPrevention)

📋 Komendy
| Komenda | Opis | Uprawnienie |
|---------|------|-------------|
| `/grzybcio-kilof` | Daje kilof sobie | `grzybciokilof.give` |
| `/grzybcio-kilof <gracz>` | Daje kilof innemu graczowi | `grzybciokilof.give` |
| `/grzybcio-kilof reload` | Przeładowuje konfigurację | `grzybciokilof.reload` |

🔑 Uprawnienia
| Uprawnienie | Opis | Domyślnie |
|-------------|------|-----------|
| `grzybciokilof.give` | Pozwala używać komendy /grzybcio-kilof | OP |
| `grzybciokilof.use` | Pozwala na używanie kilofa 3x3 | Wszyscy |
| `grzybciokilof.reload` | Pozwala przeładować konfigurację | OP |

🎮 Jak działa?
Kilof automatycznie wykrywa kierunek patrzenia gracza:
- **Patrzenie w górę/dół** → Kopie bloki poziomo (płaszczyzna XZ)
- **Patrzenie na północ/południe** → Kopie bloki w płaszczyźnie XY
- **Patrzenie na wschód/zachód** → Kopie bloki w płaszczyźnie YZ

📦 Instalacja
1. Pobierz plik `GrzybcioKilof-1.0.0.jar`
2. Umieść go w folderze `plugins` na serwerze
3. Zrestartuj serwer
4. Dostosuj konfigurację w `plugins/GrzybcioKilof/config.yml`

📝 Wymagania
- Minecraft 1.21+
- Paper/Spigot/Purpur

---

 🇬🇧 English

Description
**GrzybcioKilof** is a Minecraft plugin that adds a special pickaxe which mines blocks in a 3x3 area! Perfect for players who want to speed up mining and resource gathering.

✨ Features
- 🪓 **3x3 Mining** - Destroys 9 blocks at once instead of one
- 🎯 **Smart Mining** - The direction of block destruction depends on the player's viewing angle
- 💎 **Item Drops** - All destroyed blocks drop as items (respects Fortune and Silk Touch)
- ⚙️ **Fully Configurable** - Customize name, description, enchantments, and mining radius
- 🎨 **HEX Colors** - Support for colors in `&#RRGGBB` format
- 🔒 **Permission System** - Control who can use the pickaxe
- 🛡️ **Compatibility** - Works with land protection plugins (WorldGuard, GriefPrevention)

📋 Commands
| Command | Description | Permission |
|---------|-------------|------------|
| `/grzybcio-kilof` | Gives the pickaxe to yourself | `grzybciokilof.give` |
| `/grzybcio-kilof <player>` | Gives the pickaxe to another player | `grzybciokilof.give` |
| `/grzybcio-kilof reload` | Reloads the configuration | `grzybciokilof.reload` |

🔑 Permissions
| Permission | Description | Default |
|------------|-------------|---------|
| `grzybciokilof.give` | Allows using the /grzybcio-kilof command | OP |
| `grzybciokilof.use` | Allows using the 3x3 pickaxe | Everyone |
| `grzybciokilof.reload` | Allows reloading the configuration | OP |

🎮 How does it work?
The pickaxe automatically detects the player's viewing direction:
- **Looking up/down** → Mines blocks horizontally (XZ plane)
- **Looking north/south** → Mines blocks in the XY plane
- **Looking east/west** → Mines blocks in the YZ plane

📦 Installation
1. Download `GrzybcioKilof-1.0.0.jar`
2. Place it in the `plugins` folder on your server
3. Restart the server
4. Customize the configuration in `plugins/GrzybcioKilof/config.yml`

📝 Requirements
- Minecraft 1.21+
- Paper/Spigot/Purpur

---

  Made with ❤️ by Grzybcio

