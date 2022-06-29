package Util;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class FU {

    static List<String> wd = Arrays.asList(
            // type height width
            "BAT 0.9 0.5", "BLAZE 1.7 0.6", "CAVE_SPIDER 0.5 0.7", "CHICKEN 0.7 0.4", "COW 1.4 0.9", "CREEPER 1.7 0.6",
            "ENDER_DRAGON 8 16", "ENDERMAN 2.9 0.6", "ENDERMITE 0.3 0.4", "GHAST 4 4 ", "GIANT 11.7 3.6",
            "GUARDIAN 0.85 0.85", "HORSE 1.6 1.4", "IRON_GOLEM 2.7 1.4", "MUSHROOM_COW 1.4 0.9", "OCELOT 0.7 0.6",
            "PIG 0.9 0.9", "PIG_ZOMBIE 1.95 0.6", "POLAR_BEAR 1.4 1.3", "PLAYER 1.8 0.6", "RABBIT 0.5 0.4",
            "SHEEP 0.9 1.3", "SHULKER 1 1", "SILVERFISH 0.3 0.4", "SKELETON 1.95 0.6", "SLIME 0.5 0.5",
            "SNOWMAN 1.9 0.7", "SPIDER 0.9 1.4", "SQUID 0.8 0.8", "VILLAGER 1.95 0.6", "WITCH 1.95 0.6",
            "WITHER 3.5 0.9", "WOLF 0.85 0.6", "ZOMBIE 1.95 0.6");

    public static double getHWEntity(Entity en, int pos) {
        if (en == null)
            return 0;

        if (en.getType().toString().toUpperCase().equals("SLIME")) {
            return 0.5 * ((Slime) en).getSize();
        }
        for (String s : wd) {
            if (s.split(" ")[0].equals(en.getType().toString().toUpperCase())) {
                return FNum.rd(s.split(" ")[pos]);
            }
        }
        return 0;
    }

    public static boolean isPlayer(LivingEntity e) {
        boolean a = true;
        if (e.hasMetadata("NPC") || e.hasMetadata("shopkeeper") || e.hasMetadata("mythicmob"))
            a = false;
        return a;
    }

    public static boolean isEntity(LivingEntity e) {
        boolean a = true;
        if (e.hasMetadata("NPC") || e.hasMetadata("shopkeeper"))
            a = false;
        return a;
    }

    public static void addPotionEffect(Entity e, PotionEffectType pet, int dur, int lv) {
        if (e instanceof FallingBlock) {
            return;
        }


        LivingEntity en = (LivingEntity) e;


        for (PotionEffect pe : en.getActivePotionEffects()) {
            if (pe.getType().equals(pet)) {
                // ampli tinh tu 0, lv tinh tu 1
                //
                if (pe.getAmplifier() <= (lv - 1)) {
                    if (pe.getDuration() / 20 <= dur) {
                        en.removePotionEffect(pet);
                    }
                    break;
                }
            }
        }
        en.addPotionEffect(new PotionEffect(pet, dur * 20, lv - 1));
    }

    public static void heal(Player p, int amount) {
        for (int i = 1; i <= amount; i++) {
            if (p.getMaxHealth() - p.getHealth() > 0) {
                p.setHealth(p.getHealth() + 1);
            } else
                break;
        }
    }


    public static boolean isInRegion(Location loc, String region) {

        WorldGuardPlugin guard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (guard == null) {
            Bukkit.getConsoleSender().sendMessage("§4ERROR: §cWorldGuard chua duoc cai dat!");
            return false;
        }

        return guard.getRegionManager(loc.getWorld()).getRegion("afk").contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}
