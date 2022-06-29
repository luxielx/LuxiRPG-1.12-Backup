package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Wind3 {
    final public static int manacost = 30;
    public static int cost = 4;
    public static int levelreq = 10;
    public static String name = ChatColor.RED + ChatColor.ITALIC.toString() + "Tứ thức: Thăng Thượng Sa Trần Lam";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Chém xung quanh bản thân sát thương cho kẻ địch xung quanh."});

    public static int cooldown = 10;
    public static double cooldownr = 0.05;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.PINK_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 5, 0.2f);
        Utils.SwingHand(p,false);
        drawTornado(p);


    }

    public static void drawTornado(Player p) {
        int max_height = 3;
        double max_radius = 10;
        int lines = 16;
        double height_increasement = 0.1;
        double radius_increasement = max_radius / max_height;
        int angle = 0;
        ArrayList<LivingEntity> damaged = new ArrayList<>();
        new BukkitRunnable() {
            int l = 0;

            @Override
            public void run() {
                if (l >= lines) this.cancel();
                for (double y = 0; y < max_height; y += height_increasement) {
                    double radius = y * radius_increasement;
                    double x = Math.cos(Math.toRadians(360 / lines * l - y * 25 - angle)) * radius;
                    double z = Math.sin(Math.toRadians(360 / lines * l - y * 25 - angle)) * radius;
                    Utils.coloredRedstone(p.getLocation().clone().add(x, y, z), Color.WHITE);
                }

                for (LivingEntity en : Utils.getNearbyLivingEntities(p.getLocation(), 10)) {
                    if (Utils.damageable(en, p) && !damaged.contains(en)) {
                        SatThuong.SpellDamage(p, en, 2);

                        damaged.add(en);
                    }
                }
                l++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);

    }


}
