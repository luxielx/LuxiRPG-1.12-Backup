package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Wind4 {
    final public static int manacost = 100;
    public static int cost = 4;
    public static int levelreq = 20;
    public static String name = ChatColor.GOLD + ChatColor.ITALIC.toString() + "Thất Thức: Kính Phong";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Chém liên lục 10 nhát vào không trung về phía trước."});
    public static int cooldown = 30;
    public static double cooldownr = 0.1;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.GRAY_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));


//        for(int ory = -10; ory <= 10; ory++)
//        summonWindSlash(loz, dir, angle, radius, -10, dustOptions);
        boolean opp = false;
        if (Utils.percentRoll(50)) opp = true;
        boolean finalOpp = opp;
        new BukkitRunnable() {
            final double radius = 2.5;
            int ory = -5;

            @Override
            public void run() {
                if (ory >= 5) this.cancel();
                Utils.SwingHand(p, false);

                if (finalOpp) {
                    summonWindSlash(p, radius, ory);
                } else {
                    summonWindSlash(p, radius, -ory);
                }


                ory++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 7);


    }

    private static void summonWindSlash(Player p, double radius, double ory) {
        Location loz = p.getEyeLocation();
        loz.setPitch(0);
        Vector dir = loz.getDirection().normalize();
        double angle = Math.toRadians(p.getLocation().getYaw());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_BREATHE, 1, 0f);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 0f);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 0f);
        new BukkitRunnable() {
            final double oy = ory;
            int l = 0;
            double plus = -oy / 31.4;
            ArrayList<LivingEntity> damaged = new ArrayList<>();

            @Override
            public void run() {
                if (l >= 30) this.cancel();
//                int asd = 0;
                double y = oy;
                for (double i = angle + Math.toRadians(20); i <= Math.PI + angle - Math.toRadians(20); i += 0.05) {
                    double x = radius * Math.cos(i);
                    double z = radius * Math.sin(i);
                    y += plus;
                    Utils.coloredRedstone(loz.clone().add(x, y, z), 254, 254, 254);
                }
                Utils.getNearbyLivingEntities(loz, 5).stream().forEach(en ->{
                    if (Utils.damageable(en, p) && !damaged.contains(en)) {
                        SatThuong.SpellDamage(p, en, 1);
                        damaged.add(en);
                    }
                });
                loz.add(dir);
                l++;

            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);

    }


}
