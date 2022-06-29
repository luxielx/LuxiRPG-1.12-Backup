package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ThunderCharged {

    final public static int manacost = 20;
    public static int cost = 10;
    public static int levelreq = 0;
    public static String name = ChatColor.AQUA + ChatColor.ITALIC.toString() + "Tam Thức: Tụ Văn Thành Lôi";
    public static int cooldown = 0;
    public static double cooldownr = 0;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();

    public static void r(Player p) {

        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        Location loc = p.getEyeLocation();
        Location loc1 = p.getEyeLocation();
        Location loc2 = p.getEyeLocation();
        Location loc3 = p.getEyeLocation();
        Vector dir = loc.getDirection().normalize().multiply(3);
        loc2.setYaw(loc.getYaw() - 20);
        Vector dir2 = loc2.getDirection().normalize().multiply(3);
        loc3.setYaw(loc.getYaw() + 20);
        Vector dir3 = loc3.getDirection().normalize().multiply(3);
        ArrayList<Location> loclist = new ArrayList<>();
        ArrayList<LivingEntity> enlist = new ArrayList<>();
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 10) {
                    for (Location loz : loclist) {
                        if(Utils.percentRoll(50))
                        p.getWorld().spigot().strikeLightningEffect(loz, true);
                        p.getWorld().spawnParticle(Particle.FLAME, loz, 10);
                        for (LivingEntity en : Utils.getNearbyLivingEntities(loz, 1)) {
                            if (Utils.damageable(en, p)) {
                                SatThuong.SpellDamage(p, en, 2);
                            }
                        }
                    }
                    p.playSound(loc1, Sound.ENTITY_LIGHTNING_THUNDER, 5, 1);
                    p.playSound(loc1, Sound.ENTITY_LIGHTNING_IMPACT, 5, 1);
                    this.cancel();
                }
                loc1.add(dir);
                loclist.add(loc1.clone());
                p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc1, 1);
                p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc1, 10);
                loc2.add(dir2);
                loclist.add(loc2.clone());
                p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc2, 1);
                p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc2, 10);

                loc3.add(dir3);
                loclist.add(loc3.clone());
                p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc3, 1);
                p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc3, 10);
                for (LivingEntity en : Utils.getNearbyLivingEntities(loc1, 1)) {
                    if (Utils.damageable(en, p)) {
                        if(!enlist.contains(en)) {
                            if(Utils.damageable(en,p)) {
                                SatThuong.SpellDamage(p, en, 1);
                                enlist.add(en);
                            }
                        }
                    }
                }
                for (LivingEntity en : Utils.getNearbyLivingEntities(loc2, 1)) {
                    if(!enlist.contains(en)) {
                        if(Utils.damageable(en,p)) {
                            SatThuong.SpellDamage(p, en, 1);
                            enlist.add(en);
                        }
                    }
                }
                for (LivingEntity en : Utils.getNearbyLivingEntities(loc3, 1)) {
                    if(!enlist.contains(en)) {
                        if(Utils.damageable(en,p))
                            if(Utils.damageable(en,p)) {
                                SatThuong.SpellDamage(p, en, 1);
                                enlist.add(en);
                            }
                    }
                }

                i++;
            }
        }.runTaskTimer(Main.m, 0, 3);


    }

}


