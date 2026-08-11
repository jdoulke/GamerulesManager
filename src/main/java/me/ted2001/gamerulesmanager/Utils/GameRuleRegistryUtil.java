package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

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
        return Registry.GAME_RULE.stream()
                .map(gamerule -> (GameRule<?>) gamerule)
                .sorted(Comparator.comparing(GameRuleRegistryUtil::getName))
                .toList();
    }
}
