package me.ted2001.gamerulesmanager.Utils;

public final class GameRuleNameUtil {

    private GameRuleNameUtil() {
    }

    public static String toConfigKey(String gameruleName) {
        if (gameruleName == null) {
            return "";
        }

        int namespaceSeparator = gameruleName.indexOf(':');
        if (namespaceSeparator >= 0) {
            return gameruleName.substring(namespaceSeparator + 1);
        }

        return gameruleName;
    }

    public static String toDisplayName(String gameruleName) {
        return toConfigKey(gameruleName).replace("_", " ");
    }
}