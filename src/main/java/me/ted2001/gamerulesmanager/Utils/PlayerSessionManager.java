package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PlayerSessionManager {

    private static final Map<UUID, World> selectedWorlds = new HashMap<>();
    private static final Map<UUID, World> copiedFromWorlds = new HashMap<>();
    private static final Map<UUID, List<CopyGamerules>> copiedGamerules = new HashMap<>();

    private PlayerSessionManager() {
    }

    public static void setSelectedWorld(Player player, World world) {
        selectedWorlds.put(player.getUniqueId(), world);
    }

    public static World getSelectedWorld(Player player) {
        return selectedWorlds.get(player.getUniqueId());
    }

    public static void setCopiedGamerules(Player player, World sourceWorld, List<CopyGamerules> gamerules) {
        copiedFromWorlds.put(player.getUniqueId(), sourceWorld);
        copiedGamerules.put(player.getUniqueId(), new ArrayList<>(gamerules));
    }

    public static boolean hasCopiedGamerules(Player player) {
        List<CopyGamerules> gamerules = copiedGamerules.get(player.getUniqueId());
        return gamerules != null && !gamerules.isEmpty();
    }

    public static List<CopyGamerules> getCopiedGamerules(Player player) {
        return copiedGamerules.getOrDefault(player.getUniqueId(), Collections.emptyList());
    }

    public static World getCopiedFromWorld(Player player) {
        return copiedFromWorlds.get(player.getUniqueId());
    }

    public static void clear(Player player) {
        UUID uuid = player.getUniqueId();
        selectedWorlds.remove(uuid);
        copiedFromWorlds.remove(uuid);
        copiedGamerules.remove(uuid);
    }
}