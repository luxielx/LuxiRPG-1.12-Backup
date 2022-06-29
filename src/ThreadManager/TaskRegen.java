package ThreadManager;

import Luxiel.Main;
import Util.Sit;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import stat.StatPlayer;

public class TaskRegen {

    private static int hp = -1;

    public static void runHp() {

        if (hp != -1) {
            Bukkit.getScheduler().cancelTask(hp);
        }

        hp = new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        int bonus = 1;
                        if (p.hasPotionEffect(PotionEffectType.REGENERATION)) {
                            bonus = p.getPotionEffect(PotionEffectType.REGENERATION).getAmplifier();
                        }
                        if (Sit.isSitting(p)) {
                            bonus *= 3;
                        }
                        Utils.Heal(p,(0.5 + StatPlayer.vIt(p) / 10) * bonus,false);
                        /////////////////////

                        /////////////////////
                        if (p.getFoodLevel() > 0 && p.isSprinting() && !(p.hasPotionEffect(PotionEffectType.INVISIBILITY) || p.hasPotionEffect(PotionEffectType.SPEED))) {
                            if(p.getFoodLevel() >= 3)
                            Bukkit.getScheduler().runTask(Main.m, () -> {
                                p.setFoodLevel(p.getFoodLevel() - 1);
                                    });

                        }else{
                            Bukkit.getScheduler().runTask(Main.m, () -> {
                                p.setFoodLevel(p.getFoodLevel() + 1);
                            });
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 20).getTaskId();
    }


}
