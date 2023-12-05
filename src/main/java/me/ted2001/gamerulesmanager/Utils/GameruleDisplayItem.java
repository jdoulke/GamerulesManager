package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GameruleDisplayItem {

    public ItemStack gameruleDisplayItem(String gamerule){
        switch (gamerule) {
            case "announceAdvancements":
                return new ItemStack(Material.BOOK);
            case "blockExplosionDropDecay":
                return new ItemStack(Material.FIRE_CHARGE);
            case "commandBlockOutput":
                return new ItemStack(Material.COMMAND_BLOCK);
            case "commandModificationBlockLimit":
                return new ItemStack(Material.REPEATING_COMMAND_BLOCK);
            case "disableElytraMovementCheck":
                return new ItemStack(Material.ELYTRA);
            case "disableRaids":
                return new ItemStack(Material.CROSSBOW);
            case "doDaylightCycle":
                return new ItemStack(Material.CLOCK);
            case "doEntityDrops":
                return new ItemStack(Material.ROTTEN_FLESH);
            case "doFireTick":
                return new ItemStack(Material.FLINT_AND_STEEL);
            case "doImmediateRespawn":
                return new ItemStack(Material.TOTEM_OF_UNDYING);
            case "doInsomnia":
                return new ItemStack(Material.PHANTOM_SPAWN_EGG);
            case "doLimitedCrafting":
                return new ItemStack(Material.CRAFTING_TABLE);
            case "doMobSpawning":
                return new ItemStack(Material.ZOMBIE_SPAWN_EGG);
            case "doMobLoot":
                return new ItemStack(Material.EXPERIENCE_BOTTLE);
            case "doPatrolSpawning":
                return new ItemStack(Material.PILLAGER_SPAWN_EGG);
            case "doTileDrops":
                return new ItemStack(Material.OAK_LOG);
            case "doTraderSpawning":
                return new ItemStack(Material.WANDERING_TRADER_SPAWN_EGG);
            case "doVinesSpread":
                return new ItemStack(Material.VINE);
            case "doWardenSpawning":
                return new ItemStack(Material.WARDEN_SPAWN_EGG);
            case "doWeatherCycle":
                return new ItemStack(Material.SNOWBALL);
            case "enderPearlsVanishOnDeath":
                return new ItemStack(Material.ENDER_PEARL);
            case "fallDamage":
                return new ItemStack(Material.DIAMOND_BOOTS);
            case "fireDamage":
                return new ItemStack(Material.LAVA_BUCKET);
            case "forgiveDeadPlayers":
                return new ItemStack(Material.FEATHER);
            case "freezeDamage":
                return new ItemStack(Material.BLUE_ICE);
            case "globalSoundEvents":
                return new ItemStack(Material.NOTE_BLOCK);
            case "keepInventory":
                return new ItemStack(Material.DIAMOND_SWORD);
            case "lavaSourceConversion":
                return new ItemStack(Material.LAVA_BUCKET);
            case "logAdminCommands":
                return new ItemStack(Material.WRITABLE_BOOK);
            case "maxEntityCramming":
                return new ItemStack(Material.ZOMBIE_HEAD);
            case "mobExplosionDropDecay":
                return new ItemStack(Material.CREEPER_SPAWN_EGG);
            case "mobGriefing":
                return new ItemStack(Material.OAK_DOOR);
            case "naturalRegeneration":
                return new ItemStack(Material.GOLDEN_APPLE);
            case "playersNetherPortalCreativeDelay":
                return new ItemStack(Material.CRYING_OBSIDIAN);
            case "playersNetherPortalDefaultDelay":
                return new ItemStack(Material.OBSIDIAN);
            case "playersSleepingPercentage":
                return new ItemStack(Material.WHITE_BED);
            case "projectilesCanBreakBlocks":
                return new ItemStack(Material.TIPPED_ARROW);
            case "randomTickSpeed":
                return new ItemStack(Material.WHEAT);
            case "reducedDebugInfo":
                return new ItemStack(Material.REDSTONE_TORCH);
            case "sendCommandFeedback":
                return new ItemStack(Material.REDSTONE_LAMP);
            case "showDeathMessages":
                return new ItemStack(Material.PAPER);
            case "snowAccumulationHeight":
                return new ItemStack(Material.SNOW_BLOCK);
            case "spawnRadius":
                return new ItemStack(Material.RED_BED);
            case "spectatorsGenerateChunks":
                return new ItemStack(Material.ENDER_EYE);
            case "tntExplosionDropDecay":
                return new ItemStack(Material.TNT);
            case "universalAnger":
                return new ItemStack(Material.SPIDER_EYE);
            case "waterSourceConversion":
            case "drowningDamage":
                return new ItemStack(Material.WATER_BUCKET);
            case "maxCommandForkCount":
            case "maxCommandChainLength":
                return new ItemStack(Material.CHAIN_COMMAND_BLOCK);
        }
        return new ItemStack(Material.BOOK);
    }
}
