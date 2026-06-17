package pl.grzybcio.kilof.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.grzybcio.kilof.GrzybcioKilof;

import java.util.ArrayList;
import java.util.List;

public class KilofManager {

    private final GrzybcioKilof plugin;
    private final NamespacedKey kilofKey;

    public KilofManager(GrzybcioKilof plugin) {
        this.plugin = plugin;
        this.kilofKey = new NamespacedKey(plugin, "grzybcio_kilof");
    }

    /**
     * Tworzy ItemStack kilofa 3x3 na podstawie konfiguracji
     */
    public ItemStack createKilof() {
        // Pobierz typ kilofa z konfiguracji
        String typeName = plugin.getConfig().getString("kilof-typ", "NETHERITE_PICKAXE");
        Material material;
        try {
            material = Material.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Nieprawidłowy typ kilofa w konfiguracji: " + typeName + ". Używam NETHERITE_PICKAXE.");
            material = Material.NETHERITE_PICKAXE;
        }

        ItemStack kilof = new ItemStack(material);
        ItemMeta meta = kilof.getItemMeta();

        if (meta == null) {
            return kilof;
        }

        // Ustaw nazwę
        String name = plugin.getConfig().getString("kilof-nazwa", "&#7ED4E6&lKilof Dusz");
        meta.displayName(ColorUtils.colorize(name));

        // Ustaw lore
        List<String> loreConfig = plugin.getConfig().getStringList("kilof-lore");
        if (!loreConfig.isEmpty()) {
            List<net.kyori.adventure.text.Component> lore = new ArrayList<>();
            for (String line : loreConfig) {
                lore.add(ColorUtils.colorize(line));
            }
            meta.lore(lore);
        }

        // Dodaj enchantmenty
        ConfigurationSection enchants = plugin.getConfig().getConfigurationSection("kilof-enchantmenty");
        if (enchants != null) {
            for (String key : enchants.getKeys(false)) {
                try {
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
                    if (enchantment != null) {
                        int level = enchants.getInt(key);
                        meta.addEnchant(enchantment, level, true);
                    } else {
                        plugin.getLogger().warning("Nieznany enchantment: " + key);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Błąd przy dodawaniu enchantmentu " + key + ": " + e.getMessage());
                }
            }
        }

        // Dodaj efekt glow jeśli włączony
        if (plugin.getConfig().getBoolean("kilof-glow", true)) {
            if (meta.getEnchants().isEmpty()) {
                // Dodaj ukryty enchant dla efektu świecenia
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        // Dodaj unikalny identyfikator do PersistentDataContainer
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(kilofKey, PersistentDataType.BYTE, (byte) 1);

        // Ukryj atrybuty
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        kilof.setItemMeta(meta);
        return kilof;
    }

    /**
     * Sprawdza czy dany ItemStack to kilof GrzybcioMC
     */
    public boolean isGrzybcioKilof(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(kilofKey, PersistentDataType.BYTE);
    }

    /**
     * Daje kilof graczowi
     */
    public void giveKilof(Player player) {
        ItemStack kilof = createKilof();
        
        // Sprawdź czy jest miejsce w ekwipunku
        if (player.getInventory().firstEmpty() == -1) {
            // Upuść item na ziemię
            player.getWorld().dropItemNaturally(player.getLocation(), kilof);
        } else {
            player.getInventory().addItem(kilof);
        }
    }
}

