package pl.grzybcio.kilof.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.grzybcio.kilof.GrzybcioKilof;
import pl.grzybcio.kilof.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KilofCommand implements CommandExecutor, TabCompleter {

    private final GrzybcioKilof plugin;

    public KilofCommand(GrzybcioKilof plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Sprawdź uprawnienia
        if (!sender.hasPermission("grzybciokilof.give")) {
            sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.brak-uprawnien", "&cNie masz uprawnień!")));
            return true;
        }

        // Komenda reload
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("grzybciokilof.reload")) {
                sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.brak-uprawnien", "&cNie masz uprawnień!")));
                return true;
            }
            plugin.reloadConfiguration();
            sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.przeladowano", "&aKonfiguracja przeładowana!")));
            return true;
        }

        Player targetPlayer;

        if (args.length > 0) {
            // Daj kilof innemu graczowi
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.gracz-offline", "&cGracz nie jest online!")));
                return true;
            }
            
            // Daj kilof
            plugin.getKilofManager().giveKilof(targetPlayer);
            
            // Wyślij wiadomości
            String messageSender = plugin.getConfig().getString("wiadomosci.dano-kilof", "&aDałeś kilof graczowi %player%!")
                    .replace("%player%", targetPlayer.getName());
            sender.sendMessage(ColorUtils.colorize(messageSender));
            
            targetPlayer.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.otrzymano-kilof", "&aOtrzymałeś kilof!")));
        } else {
            // Daj kilof sobie (tylko dla graczy)
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.tylko-gracz", "&cTa komenda może być wykonana tylko przez gracza!")));
                return true;
            }
            
            targetPlayer = (Player) sender;
            plugin.getKilofManager().giveKilof(targetPlayer);
            targetPlayer.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("wiadomosci.otrzymano-kilof", "&aOtrzymałeś kilof!")));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Dodaj nazwy graczy online
            completions.addAll(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList()));
            
            // Dodaj opcję reload jeśli ma uprawnienia
            if (sender.hasPermission("grzybciokilof.reload") && "reload".startsWith(args[0].toLowerCase())) {
                completions.add("reload");
            }
        }

        return completions;
    }
}

