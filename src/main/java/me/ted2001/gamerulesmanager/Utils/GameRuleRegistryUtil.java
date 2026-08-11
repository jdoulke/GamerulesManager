package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Centralizes gamerule registry lookups and key formatting.
 */
public final class GameRuleRegistryUtil {

    private GameRuleRegistryUtil() {
    }

    public static GameRule<?> getByName(String gameruleName) {
        NamespacedKey key = NamespacedKey.fromString(gameruleName);
        if (key == null) {
            return null;
        }

        return Registry.GAME_RULE.get(key);
    }

    public static String getName(GameRule<?> gamerule) {
        return gamerule.getKeyOrThrow().getKey();
    }

    public static List<GameRule<?>> getSortedGameRules() {
        List<GameRule<?>> gamerules = new ArrayList<>();

        for (GameRule gamerule : Registry.GAME_RULE) {
            gamerules.add((GameRule<?>) gamerule);
        }

        gamerules.sort(Comparator.comparing(GameRuleRegistryUtil::getName));
        return gamerules;
    }
}
