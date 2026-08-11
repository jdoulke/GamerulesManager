package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.ChatColor;

public class ColorUtils {

    private ColorUtils() {
    }

    /**
     * Applies legacy (&a, &l, etc.) and hex (&#RRGGBB) color codes.
     * Malformed color codes are preserved as plain text instead of throwing an exception.
     *
     * @param text the text to colorize
     * @return the colorized text
     */
    public static String translateColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (current != '&' || i + 1 >= text.length()) {
                result.append(current);
                continue;
            }

            // Hex format: &#RRGGBB
            if (text.charAt(i + 1) == '#' && i + 7 < text.length()) {
                String hex = text.substring(i + 2, i + 8);
                if (hex.matches("[0-9a-fA-F]{6}")) {
                    result.append(net.md_5.bungee.api.ChatColor.of("#" + hex));
                    i += 7;
                    continue;
                }
            }

            char colorCode = text.charAt(i + 1);
            if (ChatColor.getByChar(colorCode) != null) {
                result.append(ChatColor.COLOR_CHAR).append(Character.toLowerCase(colorCode));
                i++;
                continue;
            }

            // Unknown or incomplete code: keep the '&' as normal text.
            result.append(current);
        }

        return result.toString();
    }
}
