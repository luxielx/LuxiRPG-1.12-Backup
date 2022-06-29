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

import static skill.Wind2.casting;

public class WindCharged {

    final public static int manacost = 20;
    public static int cost = 10;
    public static int levelreq = 1;
    public static String name = ChatColor.RED + ChatColor.ITALIC.toString() + "Lục Thức: Hắc Phong Yên Lam";
    public static int cooldown = 0;
    public static double cooldownr = 0;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();

    public static void r(Player p) {
        if (casting.contains(p.getUniqueId())) {
            if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, 0, lastuse)) return;

        }else{
            if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        }
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));

        Location oloc = p.getLocation();
        oloc.setPitch(0);
        oloc.setYaw(0);

        int sl = 8;
        new BukkitRunnable() {
            int z = 0;

            @Override
            public void run() {
                if (z >= sl) this.cancel();
                Location loc = oloc.clone();
                loc.setYaw(z * (360 / sl));
                Vector dir = loc.getDirection().normalize();

                for (int i = 0; i <= 10; i++) {
                    loc.add(dir);
                    loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);
                }

                z++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);
        p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().add(0, 0.2, 0), 100);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 5, 1f);
        ArrayList<LivingEntity> damaged = new ArrayList<>();
        for (LivingEntity en : Utils.getNearbyLivingEntities(p.getLocation(), 10)) {
            if (Utils.damageable(en, p) && !damaged.contains(en)) {
                SatThuong.SpellDamage(p, en, 1.5);
                en.setVelocity(en.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().add(new Vector(0, 0.3, 0).multiply(1.2)));
                damaged.add(en);
            }
        }


    }


}


