package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.FeatureFlag;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Centralizes gamerule registry lookups, key formatting and world availability checks.
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

    /**
     * Returns every gamerule registered by Bukkit, including experimental rules.
     */
    public static List<GameRule<?>> getSortedGameRules() {
        List<GameRule<?>> gamerules = new ArrayList<>();

        for (GameRule gamerule : Registry.GAME_RULE) {
            gamerules.add((GameRule<?>) gamerule);
        }

        gamerules.sort(Comparator.comparing(GameRuleRegistryUtil::getName));
        return gamerules;
    }

    /**
     * Returns only the gamerules that are usable in the supplied world.
     * Experimental gamerules remain in Bukkit's registry even when their required
     * world feature is disabled, so they must be filtered before accessing values.
     */
    public static List<GameRule<?>> getSortedGameRules(World world) {
        List<GameRule<?>> gamerules = new ArrayList<>();

        for (GameRule gamerule : Registry.GAME_RULE) {
            GameRule<?> typedRule = (GameRule<?>) gamerule;
            if (isAvailableInWorld(typedRule, world)) {
                gamerules.add(typedRule);
            }
        }

        gamerules.sort(Comparator.comparing(GameRuleRegistryUtil::getName));
        return gamerules;
    }

    /**
     * Checks feature-gated gamerules against the feature flags enabled in the world.
     */
    public static boolean isAvailableInWorld(GameRule<?> gamerule, World world) {
        if ("max_minecart_speed".equals(getName(gamerule))) {
            return world.getFeatureFlags().contains(FeatureFlag.MINECART_IMPROVEMENTS);
        }

        return true;
    }
}
