package pl.grzybcio.kilof.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern HEX_PATTERN_ALT = Pattern.compile("<#([A-Fa-f0-9]{6})>");

    /**
     * Konwertuje tekst z kodami kolorów na Component
     * Obsługuje:
     * - Standardowe kody (&a, &b, &c, itd.)
     * - HEX w formacie &#RRGGBB
     * - HEX w formacie <#RRGGBB>
     */
    public static Component colorize(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }

        // Konwertuj &#RRGGBB na format MiniMessage <color:#RRGGBB>
        message = convertHexToMiniMessage(message);
        
        // Konwertuj standardowe kody kolorów (&a, &l, itd.) na format MiniMessage
        message = convertLegacyToMiniMessage(message);

        return MiniMessage.miniMessage().deserialize(message);
    }

    /**
     * Konwertuje tekst z kodami kolorów na String z formatowaniem sekcji (§)
     */
    public static String colorizeToString(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        Component component = colorize(message);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    private static String convertHexToMiniMessage(String message) {
        // Konwertuj &#RRGGBB
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "<color:#" + matcher.group(1) + ">");
        }
        matcher.appendTail(buffer);
        message = buffer.toString();

        // Konwertuj <#RRGGBB>
        matcher = HEX_PATTERN_ALT.matcher(message);
        buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "<color:#" + matcher.group(1) + ">");
        }
        matcher.appendTail(buffer);

        return message;
    }

    private static String convertLegacyToMiniMessage(String message) {
        // Kolory
        message = message.replace("&0", "<black>");
        message = message.replace("&1", "<dark_blue>");
        message = message.replace("&2", "<dark_green>");
        message = message.replace("&3", "<dark_aqua>");
        message = message.replace("&4", "<dark_red>");
        message = message.replace("&5", "<dark_purple>");
        message = message.replace("&6", "<gold>");
        message = message.replace("&7", "<gray>");
        message = message.replace("&8", "<dark_gray>");
        message = message.replace("&9", "<blue>");
        message = message.replace("&a", "<green>");
        message = message.replace("&b", "<aqua>");
        message = message.replace("&c", "<red>");
        message = message.replace("&d", "<light_purple>");
        message = message.replace("&e", "<yellow>");
        message = message.replace("&f", "<white>");

        // Formatowanie
        message = message.replace("&l", "<bold>");
        message = message.replace("&m", "<strikethrough>");
        message = message.replace("&n", "<underlined>");
        message = message.replace("&o", "<italic>");
        message = message.replace("&k", "<obfuscated>");
        message = message.replace("&r", "<reset>");

        return message;
    }
}

