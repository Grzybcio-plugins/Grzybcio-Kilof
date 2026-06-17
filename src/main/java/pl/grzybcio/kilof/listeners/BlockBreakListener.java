package pl.grzybcio.kilof.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.grzybcio.kilof.GrzybcioKilof;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockBreakListener implements Listener {

    private final GrzybcioKilof plugin;
    private final Set<Player> isBreaking = new HashSet<>();

    public BlockBreakListener(GrzybcioKilof plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        // Zapobiegaj nieskończonej pętli
        if (isBreaking.contains(player)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        
        // Sprawdź czy to nasz kilof
        if (!plugin.getKilofManager().isGrzybcioKilof(item)) {
            return;
        }

        // Sprawdź uprawnienia jeśli wymagane
        if (plugin.getConfig().getBoolean("wymagaj-uprawnienia", false) && 
            !player.hasPermission("grzybciokilof.use")) {
            return;
        }

        // Nie działaj w trybie widza
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        Block centerBlock = event.getBlock();
        
        // Pobierz kierunek patrzenia gracza
        BlockFace face = getTargetBlockFace(player);
        
        // Pobierz promień kopania z konfiguracji
        int radius = plugin.getConfig().getInt("kopanie-promien", 1);
        
        // Pobierz bloki do zniszczenia
        List<Block> blocksToBreak = getBlocksToBreak(centerBlock, face, radius);
        
        // Pobierz listę wykluczonych bloków
        List<String> excludedBlocks = plugin.getConfig().getStringList("bloki-wykluczone");
        
        isBreaking.add(player);
        
        try {
            for (Block block : blocksToBreak) {
                // Pomiń centralny blok (zostanie zniszczony przez oryginalne wydarzenie)
                if (block.equals(centerBlock)) {
                    continue;
                }
                
                // Sprawdź czy blok nie jest wykluczony (np. BEDROCK)
                if (excludedBlocks.contains(block.getType().name())) {
                    continue;
                }
                
                // Sprawdź czy blok nie jest powietrzem
                if (block.getType().isAir()) {
                    continue;
                }
                
                // Sprawdź czy blok jest solidny (nie jest płynem, ogniem itp.)
                if (block.isLiquid()) {
                    continue;
                }
                
                // Wywołaj wydarzenie dla innych pluginów (ochrona terenu itp.)
                BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
                plugin.getServer().getPluginManager().callEvent(breakEvent);
                
                if (!breakEvent.isCancelled()) {
                    // Zniszcz blok z dropem itemów
                    if (player.getGameMode() == GameMode.CREATIVE) {
                        block.setType(Material.AIR);
                    } else {
                        // breakNaturally(item) niszczy blok i dropuje itemy z uwzględnieniem enchantów (Fortune, Silk Touch)
                        block.breakNaturally(item);
                    }
                }
            }
        } finally {
            isBreaking.remove(player);
        }
    }

    private BlockFace getTargetBlockFace(Player player) {
        // Pobierz kierunek patrzenia gracza
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        // Jeśli gracz patrzy w górę lub w dół (kąt > 45 stopni)
        if (pitch < -45) {
            return BlockFace.UP;
        } else if (pitch > 45) {
            return BlockFace.DOWN;
        }

        // Kierunek poziomy - normalizuj yaw do 0-360
        yaw = (yaw % 360 + 360) % 360;

        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }

    private List<Block> getBlocksToBreak(Block center, BlockFace face, int radius) {
        List<Block> blocks = new ArrayList<>();

        int[] xOffsets;
        int[] yOffsets;
        int[] zOffsets;

        switch (face) {
            case UP:
            case DOWN:
                // Kopanie w poziomie (płaszczyzna XZ)
                xOffsets = getRange(radius);
                yOffsets = new int[]{0};
                zOffsets = getRange(radius);
                break;
            case NORTH:
            case SOUTH:
                // Kopanie w płaszczyźnie XY
                xOffsets = getRange(radius);
                yOffsets = getRange(radius);
                zOffsets = new int[]{0};
                break;
            case EAST:
            case WEST:
                // Kopanie w płaszczyźnie YZ
                xOffsets = new int[]{0};
                yOffsets = getRange(radius);
                zOffsets = getRange(radius);
                break;
            default:
                xOffsets = getRange(radius);
                yOffsets = getRange(radius);
                zOffsets = new int[]{0};
                break;
        }

        for (int xOff : xOffsets) {
            for (int yOff : yOffsets) {
                for (int zOff : zOffsets) {
                    blocks.add(center.getRelative(xOff, yOff, zOff));
                }
            }
        }

        return blocks;
    }

    private int[] getRange(int radius) {
        int[] range = new int[radius * 2 + 1];
        for (int i = 0; i < range.length; i++) {
            range[i] = -radius + i;
        }
        return range;
    }
}

